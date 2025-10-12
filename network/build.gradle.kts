plugins {
    alias(libs.plugins.moex.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "mikle.sam.moex.network"
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
}