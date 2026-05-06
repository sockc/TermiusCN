package com.sockc.termiuscn.data

object TranslateRepo {
    private val exact = linkedMapOf(
        "Hosts" to "主机",
        "Host" to "主机",
        "Keychain" to "密钥链",
        "Forwarding" to "转发",
        "Snippets" to "代码片段",
        "Known hosts" to "已知主机",
        "Logs" to "日志",
        "Settings" to "设置",
        "Account" to "账户",
        "Account settings" to "账户设置",
        "Security, Cloud, and Subscription" to "安全、云与订阅",
        "Security" to "安全",
        "Cloud" to "云",
        "Subscription" to "订阅",
        "Team" to "团队",
        "Personal" to "个人",
        "Join our Discord" to "加入我们的 Discord",
        "Share your feedback on the new Termius Android app and find the latest news in our Discord community." to "在 Discord 社区分享你对新版 Termius Android 应用的反馈，并获取最新消息。",
        "Terminal" to "终端",
        "Font & colours" to "字体与颜色",
        "Font & colors" to "字体与颜色",
        "Terminal type" to "终端类型",
        "Hotkeys settings" to "快捷键设置",
        "Customize keyboard" to "自定义键盘",
        "Terminal back buffer size" to "终端回滚缓冲区大小",
        "Bell settings" to "提示铃设置",
        "Enable autocomplete" to "启用自动补全",
        "Use Ctrl" to "使用 Ctrl",
        "Pinch to zoom" to "双指缩放",
        "Cursor speed" to "光标速度",
        "Normal" to "正常",
        "Prevent sleeping" to "防止休眠",
        "Keep display active when in terminal" to "在终端中保持屏幕常亮",
        "Experimental Keyboard Support" to "实验性键盘支持",
        "Voice input and CJK layout support" to "支持语音输入和中日韩键盘布局",
        "Session" to "会话",
        "Keep alive interval" to "保活间隔",
        "60 seconds" to "60 秒",
        "Keep alive max count" to "最大保活次数",
        "Monokai/8/Source Code Pro Medium" to "Monokai/8/Source Code Pro Medium",
        "xterm-256color" to "xterm-256color",
        "1000 lines" to "1000 行",
        "Vibration and Sound" to "振动和声音",
        "Groups" to "分组",
        "Group" to "分组",
        "Snippet" to "代码片段",
        "Search" to "搜索",
        "Edit" to "编辑",
        "Delete" to "删除",
        "Add" to "添加",
        "Create" to "创建",
        "Save" to "保存",
        "Saved" to "已保存",
        "Cancel" to "取消",
        "Close" to "关闭",
        "Retry" to "重试",
        "Rename" to "重命名",
        "Duplicate" to "复制",
        "Copy" to "复制",
        "Paste" to "粘贴",
        "Done" to "完成",
        "Back" to "返回",
        "Next" to "下一步",
        "Connect" to "连接",
        "Connected" to "已连接",
        "Disconnect" to "断开连接",
        "Disconnected" to "已断开",
        "Connection" to "连接",
        "Password" to "密码",
        "Username" to "用户名",
        "Hostname" to "主机名",
        "Port" to "端口",
        "Port Forwarding" to "端口转发",
        "Local" to "本地",
        "Remote" to "远程",
        "Dynamic" to "动态",
        "SFTP" to "文件传输",
        "Clipboard" to "剪贴板",
        "Import" to "导入",
        "Export" to "导出",
        "Appearance" to "外观",
        "Notifications" to "通知",
        "Notification" to "通知",
        "Enable notifications" to "启用通知",
        "Customise or disable the displaying of active connections" to "自定义或禁用活动连接通知显示",
        "Customize or disable the displaying of active connections" to "自定义或禁用活动连接通知显示",
        "Security & Privacy" to "安全与隐私",
        "Biometric unlock" to "生物识别解锁",
        "PIN code and Pattern lock" to "PIN 码和图案锁",
        "Disabled" to "已禁用",
        "Logs storage" to "日志存储",
        "Send diagnostic data" to "发送诊断数据",
        "Help to make the app better" to "帮助改进此应用",
        "Screenshots & screen recording" to "截图与录屏",
        "Blocked. You will not be able to take screenshots of screens that contain sensitive data." to "已阻止。你将无法对包含敏感数据的界面进行截图或录屏。",
        "Volume keys" to "音量键",
        "Volume up" to "音量加",
        "Volume down" to "音量减",
        "Post-Quantum Key Exchange" to "后量子密钥交换",
        "Turn off if you're experiencing issues with legacy devices" to "如果旧设备出现兼容性问题，请关闭此选项",
        "Detect OS" to "检测操作系统",
        "Import shell history" to "导入 Shell 历史记录",
        "Required for autocomplete and accessing command history" to "自动补全和访问命令历史记录需要此功能",
        "Home" to "主页",
        "New host" to "新建主机",
        "Alias" to "别名",
        "Hostname or IP Address" to "主机名或 IP 地址",
        "Tags" to "标签",
        "Delete sends Ctrl-H" to "Delete 键发送 Ctrl-H",
        "Learn more..." to "了解更多…",
        "Credentials" to "凭据",
        "SSH ID, Key, Certificate, FIDO2" to "SSH 身份、密钥、证书、FIDO2",
        "Agent Forwarding" to "代理转发",
        "Host Chaining" to "主机链",
        "Proxy" to "代理",
        "+ Add Env Variable" to "+ 添加环境变量",
        "Unknown error" to "未知错误",
        "Error" to "错误",
        "Warning" to "警告",
        "Info" to "信息",
        "OK" to "确定",
        "Yes" to "是",
        "No" to "否",
        "Never" to "从不",
        "Always" to "始终",
        "Required" to "必填",
        "Optional" to "可选"
    )

    private val templates: List<Pair<Regex, (MatchResult) -> String>> = listOf(
        Regex("^Connected to (.+)$") to { m -> "已连接到 ${m.groupValues[1]}" },
        Regex("^Connecting to (.+)$") to { m -> "正在连接到 ${m.groupValues[1]}" },
        Regex("^Disconnected from (.+)$") to { m -> "已从 ${m.groupValues[1]} 断开" },
        Regex("^Delete host \"(.+)\"\\?$") to { m -> "删除主机“${m.groupValues[1]}”？" },
        Regex("^Delete group \"(.+)\"\\?$") to { m -> "删除分组“${m.groupValues[1]}”？" },
        Regex("^(\\d+) Hosts$") to { m -> "${m.groupValues[1]} 台主机" },
        Regex("^(\\d+) Host$") to { m -> "${m.groupValues[1]} 台主机" }
    )

    fun translate(raw: String): String? {
        val src = raw.trim()
        if (src.isEmpty()) return null

        if (src.length > 220) return null
        if ('\n' in src || '\r' in src || '\t' in src) return null
        if (looksLikeCommandOrPath(src)) return null

        exact[src]?.let { return it }

        val normalized = normalize(src)
        if (normalized != src) {
            exact[normalized]?.let { return it }
        }

        templates.firstNotNullOfOrNull { (regex, block) ->
            regex.matchEntire(src)?.let(block)
                ?: regex.matchEntire(normalized)?.let(block)
        }?.let { return it }

        return null
    }

    private fun normalize(src: String): String {
        return src
            .replace('\u00A0', ' ')
            .replace('’', '\'')
            .replace('“', '"')
            .replace('”', '"')
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    private fun looksLikeCommandOrPath(src: String): Boolean {
        if (src.contains("/")) return true
        if (src.contains("$ ")) return true
        if (src.startsWith("~")) return true
        if (src.contains("@") && src.contains(":")) return true
        return false
    }
}
