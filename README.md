# HookWxAds
作者声明: 该插件仅供测试使用,请勿用于商业或其它用途,产生一切后果与作者无关
#### 1.使用说明
 1. 该应用为Xposed插件只适用于Android5.0以上,5.0以下需要自适配ClassLoader及WebView相关api
 
 2. 由于插件是直接替换下载的js脚本,因此使用前需要清除之前的WebView缓存让其重新触发下载脚本
 
 3. 微信朋友圈广告是禁止让广告写入数据库,由于之前的缓存可能会导致一直显示,可以手动修改 /data/data/com.tencent.mm/MicroMsg/你的账号ID/SnsMicroMsg.db 数据库,删除adsnsinfo表中信息