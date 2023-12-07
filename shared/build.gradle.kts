plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.compose)
    alias(libs.plugins.icerock)
//    id(libs.plugins.icerock.get().pluginId)
//    alias(libs.plugins.androidApplication)
//    alias(libs.plugins.kotlinAndroid)

}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java).all {
        binaries.withType(org.jetbrains.kotlin.gradle.plugin.mpp.Framework::class.java).all {
            export("dev.icerock.moko:mvvm-core:0.16.1")
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
            export("dev.icerock.moko:resources:0.22.0")
            export("dev.icerock.moko:graphics:0.9.0")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(libs.resources)
            implementation(libs.resources.compose)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization)
        }

        androidMain.dependencies {
            androidMain.get().dependsOn(commonMain.get())
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.android)
        }

        iosMain.dependencies {
            iosMain.get().dependsOn(commonMain.get())
            implementation(libs.ktor.client.darwin)
        }

        commonTest.dependencies {
            commonTest.get().dependsOn(commonMain.get())
            implementation(libs.kotlin.test)
        }

    }
}

android {
    namespace = "com.shahroze.shared"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

//    sourceSets["main"].res.srcDirs("src/androidMain/res")
//    sourceSets["main"].resources.srcDirs("src/commonMain/resources")
}

val osName = System.getProperty("os.name")
val targetOs = when {
    osName == "Mac OS X" -> "macos"
    osName.startsWith("Win") -> "windows"
    osName.startsWith("Linux") -> "linux"
    else -> error("Unsupported OS: $osName")
}

val osArch = System.getProperty("os.arch")
var targetArch = when (osArch) {
    "x86_64", "amd64" -> "x64"
    "aarch64" -> "arm64"
    else -> error("Unsupported arch: $osArch")
}

val version = "0.7.9" // or any more recent version
val target = "${targetOs}-${targetArch}"

dependencies {
    implementation(libs.androidx.core.ktx)
    commonMainApi(libs.mvvm.core)
    commonMainApi(libs.mvvm.compose)
    commonMainApi(libs.mvvm.flow)
    commonMainApi(libs.mvvm.flow.compose)
    implementation("org.jetbrains.skiko:skiko-awt-runtime-$target:$version")

}

multiplatformResources{
    multiplatformResourcesPackage = "com.shahroze.shared"
    disableStaticFrameworkWarning = true

}