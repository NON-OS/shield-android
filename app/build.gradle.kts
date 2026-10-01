import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

// Where a signing key is described, if the builder has one. It is ignored by git and holds a path
// and three passwords. Without it the build makes the unsigned package a verifier compares.
val signingFile = rootProject.file("keystore.properties")
val signingValues = Properties().apply {
    if (signingFile.exists()) {
        signingFile.inputStream().use { load(it) }
    }
}
val signingKeys = listOf("storeFile", "storePassword", "keyAlias", "keyPassword")
val signingDescribed = signingKeys.all { !signingValues.getProperty(it).isNullOrBlank() }

// The version is the release tag CI builds, never a constant here, since a constant goes stale.
// Other builds say so in their name. The code orders releases, 0.6.0 as 600, so each installs over
// the last.
val releaseTag: String? = providers.environmentVariable("GITHUB_REF_NAME").orNull
    ?.takeIf { Regex("v\\d+\\.\\d+\\.\\d+").matches(it) }
    ?.removePrefix("v")
val releaseName: String = releaseTag ?: "0.0.0-dev"
val releaseCode: Int = releaseTag
    ?.split(".")
    ?.map { it.toInt() }
    ?.let { (major, minor, patch) -> major * 10_000 + minor * 100 + patch }
    ?.coerceAtLeast(1)
    ?: 1

android {
    namespace = "systems.nonos.shield"
    compileSdk = 36

    defaultConfig {
        applicationId = "systems.nonos.shield"
        minSdk = 28
        targetSdk = 36
        versionCode = releaseCode
        versionName = releaseName

        // The core is built for these two only. 32-bit ARM is left out: every Goldilocks multiply
        // folds a 128-bit product, done in software on armeabi-v7a, and a proof would take minutes.
        // x86_64 is for the emulator.
        ndk {
            abiFilters += listOf("arm64-v8a", "x86_64")
        }
        // No instrumented runner: the failure sentences and value runs are unit tested off device.
    }

    signingConfigs {
        if (signingDescribed) {
            create("release") {
                storeFile = rootProject.file(signingValues.getProperty("storeFile"))
                storePassword = signingValues.getProperty("storePassword")
                keyAlias = signingValues.getProperty("keyAlias")
                keyPassword = signingValues.getProperty("keyPassword")
                // Scheme v3 only. Every device this app runs on is API 28 or above, so v1 and v2
                // have no reader.
                enableV1Signing = false
                enableV2Signing = false
                enableV3Signing = true
            }
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.findByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            ndk {
                debugSymbolLevel = "none"
            }
        }
        debug {
            applicationIdSuffix = ".debug"
        }

        // A build that exists to be filmed, and for nothing else. A resource turns FLAG_SECURE off,
        // and its own application id keeps it off the real app. It is never signed for release or
        // distributed.
        create("capture") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".capture"
            isMinifyEnabled = false
        }
    }

    // The prebuilt core for each ABI, from scripts/build-core.sh. See docs/03-reproduce.md.
    sourceSets["main"].jniLibs.srcDir("src/main/jniLibs")
    sourceSets["main"].kotlin.srcDir("src/main/generated")

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        // Warnings are build failures: a warning in a wallet is a finding nobody read.
        allWarningsAsErrors = true
    }

    buildFeatures {
        compose = true
        buildConfig = false
    }

    packaging {
        resources.excludes += setOf("/META-INF/{AL2.0,LGPL2.1}")
    }

    dependenciesInfo {
        // Play Store dependency metadata is encrypted, so a reproducible build cannot carry it.
        includeInApk = false
        includeInBundle = false
    }
}

detekt {
    config.setFrom(files("$rootDir/detekt.yml"))
    buildUponDefaultConfig = true
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.animation)
    implementation(libs.activity.compose)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.coroutines.android)
    implementation(libs.biometric)
    // The receive QR code, encoded on the device. Pure Java, no network.
    implementation(libs.zxing.core)
    implementation(libs.jna) { artifact { type = "aar" } }
    testImplementation(libs.junit)
}

// The licences of everything in the package, generated from what the build resolves. An unknown
// group fails the build, since a dependency with no stated licence is one nobody reviewed. The
// fonts are added by hand, the crates by scripts/build-core.sh.
val licenceOf = mapOf(
    "org.jetbrains.kotlin" to "Apache License 2.0",
    "org.jetbrains.kotlinx" to "Apache License 2.0",
    "org.jetbrains" to "Apache License 2.0",
    "androidx" to "Apache License 2.0",
    "net.java.dev.jna" to "Apache License 2.0 or LGPL 2.1",
    "com.google.guava" to "Apache License 2.0",
    "com.google.code.findbugs" to "BSD 3-Clause",
    "com.google.errorprone" to "Apache License 2.0",
    "com.google.j2objc" to "Apache License 2.0",
    "org.checkerframework" to "MIT",
    "org.intellij" to "Apache License 2.0",
    "com.google.zxing" to "Apache License 2.0",
)

val ownParts = listOf(
    "Geist : SIL Open Font License 1.1",
    "JetBrains Mono : SIL Open Font License 1.1",
)

// Written to the build directory, added as an asset source, and kept out of src/main/assets. A
// generated file in a source tree is read by tasks that never declare it, so task order would
// decide what ships.
val coreLicences = layout.projectDirectory.dir("src/main/generated/licences")

val writeLicences by tasks.registering {
    val dir = layout.buildDirectory.dir("generated/licences/assets")
    inputs.dir(coreLicences)
    outputs.dir(dir)
    doLast {
        val out = dir.get().file("licences.txt").asFile
        val crates = coreLicences.file("licences.txt").asFile
        val texts = coreLicences.file("licence-texts.txt").asFile
        if (!crates.isFile || !texts.isFile) {
            throw GradleException("the core's licences are missing: run scripts/build-core.sh")
        }
        val resolved = configurations.getByName("captureRuntimeClasspath")
            .incoming.resolutionResult.allComponents
            .mapNotNull { it.moduleVersion }
            // This project's own modules have no version and no licence to declare: they are the
            // thing being licensed, and they are named in ownParts above.
            .filter { it.group.isNotEmpty() && it.version != "unspecified" }
            .map { "${it.group}:${it.name}:${it.version}" }
            .distinct()
            .sorted()
        val unknown = resolved.filterNot { line ->
            licenceOf.keys.any { group -> line.startsWith("$group:") || line.startsWith("$group.") }
        }
        if (unknown.isNotEmpty()) {
            throw GradleException(
                "no licence is declared for: ${unknown.joinToString()}. " +
                    "Add it to licenceOf in app/build.gradle.kts and say what it is.",
            )
        }
        val lines = resolved.map { line ->
            val group = licenceOf.keys.first { line.startsWith("$it:") || line.startsWith("$it.") }
            "$line : ${licenceOf.getValue(group)}"
        }
        out.parentFile.mkdirs()
        val core = crates.readLines().filter { it.isNotBlank() }
        out.writeText((core + ownParts + lines).joinToString("\n", postfix = "\n"))
        texts.copyTo(dir.get().file("licence-texts.txt").asFile, overwrite = true)
    }
}

// Registered as an asset source with its producer, or the package can ship without the file.
android.sourceSets["main"].assets.srcDir(layout.buildDirectory.dir("generated/licences/assets"))

tasks.matching { it.name.startsWith("merge") && it.name.endsWith("Assets") }.configureEach {
    dependsOn(writeLicences)
}

// Every lint task, since lint reads the asset directory and so depends on whatever fills it.
tasks.matching { it.name.lowercase().contains("lint") }.configureEach {
    dependsOn(writeLicences)
}
