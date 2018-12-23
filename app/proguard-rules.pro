# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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

#忽略警告
-ignorewarnings
# 将.class信息中的类名重新定义为"Proguard"字符串
#-renamesourcefileattribute Proguard
# 并保留源文件名为"Proguard"字符串，而非原始的类名 并保留行号
-keepattributes SourceFile,LineNumberTable

#指定代码的压缩级别
-optimizationpasses 5

#包名不混合大小写
-dontusemixedcaseclassnames

 #不忽略非公共的库类
-dontskipnonpubliclibraryclasses

#优化/不优化输入的类文件
-dontoptimize

#预校验
-dontpreverify

#混淆时是否记录日志
-verbose

#混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#保护注解
-keepattributes *Annotation*

#保护反射的正常调用
-keepattributes Signature
-keepattributes EnclosingMethod

#指定不混淆所有的JNI方法
#-keepclasseswithmembernames class * {
#        native ;
#    }

#不混淆Activity中参数类型为View的所有方法
-keepclassmembers class * extends android.app.Activity {
        public void *(android.view.View);
    }

#不混淆Parcelable和它的子类，还有Creator成员变量
-keep class * implements android.os.Parcelable {
        public static final android.os.Parcelable$Creator *;
    }

#不混淆Serializable接口
-keepnames class * implements java.io.Serializable

#不混淆R类里及其所有内部static类中的所有static变量字段
#-keepclassmembers class **.R$* {
#        public static ;
#    }

#如果有用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }

#如果有用到WebView的JS调用接口，需加入如下规则。
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
        public *;
    }

#注解和反射
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Gson
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod

#不混淆哪些类
-keep class com.kent.newspaper.module.newsbrowse.model.abs.GetDataApi
-keep class com.kent.newspaper.module.newsbrowse.entity.**{*;}
