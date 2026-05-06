package com.sockc.termiuscn.hook

import android.content.Context
import android.widget.Toast
import com.sockc.termiuscn.data.TranslateRepo
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object ToastHooks {
    fun install() {
        XposedHelpers.findAndHookMethod(
            Toast::class.java,
            "makeText",
            Context::class.java,
            CharSequence::class.java,
            Int::class.javaPrimitiveType,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val original = param.args.getOrNull(1) as? CharSequence ?: return
                    val translated = TranslateRepo.translate(original.toString()) ?: return
                    param.args[1] = translated
                }
            }
        )
    }
}
