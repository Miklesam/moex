plugins {
    alias(libs.plugins.moex.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "mikle.sam.moex.network"
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}