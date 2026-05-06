package com.sockc.termiuscn.hook

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

class XposedInit : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != TARGET_PACKAGE) return

        try {
            TermiusHooks.install(lpparam)
            XposedBridge.log("TermiusCN: injected into ${lpparam.packageName}")
        } catch (t: Throwable) {
            XposedBridge.log(t)
        }
    }

    companion object {
        const val TARGET_PACKAGE = "com.server.auditor.ssh.client"
    }
}
