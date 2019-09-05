# 文件生成系统

## 调用须知
- 接口统一前缀：/generate-file
- 默认端口：8080

## 接口文档

### 生成PDF，并将文件上传至腾讯云
- 请求方式：POST
- URL：/generate/pdf

- 入参

参数名|类型|是否必填|描述|示例
:---:|:---:|:---:|---|:---:
type|String|是|模板类型|test
data|Object|是|模板填充数据|
targetName|String|否|生成文件的名字|文件名

- 接口返回

参数名|类型|示例
:---:|:---:|---
code|String|响应码
message|String|消息提示
data|String|生成的PDF的URL（腾讯云）

### 下载文件到私有存储
- 请求方式：POST
- URL：/inner-download

- 入参

参数名|类型|是否必填|描述|示例
:---:|:---:|:---:|---|:---:
fileUrls|List<String>|是|需要下载的文件的URL|
fileFormat|String|否|文件格式|.png

- 接口返回

参数名|类型|示例
:---:|:---:|---
code|String|响应码
message|String|消息提示
data|Map|下载到本地的文件的URL（key为文件原始路径，value为文件本地路径）


