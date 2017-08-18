# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\adt-bundle-windows-x86_64_20140101\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


# SMSSDK mob
-dontwarn com.mob.**
-keep class com.mob.**{*;}
-keep class com.mob.**{*;}
-keep class cn.sharesdk.**{*;}
-kepp class cn.smssdk.**{*;}


#融云 SDK 支持小米和 GCM 推送，SDK 内部帮用户做了部分集成， 所以在您没有集成这几个第三方 jar 包时， 会有一些告警，混淆时加入下面语句即可：
-dontwarn io.rong.push.**
 -dontnote com.xiaomi.**
-dontnote com.google.android.gms.gcm.**
 -dontnote io.rong.**
#另外，你需要 keep 自定义的 BroadcastReceiver。自定义的 BroadcastReceiver 继承PushMessageReceiver，使用下面的代码是不行的。
-keep public class * extends android.content.BroadcastReceiver
#你需要使用下面的代码 keep 自定义的 BroadcastReceiver。
-keep class clocklife.shenle.com.base.message.RonYunNotificationReceiver {*;}


#讯飞统计
-keep class com.iflytek.sunflower.**{*;}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule