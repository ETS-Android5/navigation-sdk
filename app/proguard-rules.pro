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
#-dontshrink
-keepattributes Signature
-keepattributes Annotation
-keep class okhttp3.** { *; }
ç
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# Retrofit
-keep class retrofit2.** { *; }
-keep class com.google.appengine.** { *; }

-keep class com.bhawak.osmnavigation.navigation.model.** { *; }
-keepclasseswithmembers class com.bhawak.osmnavigation.navigation.model.** {
   public static *** parseparse(...);
   private static *** parseparse(...);
}


