# CatSeedLogin 猫种子登陆
> 插件在Spigot API 1.13.2环境下开发的，
由于现在很多登录插件功能配置非常多，配置起来麻烦并且有很多用不到的功能。
crazylogin在高版本有各种匪夷所思的bug（总之我是被crazylogin从1.13.2的版本劝退自己开始造起了登陆插件）
authme配置文件对一些经验不足的服主配置起来极其麻烦，甚至有人从入门到弃坑
有人测试1.7.10 和 1.11版本的服务器可以用 理论上应该支持1.7 ~ 1.15 一般都是低版本向上兼容。
## 基础功能:
*  注册 登录 修改密码 管理员设置密码 销户
*  防止英文id大小写登录bug
*  防止玩家登录之后,被别人顶下线
*  下线之后指定时间内不能重新进入服务器（防止某些bug）
*  没有登录之前禁止移动,交互,攻击,发言,使用指令,传送,点击背包物品,丢弃物品,拾取物品
*  禁止同ip的帐号同时在线数量超过可配置数量
*  登录之前在配置文件指定的世界出生点,登录之后自动返回下线地点（可配置取消）
*  储存默认使用的是SQLite（也支持Mysql，需要配置文件sql.yml中配置打开）
*  密码加密储存,Crypt默认加密方式
*  进入游戏时游戏名的限制（游戏名长度在指定范围内才能进入，游戏名是否只能包含英文字母、数字、下划线）
*  绑定邮箱，邮箱重置密码功能
## 下载
* 原版：https://www.mcbbs.net/thread-847859-1-1.html
* 改版：https://github.com/MCUmbrella/CatSeedLogin/releases
## 使用方式
* 下载到的文件放入plugins文件夹重启服务器
## 指令
### 登录
* /login 密码
* /l 密码
### 注册密码
* /register 密码 重复密码
* /reg 密码 重复密码
### 修改密码
* /changepassword 旧密码 新密码 重复新密码
* /changepw 旧密码 新密码 重复新密码
### 管理员强制设置玩家密码
* /adminsetpassword 游戏名 密码
* /adminsetpw 游戏名 密码
### 绑定邮箱
* /bindemail set 邮箱
* /bdmail set 邮箱
### 用邮箱收到的验证码完成绑定
* /bindemail verify 验证码
* /bdmail verify 验证码
### 忘记密码，请求服务器给自己绑定的邮箱发送重置密码的验证码
* /resetpassword forget
* /repw forget
### 用邮箱收到的验证码重置密码
* /bindemail re 验证码 新密码
* /bdmail re 验证码 新密码
### 销户（清除注册信息）
* /unregister 当前密码 重复密码
* /unreg 当前密码 重复密码
### 管理员重载配置文件
* /catseedlogin reload
* /cslogin reload
## 权限
* catseedlogin.command.adminsetpassword 管理员强制设置玩家密码指令使用权限/adminsetpassword
* catseedlogin.command.catseedlogin 管理员重载配置文件指令使用权限/catseedlogin reload
## 配置文件
### 原版CatSeed配置文件移步https://github.com/CatSeed/CatSeedLogin
### config.yml
用于存储玩家数据，不建议手动更改<br/>
### settings.yml
### 本分支的settings.yml与CatSeed原版的不兼容，请删除旧settings.yml后重新生成
> \#配置文件版本，正常情况不要更改<br/>
cfgVersion: 1<br/>
\#相同ip限制<br/>
maxLogPerIP: 1<br/>
\#登录时在哪个世界的出生点<br/>
spawnWorld: "world"<br/>
\#是否禁止中文及其它非英文字母、数字、下划线组成的ID<br/>
specialSymbolsIDLimit: true<br/>
\#游戏ID最小长度<br/>
IDMinLength: 3<br/>
\#游戏ID最大长度<br/>
IDMaxLength: 16<br/>
\#登陆之前不受到伤害<br/>
noDamageBeforeLogin: true<br/>
\#已登录玩家离开服务器重新进入的间隔限制 单位：tick（如果设置3秒则是60）<br/>
loginInterval: 60<br/>
\#登陆之后是否返回退出地点<br/>
backAfterLogin: true<br/>
\#语言文件，内置lang_zhCN.yml（简体中文）和lang_enUS.yml（美国英语）<br/>
languageFile: "lang_zhCN.yml"<br/>
\#调试选项，在控制台输出更多信息<br/>
debug: false<br/>
\#强制使用高强度密码<br/>
forceStrongPasswd: true<br/>
### sql.yml
如果不使用mysql数据库储存，就请无视此配置<br/>
> MySQL:<br/>
\#是否开启数据库功能（false = 不开启）<br/>
  Enable: false<br/>
  Host: 127.0.0.1<br/>
  Port: '3306'<br/>
  Database: databaseName<br/>
  User: root<br/>
  Password: root<br/>
### emailVerify.yml 
如果不使用邮箱一系列功能，就请无视此配置<br/>
> \#是否开启邮箱系列的功能（false = 不开启）<br/>
Enable: false<br/>
EmailAccount: "763737569@qq.com"<br/>
EmailPassword: "123456"<br/>
EmailSmtpHost: "smtp.qq.com"<br/>
EmailSmtpPort: "465"<br/>
SSLAuthVerify: true<br/>
\#发件人的名字<br/>
 FromPersonal: "xxx服务器"<br/>
## 开发者部分
### 事件
CatSeedPlayerLoginEvent
<br/>
CatSeedPlayerRegisterEvent
### API
CatSeedLoginAPI
### 登录玩家管理
## 联系
[CatSeedLogin官方QQ交流群839815243](http://shang.qq.com/wpa/qunwpa?idkey=91199801a9406f659c7add6fb87b03ca071b199b36687c62a3ac51bec2f258a3)
[MCUmbrella的Discord交流群](https://discord.gg/45W6rVr)
### CatSeed:
- QQ: 763737569
- Email: 763737569@qq.com
- Twitter: @ClassCatSeed
### MCUmbrella
- QQ: 1660030319
- Email: mcumbrella@qq.com
- Twitter: @MCUmbrella
