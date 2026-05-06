package de.robv.android.xposed;

import java.lang.reflect.Member;
import java.util.Collections;
import java.util.Set;

public final class XposedBridge {
    private XposedBridge() {
    }

    public static void log(String text) {
    }

    public static void log(Throwable throwable) {
    }

    public static XC_MethodHook.Unhook hookMethod(Member hookMethod, XC_MethodHook callback) {
        return null;
    }

    public static Set<XC_MethodHook.Unhook> hookAllMethods(Class<?> clazz, String methodName, XC_MethodHook callback) {
        return Collections.emptySet();
    }

    public static Set<XC_MethodHook.Unhook> hookAllConstructors(Class<?> clazz, XC_MethodHook callback) {
        return Collections.emptySet();
    }
}
