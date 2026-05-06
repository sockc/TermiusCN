# TermiusCN

Termius 7.5.2 的 LSPosed 汉化模块。

## v0.2 修正
- 增加 `Resources.getText/getString` 运行时翻译 Hook
- 保留 `TextView.setText` 兜底
- 扩展了 Settings 页常见词条

## 使用
1. 在 LSPosed 中启用模块
2. 作用域只勾选 `com.server.auditor.ssh.client`
3. 强行停止 Termius 后重新打开
4. 仍无效果时查看 LSPosed 日志中是否有 `TermiusCN: hooks installed`
