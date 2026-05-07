package com.sockc.termiuscn.hook

import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object ResourceHooks {
    fun install(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("TermiusCN: Resource hooks skipped for homepage stability in ${lpparam.packageName}")
    }
}
