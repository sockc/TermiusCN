package com.sockc.termiuscn.hook

import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object TermiusHooks {
    fun install(lpparam: XC_LoadPackage.LoadPackageParam) {
        PreferenceHooks.install(lpparam)
        ResourceHooks.install(lpparam)
        ComposeHooks.install(lpparam)
        TextHooks.install()
        ToastHooks.install()
        ToolbarHooks.install(lpparam)
        MenuHooks.install()
        XposedBridge.log("TermiusCN: guarded Compose hooks enabled")
        XposedBridge.log("TermiusCN: hooks installed for ${lpparam.packageName}")
    }
}
