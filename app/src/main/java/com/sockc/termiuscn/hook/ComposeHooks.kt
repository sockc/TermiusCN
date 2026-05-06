package com.sockc.termiuscn.hook

import com.sockc.termiuscn.data.TranslateRepo
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.reflect.Method

object ComposeHooks {
    fun install(lpparam: XC_LoadPackage.LoadPackageParam) {
        hookStringResource(lpparam)
        hookAnnotatedString(lpparam)
        hookAnnotatedStringBuilder(lpparam)
        hookComposeTextLikeMethods(lpparam)
    }

    private fun hookStringResource(lpparam: XC_LoadPackage.LoadPackageParam) {
        val candidates = listOf(
            "androidx.compose.ui.res.StringResources_androidKt",
            "androidx.compose.ui.res.StringResourcesKt"
        )

        var installed = false
        for (name in candidates) {
            val clazz = runCatching { Class.forName(name, false, lpparam.classLoader) }.getOrNull() ?: continue
            runCatching {
                XposedBridge.hookAllMethods(clazz, "stringResource", object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val original = param.result as? String ?: return
                        val translated = TranslateRepo.translate(original) ?: return
                        param.result = translated
                    }
                })
                installed = true
                XposedBridge.log("TermiusCN: Compose stringResource hooks installed via $name")
            }
        }

        if (!installed) {
            XposedBridge.log("TermiusCN: Compose stringResource class not found, fallback hooks only")
        }
    }

    private fun hookAnnotatedString(lpparam: XC_LoadPackage.LoadPackageParam) {
        val clazz = runCatching {
            Class.forName("androidx.compose.ui.text.AnnotatedString", false, lpparam.classLoader)
        }.getOrNull() ?: return

        runCatching {
            XposedBridge.hookAllConstructors(clazz, object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (param.args.isEmpty()) return
                    val original = (param.args[0] as? CharSequence)?.toString() ?: return
                    val translated = TranslateRepo.translate(original) ?: return
                    param.args[0] = translated
                }
            })
            XposedBridge.log("TermiusCN: AnnotatedString constructor hooks installed")
        }.onFailure {
            XposedBridge.log("TermiusCN: failed to hook AnnotatedString constructors")
            XposedBridge.log(it)
        }
    }

    private fun hookAnnotatedStringBuilder(lpparam: XC_LoadPackage.LoadPackageParam) {
        val clazz = runCatching {
            Class.forName("androidx.compose.ui.text.AnnotatedString\$Builder", false, lpparam.classLoader)
        }.getOrNull() ?: return

        runCatching {
            XposedBridge.hookAllMethods(clazz, "append", object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (param.args.isEmpty()) return
                    val original = (param.args[0] as? CharSequence)?.toString() ?: return
                    val translated = TranslateRepo.translate(original) ?: return
                    param.args[0] = translated
                }
            })
            XposedBridge.log("TermiusCN: AnnotatedString.Builder append hooks installed")
        }.onFailure {
            XposedBridge.log("TermiusCN: failed to hook AnnotatedString.Builder append")
            XposedBridge.log(it)
        }
    }

    private fun hookComposeTextLikeMethods(lpparam: XC_LoadPackage.LoadPackageParam) {
        val candidates = listOf(
            "androidx.compose.material.TextKt",
            "androidx.compose.material3.TextKt",
            "androidx.compose.foundation.text.BasicTextKt",
            "androidx.compose.foundation.text.BasicText_androidKt"
        )

        for (name in candidates) {
            val clazz = runCatching { Class.forName(name, false, lpparam.classLoader) }.getOrNull() ?: continue
            val methods = clazz.declaredMethods.filter { method ->
                method.name.startsWith("Text") || method.name.startsWith("BasicText")
            }
            if (methods.isEmpty()) continue

            methods.forEach { method -> hookMethodIfUseful(method) }
            XposedBridge.log("TermiusCN: Compose text-like hooks installed via $name (${methods.size} methods)")
        }
    }

    private fun hookMethodIfUseful(method: Method) {
        runCatching {
            XposedBridge.hookMethod(method, object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (param.args.isEmpty()) return
                    val first = param.args[0] ?: return

                    when (first) {
                        is String -> {
                            val translated = TranslateRepo.translate(first) ?: return
                            param.args[0] = translated
                        }
                        is CharSequence -> {
                            val translated = TranslateRepo.translate(first.toString()) ?: return
                            param.args[0] = translated
                        }
                        else -> {
                            if (first.javaClass.name == "androidx.compose.ui.text.AnnotatedString") {
                                val original = first.toString()
                                val translated = TranslateRepo.translate(original) ?: return
                                val replaced = buildAnnotatedString(first.javaClass, translated) ?: return
                                param.args[0] = replaced
                            }
                        }
                    }
                }
            })
        }
    }

    private fun buildAnnotatedString(clazz: Class<*>, text: String): Any? {
        return runCatching {
            clazz.getConstructor(String::class.java).newInstance(text)
        }.getOrElse {
            runCatching {
                clazz.declaredConstructors.firstOrNull { c ->
                    val p = c.parameterTypes
                    p.isNotEmpty() && p[0] == String::class.java
                }?.let { ctor ->
                    ctor.isAccessible = true
                    val args = Array(ctor.parameterCount) { index ->
                        when (val t = ctor.parameterTypes[index]) {
                            String::class.java -> text
                            java.util.List::class.java -> emptyList<Any>()
                            Int::class.javaPrimitiveType, Integer::class.java -> 0
                            Boolean::class.javaPrimitiveType, java.lang.Boolean::class.java -> false
                            else -> null
                        }
                    }
                    ctor.newInstance(*args)
                }
            }.getOrNull()
        }
    }
}
