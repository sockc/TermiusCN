package com.sockc.termiuscn.hook

import android.content.res.Resources
import com.sockc.termiuscn.data.TranslateRepo
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

object ResourcesHooks {
    fun install() {
        runCatching {
            XposedHelpers.findAndHookMethod(
                Resources::class.java,
                "getText",
                Int::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val original = param.result as? CharSequence ?: return
                        val translated = TranslateRepo.translate(original.toString()) ?: return
                        param.result = translated
                    }
                }
            )
        }.onFailure { XposedBridge.log(it) }

        runCatching {
            XposedHelpers.findAndHookMethod(
                Resources::class.java,
                "getString",
                Int::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val original = param.result as? String ?: return
                        val translated = TranslateRepo.translate(original) ?: return
                        param.result = translated
                    }
                }
            )
        }.onFailure { XposedBridge.log(it) }

        runCatching {
            XposedHelpers.findAndHookMethod(
                Resources::class.java,
                "getString",
                Int::class.javaPrimitiveType,
                Array<Any>::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val original = param.result as? String ?: return
                        val translated = TranslateRepo.translate(original) ?: return
                        param.result = translated
                    }
                }
            )
        }.onFailure { XposedBridge.log(it) }
    }
}
