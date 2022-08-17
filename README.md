# SMTP
[参考网址](https://www.liaoxuefeng.com/wiki/1252599548343744/1319099923693601)

+ POP3协议——接收邮件
+ SMTP协议——发送邮件



---
**报错**
1. `535 Login Fail. Please enter your authorization code to login.`
解决：使用授权码作为`password`而不是邮箱密码。
---




# Exchange
必须使用`outlook`邮箱发送邮件


---
# 文件读写
## txt
功能：Java读取txt文件的内容
工具：`BufferedReader`

步骤：
1：先获得文件句柄

2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取

3：读取到输入流后，需要读取生成字节流

4：一行一行的输出：`readline()`

## pdf
工具：`PDFbox`

## Excel
工具：`poi`

## CSV
工具：`CsvReader`

## DBF
工具：`javadbf`



---
SpringBoot  

问题：
1. 是否需要写入
4. 邮箱需求 
5. Exchange需要EWS服务地址

+ 邮件
  + exchange  or java mail smtp
  + 发送邮件
  + 接收邮件
  + 实现多发件用户
+ 文件读取
  + 类型
    + txt
    + pdf
    + excel
    + csv
    + dbf
  + 导入数据库
  + 工具类
