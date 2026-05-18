# 项目规则

## 工具配置目录

每次对话开始时，必须先读取全局工具配置文件：

**配置文件路径：** `C:\Users\Administrator\.trae-cn\tool-paths.json`

**配置的工具：**

- Node.js: `E:\nodejs\node.exe`
- Git: `D:\Git\cmd\git.exe`
- npm: `E:\nodejs\npm`
- Maven: `D:\apache-maven-3.9.15\bin\mvn.cmd`

## 执行流程

1. 对话开始 → 立即读取 `tool-paths.json`
2. 根据需要使用对应的工具路径
3. 执行相关命令时优先使用配置的工具路径
