plugins {
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinAndroidDaggerHilt) apply false
    alias(libs.plugins.devtools.ksp) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.icerock) apply false
}

buildscript {
    dependencies {
        classpath(libs.resources.generator)
    }
}
