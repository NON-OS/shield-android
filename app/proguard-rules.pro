# The generated bindings reach the core through JNA, which resolves method
# names at runtime. Shrinking those names breaks the boundary.
-keep class systems.nonos.shield.core.** { *; }
-keep class com.sun.jna.** { *; }
-keep class * implements com.sun.jna.** { *; }

# The same library ships a desktop helper that reaches for AWT, which no
# Android device has. Keeping the library whole keeps that class too, so R8 is
# told the references it can see there are ones nothing on a phone follows.
-dontwarn java.awt.**

# No logging survives a release build. Stripped rather than filtered, so a
# device with a log reader attached has nothing to read.
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
    public static *** wtf(...);
    public static *** println(...);
}

# JNA writes to java.util.logging, which Android routes to the same log.
-assumenosideeffects class java.util.logging.Logger {
    public void log(...);
    public void logp(...);
    public void severe(...);
    public void warning(...);
    public void info(...);
    public void config(...);
    public void fine(...);
    public void finer(...);
    public void finest(...);
}
