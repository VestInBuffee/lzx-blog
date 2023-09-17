# lzx-blog
## 1.basic description
A blog project based on sg-blog project by 三更草堂 in 哔哩哔哩 app. You can visit it on https://www.bilibili.com/video/BV1hq4y1F7zk<br/><br/>
This is a back-end（后端） of the whole app, containing client-side（前台） and admin-side（后台）.<br/>

## 2.how to run this app
You need to add 2 ymls to `lzx-admin` module and `lzx-blog` module in their "resources" package separately. Then you can run them separately.
### 2.1 add "application.yml" to `lzx-admin` module
Below is the content. You need to finish this configuration before running:
```
server:
  port: 8989
spring:
  datasource:
    url: 
    username: 
    password: 
    driver-class-name: 
  servlet:
    multipart:
      max-file-size: 2MB
      max-request-size: 5MB
  redis:
    host: 
    port: 
mybatis-plus:
  configuration:
    # 日志
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: delFlag
      logic-delete-value: 1
      logic-not-delete-value: 0
      id-type: auto
oss:
  accessKey: 
  secretKey: 
  bucket: 
```
### 2.2 add "application.yml" to `lzx-blog` module
Below is the content. You need to finish this configuration before running:
```
server:
  port: 7777
spring:
  datasource:
    url: 
    username: 
    password: 
    driver-class-name: 
  servlet:
    multipart:
      max-file-size: 2MB
      max-request-size: 5MB
  redis:
    host: 
    port: 
mybatis-plus:
  configuration:
    # 日志
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: delFlag
      logic-delete-value: 1
      logic-not-delete-value: 0
      id-type: auto
oss:
  accessKey: 
  secretKey: 
  bucket: 
```
