package com.sockc.termiuscn.hook

import com.sockc.termiuscn.data.TranslateRepo
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object ComposeHooks {
    fun install(lpparam: XC_LoadPackage.LoadPackageParam) {
        hookTextComposable(lpparam)
        hookBasicTextComposable(lpparam)
    }

    private fun hookTextComposable(lpparam: XC_LoadPackage.LoadPackageParam) {
        val candidates = listOf(
            "androidx.compose.material.TextKt",
            "androidx.compose.material3.TextKt"
        )
        for (name in candidates) {
            val clazz = runCatching { Class.forName(name, false, lpparam.classLoader) }.getOrNull()
            if (clazz == null) {
                XposedBridge.log("TermiusCN: Compose Text class not found: $name")
                continue
            }
            runCatching {
                XposedBridge.hookAllMethods(clazz, "Text", object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (shouldSkipForSettings()) return
                        if (param.args.isEmpty()) return
                        val original = (param.args[0] as? CharSequence)?.toString() ?: return
                        val translated = TranslateRepo.translate(original) ?: return
                        param.args[0] = translated
                    }
                })
                XposedBridge.log("TermiusCN: Compose Text hooks installed via $name")
            }.onFailure {
                XposedBridge.log("TermiusCN: failed Compose Text via $name")
                XposedBridge.log(it)
            }
        }
    }

    private fun hookBasicTextComposable(lpparam: XC_LoadPackage.LoadPackageParam) {
        val candidates = listOf(
            "androidx.compose.foundation.text.BasicTextKt",
            "androidx.compose.foundation.text.BasicText_androidKt"
        )
        for (name in candidates) {
            val clazz = runCatching { Class.forName(name, false, lpparam.classLoader) }.getOrNull()
            if (clazz == null) {
                XposedBridge.log("TermiusCN: Compose BasicText class not found: $name")
                continue
            }
            runCatching {
                XposedBridge.hookAllMethods(clazz, "BasicText", object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (shouldSkipForSettings()) return
                        if (param.args.isEmpty()) return
                        val original = (param.args[0] as? CharSequence)?.toString() ?: return
                        val translated = TranslateRepo.translate(original) ?: return
                        param.args[0] = translated
                    }
                })
                XposedBridge.log("TermiusCN: Compose BasicText hooks installed via $name")
            }.onFailure {
                XposedBridge.log("TermiusCN: failed Compose BasicText via $name")
                XposedBridge.log(it)
            }
        }
    }

    private fun shouldSkipForSettings(): Boolean {
        if (PreferenceState.isActive()) return true
        return Throwable().stackTrace.any { element ->
            val name = element.className
            name.startsWith("androidx.preference.") ||
                name.contains("PreferenceFragmentCompat") ||
                name.contains("PreferenceScreen") ||
                name.contains("PreferenceGroup") ||
                name.contains("PreferenceInflater")
        }
    }
}
