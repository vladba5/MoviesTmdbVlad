package tmdb.buildSrc.plugins

import tmdb.buildSrc.*
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class UiLibraryPlugin : Plugin<Project> {

    private val Project.android: BaseExtension
        get() = extensions.findByName("android") as? BaseExtension
            ?: error("Not an Android module: $name")

    override fun apply(target: Project) {
        with(target) {
            applyPlugins()
            androidConfig()
            productFlavorsConfig()
            dependenciesConfig()
        }
    }

    private fun Project.applyPlugins() {
        plugins.run {
            apply("com.android.library")
            apply("kotlin-android")
            apply("kotlin-parcelize")
            apply("kotlin-kapt")
            apply("dagger.hilt.android.plugin")
        }
    }

    private fun Project.androidConfig() {
        android.run {
            compileSdkVersion(AndroidConfig.compileSdk)
            defaultConfig {
                minSdk = AndroidConfig.minSdk
                targetSdk = AndroidConfig.targetSdk
                versionCode = AndroidConfig.versionCode
                versionName = AndroidConfig.versionName

                testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
            }

            compileOptions {
                sourceCompatibility = AndroidConfig.sourceCompatibility
                targetCompatibility = AndroidConfig.targetCompatibility
            }

//            composeOptions{
//                buildFeatures.compose = true
//                composeOptions.kotlinCompilerExtensionVersion = Libs.Compose.kotlinCompilerExtensionVersion
//            }

            viewBinding.isEnabled = true
            composeOptions.kotlinCompilerExtensionVersion = Libs.Compose.kotlinCompilerExtensionVersion
            buildFeatures.compose = true

            packagingOptions {
                resources {
                    excludes += "META-INF/main.kotlin_module"
                }
            }
        }
    }

    private fun Project.dependenciesConfig() {
        dependencies {
            "implementation"(Libs.AndroidX.coreKtx)
            "implementation"(Libs.Google.material)

            "implementation"(Libs.AndroidX.Lifecycle.viewmodel)
            "implementation"(Libs.AndroidX.Lifecycle.runtime)
            "implementation"(Libs.AndroidX.Paging.runtime)
            "implementation"(Libs.AndroidX.SwipeRefreshLayout.swiperefreshlayout)

            "implementation"(Libs.Hilt.android)
            "kapt"(Libs.Hilt.compiler)

            "implementation"(Libs.Coroutines.android)
            "implementation"(Libs.Coroutines.core)

            "implementation"(Libs.AndroidX.Navigation.fragment)
            "implementation"(Libs.AndroidX.Navigation.uiKtx)

            //"implementation"(Libs.Pallete.pallete)
            // Compose
            "implementation"(Libs.Compose.layout)
            "implementation"(Libs.Compose.material)
            "implementation"(Libs.Compose.Material3.material3)
            "implementation"(Libs.Compose.activityCompose)
            "implementation"(Libs.Compose.viewModelCompose)
            "implementation"(Libs.Compose.materialIconsExtended)
            "implementation"(Libs.Compose.tooling)
            "implementation"(Libs.Compose.uiUtil)
            "implementation"(Libs.Compose.runtime)
            "implementation"(Libs.Compose.runtimeLivedata)
            "implementation"(Libs.Compose.viewBinding)
            "implementation"(Libs.Compose.themeAdapter)
            "implementation"(Libs.Compose.accompanistTheme)
            "implementation"(Libs.Compose.preview)
            "implementation"(Libs.Compose.paging)
            "implementation"(Libs.Compose.coil)

            "debugImplementation"(Libs.Compose.uiTestManifest)
            "androidTestImplementation"(Libs.Compose.test)
            "androidTestImplementation"(Libs.Compose.uiTest)
            "androidTestImplementation"(Libs.Compose.manifest)
        }
    }
}