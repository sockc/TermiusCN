package com.sockc.termiuscn.hook

import com.sockc.termiuscn.data.TranslateRepo
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object ComposeHooks {
    fun install(lpparam: XC_LoadPackage.LoadPackageParam) {
        runCatching {
            val clazz = Class.forName(
                "androidx.compose.ui.res.StringResources_androidKt",
                false,
                lpparam.classLoader
            )

            XposedBridge.hookAllMethods(clazz, "stringResource", object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val original = param.result as? String ?: return
                    val translated = TranslateRepo.translate(original) ?: return
                    param.result = translated
                }
            })

            XposedBridge.log("TermiusCN: Compose stringResource hooks installed")
        }.onFailure {
            XposedBridge.log("TermiusCN: failed to hook Compose stringResource")
            XposedBridge.log(it)
        }
    }
}
