package com.sockc.termiuscn.hook

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

object PreferenceHooks {
    fun install(lpparam: XC_LoadPackage.LoadPackageParam) {
        val candidates = listOf(
            "androidx.preference.PreferenceFragmentCompat",
            "androidx.preference.PreferenceFragment"
        )

        for (name in candidates) {
            val clazz = runCatching { Class.forName(name, false, lpparam.classLoader) }.getOrNull() ?: continue
            hookLifecycle(clazz, "onCreate") { PreferenceState.setActive(true) }
            hookLifecycle(clazz, "onStart") { PreferenceState.setActive(true) }
            hookLifecycle(clazz, "onResume") { PreferenceState.setActive(true) }
            hookLifecycle(clazz, "onPause") { PreferenceState.setActive(false) }
            hookLifecycle(clazz, "onStop") { PreferenceState.setActive(false) }
            hookLifecycle(clazz, "onDestroy") { PreferenceState.setActive(false) }
            XposedBridge.log("TermiusCN: preference lifecycle hooks installed via $name")
        }
    }

    private fun hookLifecycle(clazz: Class<*>, methodName: String, block: () -> Unit) {
        runCatching {
            XposedBridge.hookAllMethods(clazz, methodName, object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    block()
                }
            })
        }.recoverCatching {
            XposedHelpers.findAndHookMethod(
                clazz,
                methodName,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        block()
                    }
                }
            )
        }
    }
}
