package com.sockc.termiuscn.hook

import android.content.res.Resources
import com.sockc.termiuscn.data.TranslateRepo
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

object ResourcesHooks {
    private const val TARGET_PKG = "com.server.auditor.ssh.client"

    fun install() {
        runCatching {
            XposedHelpers.findAndHookMethod(
                Resources::class.java,
                "getText",
                Int::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val resId = param.args.getOrNull(0) as? Int ?: return
                        if (!shouldTranslate(param.thisObject as? Resources, resId)) return
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
                        val resId = param.args.getOrNull(0) as? Int ?: return
                        if (!shouldTranslate(param.thisObject as? Resources, resId)) return
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
                        val resId = param.args.getOrNull(0) as? Int ?: return
                        if (!shouldTranslate(param.thisObject as? Resources, resId)) return
                        val original = param.result as? String ?: return
                        val translated = TranslateRepo.translate(original) ?: return
                        param.result = translated
                    }
                }
            )
        }.onFailure { XposedBridge.log(it) }
    }

    private fun shouldTranslate(resources: Resources?, resId: Int): Boolean {
        if (resources == null || resId == 0) return false
        return runCatching { resources.getResourcePackageName(resId) == TARGET_PKG }
            .getOrDefault(false)
    }
}
