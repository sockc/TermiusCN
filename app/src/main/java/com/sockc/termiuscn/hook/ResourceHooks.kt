package com.sockc.termiuscn.hook

import android.content.res.Resources
import com.sockc.termiuscn.data.TranslateRepo
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

object ResourceHooks {
    fun install(lpparam: XC_LoadPackage.LoadPackageParam) {
        hookGetString(lpparam)
    }

    private fun hookGetString(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            Resources::class.java,
            "getString",
            Int::class.javaPrimitiveType,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val resId = param.args.getOrNull(0) as? Int ?: return
                    if (!shouldHandle(param.thisObject as? Resources, resId, lpparam.packageName)) return
                    if (shouldSkipForSettings()) return
                    val original = param.result as? String ?: return
                    val translated = TranslateRepo.translate(original) ?: return
                    param.result = translated
                }
            }
        )
    }

    private fun shouldHandle(resources: Resources?, resId: Int, targetPackage: String): Boolean {
        if (resources == null || resId == 0) return false
        return runCatching {
            resources.getResourcePackageName(resId) == targetPackage
        }.getOrDefault(false)
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
