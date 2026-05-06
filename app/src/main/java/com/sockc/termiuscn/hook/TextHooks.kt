package com.sockc.termiuscn.hook

import android.widget.TextView
import com.sockc.termiuscn.data.TranslateRepo
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object TextHooks {
    fun install() {
        XposedHelpers.findAndHookMethod(
            TextView::class.java,
            "setText",
            CharSequence::class.java,
            TextView.BufferType::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val original = param.args.getOrNull(0) as? CharSequence ?: return
                    val translated = TranslateRepo.translate(original.toString()) ?: return
                    param.args[0] = translated
                }
            }
        )

        XposedHelpers.findAndHookMethod(
            TextView::class.java,
            "setText",
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
