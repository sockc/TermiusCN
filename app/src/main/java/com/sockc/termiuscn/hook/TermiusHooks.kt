package com.sockc.termiuscn.hook

import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object TermiusHooks {
    fun install(lpparam: XC_LoadPackage.LoadPackageParam) {
        TextHooks.install()
        ToastHooks.install()
        ToolbarHooks.install(lpparam)
        XposedBridge.log("TermiusCN: hooks installed")
    }
}
