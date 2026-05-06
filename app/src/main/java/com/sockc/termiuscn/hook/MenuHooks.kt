package com.sockc.termiuscn.hook

import com.sockc.termiuscn.data.TranslateRepo
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object MenuHooks {
    fun install() {
        runCatching {
            XposedHelpers.findAndHookMethod(
                "android.view.MenuItem",
                null,
                "setTitle",
                CharSequence::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val original = param.args.getOrNull(0) as? CharSequence ?: return
                        TranslateRepo.translate(original.toString())?.let { param.args[0] = it }
                    }
                }
            )
        }

        runCatching {
            XposedHelpers.findAndHookMethod(
                "androidx.appcompat.view.menu.MenuItemImpl",
                null,
                "setTitle",
                CharSequence::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val original = param.args.getOrNull(0) as? CharSequence ?: return
                        TranslateRepo.translate(original.toString())?.let { param.args[0] = it }
                    }
                }
            )
        }
    }
}
