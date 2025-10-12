plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.moex.android.library.compose)
    alias(libs.plugins.moex.android.library)
}

android {
    namespace = "mikle.sam.moex.details"
}

dependencies {
    implementation(project(":network"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.retrofit.converter.gson)
}