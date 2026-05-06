package com.sockc.termiuscn.hook

import com.sockc.termiuscn.data.TranslateRepo
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

object ToolbarHooks {
    fun install(lpparam: XC_LoadPackage.LoadPackageParam) {
        runCatching {
            XposedHelpers.findAndHookMethod(
                "androidx.appcompat.widget.Toolbar",
                lpparam.classLoader,
                "setTitle",
                CharSequence::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val original = param.args.getOrNull(0) as? CharSequence ?: return
                        val translated = TranslateRepo.translate(original.toString()) ?: return
                        param.args[0] = translated
                    }
                }
            )
        }

        runCatching {
            XposedHelpers.findAndHookMethod(
                "com.google.android.material.appbar.MaterialToolbar",
                lpparam.classLoader,
                "setTitle",
                CharSequence::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val original = param.args.getOrNull(0) as? CharSequence ?: return
                        val translated = TranslateRepo.translate(original.toString()) ?: return
                        param.args[0] = translated
                    }
                }
            )
        }
    }
}
