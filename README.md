# TermiusCN

一个面向 **Termius 7.5.2** 的最小 LSPosed/Xposed 文本替换模块工程。

## 当前功能

- 仅作用于 `com.server.auditor.ssh.client`
- 运行时替换一批常见界面英文
- 尽量避免误改终端会话内容
- 自带 GitHub Actions，可直接构建 `debug APK`

## 说明

这个工程采用 **legacy-compatible** 写法，方便直接构建 APK。
项目内的 `xposed-stubs` 只是编译期占位，不会打包进 APK。
实际运行时由设备上的 LSPosed/Xposed 提供真正的 API。

## GitHub Actions 构建

推送到 GitHub 后，进入 **Actions** 页面，运行 `Android CI`。
构建产物会出现在 workflow artifacts 里。

## 安装

1. 在 GitHub Actions 下载 `app-debug.apk`
2. 安装到手机
3. 在 LSPosed 中启用模块
4. 作用域只勾选 `Termius`
5. 强制停止 Termius 后重开

## 后续可扩展

- 增加更多翻译词条
- 增加模板替换（例如带参数的提示）
- 增加日志开关
- 改成 libxposed 101 现代 API 版本
