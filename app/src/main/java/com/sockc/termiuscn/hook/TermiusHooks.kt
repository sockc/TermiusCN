package com.sockc.termiuscn.hook

import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object TermiusHooks {
    fun install(lpparam: XC_LoadPackage.LoadPackageParam) {
        PreferenceHooks.install(lpparam)
        TextHooks.install()
        ToastHooks.install()
        ToolbarHooks.install(lpparam)
        MenuHooks.install()
        XposedBridge.log("TermiusCN: homepage/resource hooks disabled for stability")
        XposedBridge.log("TermiusCN: hooks installed for ${lpparam.packageName}")
    }
}
