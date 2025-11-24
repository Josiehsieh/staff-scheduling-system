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
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

# ==================== Kotlin ====================
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}

# ==================== Kotlinx Coroutines ====================
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# ==================== Kotlinx Serialization ====================
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ==================== Jetpack Compose ====================
-keep class androidx.compose.** { *; }
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-dontwarn androidx.compose.**

# ==================== Room Database ====================
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# ==================== Hilt ====================
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel
-keepclasseswithmembers class * {
    @dagger.hilt.* <methods>;
}
-keepclasseswithmembers class * {
    @javax.inject.* <fields>;
}

# ==================== Google APIs ====================
# Google API Client
-keep class com.google.api.client.** { *; }
-dontwarn com.google.api.client.**

# Google Sheets API
-keep class com.google.api.services.sheets.** { *; }
-dontwarn com.google.api.services.sheets.**

# Google Calendar API
-keep class com.google.api.services.calendar.** { *; }
-dontwarn com.google.api.services.calendar.**

# Google OAuth
-keep class com.google.auth.** { *; }
-dontwarn com.google.auth.**

# Google Play Services
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# ==================== Gson ====================
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# ==================== OkHttp ====================
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keepclassmembers class * {
  @retrofit2.http.* <methods>;
}

# ==================== Firebase ====================
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# ==================== Data Models ====================
# Keep all data models
-keep class com.medical.calendar.data.model.** { *; }

# Keep all entities
-keep @androidx.room.Entity class * {
    <fields>;
    <init>(...);
}

# ==================== Parcelable ====================
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

# ==================== Serializable ====================
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ==================== Enum ====================
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ==================== WorkManager ====================
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.InputMerger
-keep class androidx.work.impl.background.systemalarm.RescheduleReceiver

# ==================== Lunar Calendar ====================
-keep class com.heqiao.lunarapi.** { *; }
-dontwarn com.heqiao.lunarapi.**

# ==================== JSoup ====================
-keep class org.jsoup.** { *; }
-dontwarn org.jsoup.**

# ==================== MPAndroidChart ====================
-keep class com.github.mikephil.charting.** { *; }

# ==================== Coil ====================
-keep class coil.** { *; }
-dontwarn coil.**

# ==================== Custom Application Classes ====================
-keep class com.medical.calendar.** { *; }

# ==================== Remove Logging ====================
# Uncomment these lines in production to remove all logging
#-assumenosideeffects class android.util.Log {
#    public static *** d(...);
#    public static *** v(...);
#    public static *** i(...);
#    public static *** w(...);
#    public static *** e(...);
#}



