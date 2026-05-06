package com.sockc.termiuscn.hook

import com.sockc.termiuscn.data.TranslateRepo
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object ComposeHooks {
    fun install(lpparam: XC_LoadPackage.LoadPackageParam) {
        hookStringResource(lpparam)
        hookAnnotatedString(lpparam)
        hookAnnotatedStringBuilder(lpparam)
        hookTextKt(lpparam)
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

    private fun hookTextKt(lpparam: XC_LoadPackage.LoadPackageParam) {
        val candidates = listOf(
            "androidx.compose.material.TextKt",
            "androidx.compose.material3.TextKt"
        )
        for (name in candidates) {
            val clazz = runCatching { Class.forName(name, false, lpparam.classLoader) }.getOrNull() ?: continue
            runCatching {
                XposedBridge.hookAllMethods(clazz, "Text", object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (param.args.isEmpty()) return
                        val original = (param.args[0] as? CharSequence)?.toString() ?: return
                        val translated = TranslateRepo.translate(original) ?: return
                        param.args[0] = translated
                    }
                })
                XposedBridge.log("TermiusCN: Compose Text hooks installed via $name")
            }
        }
    }
}
