# TermiusCN v1.1.2

为设置页新增 androidx.preference 生命周期探测；当 Preference 页面活跃时，自动跳过 Resources.getString 翻译，降低闪退概率。
