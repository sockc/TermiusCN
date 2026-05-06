package com.sockc.termiuscn.hook

object PreferenceState {
    @Volatile
    private var preferenceScreenActive: Boolean = false

    fun setActive(active: Boolean) {
        preferenceScreenActive = active
    }

    fun isActive(): Boolean = preferenceScreenActive
}
