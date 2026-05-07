package com.sockc.termiuscn.hook

import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object ComposeHooks {
    fun install(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("TermiusCN: Compose hooks skipped for stability in ${lpparam.packageName}")
    }
}
