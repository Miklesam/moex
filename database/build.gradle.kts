plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.moex.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "mikle.sam.moex.database"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.jroom.runtime)
    implementation(libs.jroom.ktx)
    ksp(libs.jroom.compiler)
}





