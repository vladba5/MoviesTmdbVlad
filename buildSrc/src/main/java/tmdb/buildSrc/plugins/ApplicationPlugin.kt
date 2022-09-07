package tmdb.buildSrc.plugins

import tmdb.buildSrc.*
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class ApplicationPlugin : Plugin<Project> {

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
            apply("com.android.application")
            apply("kotlin-android")
            apply("kotlin-parcelize")
            apply("kotlin-kapt")
            apply("dagger.hilt.android.plugin")
            apply(Libs.FireBase.googleServices)
        }
    }

    private fun Project.androidConfig() {
        android.run {
            compileSdkVersion(AndroidConfig.compileSdk)
            defaultConfig {
                applicationId = AndroidConfig.applicationId
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

            viewBinding.isEnabled = true

            packagingOptions {
                resources {
                    excludes += "META-INF/main.kotlin_module"
                }
            }
        }
    }

    private fun Project.dependenciesConfig() {
        dependencies {
            "implementation"(Libs.Kotlin.stdlib)
            "implementation"(Libs.Coroutines.android)
            "implementation"(Libs.Coroutines.core)

            "implementation"(Libs.AndroidX.coreKtx)
            "implementation"(Libs.AndroidX.Fragment.fragmentKtx)

            "implementation"(Libs.AndroidX.Lifecycle.runtime)
            "implementation"(Libs.AndroidX.Lifecycle.process)
            "implementation"(Libs.AndroidX.Lifecycle.viewmodel)


//            "implementation"(Libs.Compose.viewModelCompose)
//            "implementation"(Libs.Compose.activityCompose)
//            "implementation"(Libs.Compose.foundation)
//            "implementation"(Libs.Compose.layout)
//            "implementation"(Libs.Compose.material)
//            "implementation"(Libs.Compose.materialIconsExtended)
//            "implementation"(Libs.Compose.runtime)
//            "implementation"(Libs.Compose.runtimeLivedata)
//            "implementation"(Libs.Compose.tooling)
//            "implementation"(Libs.Compose.runtimeLivedata)
//            "implementation"(Libs.Compose.tooling)
//            "implementation"(Libs.Compose.uiUtil)
//            "implementation"(Libs.Compose.viewBinding)
//            "implementation"(Libs.Compose.manifest)
//            "implementation"(Libs.Compose.preview)
//            "implementation"(Libs.Compose.themeAdapter)
//            "implementation"(Libs.Compose.accompanistTheme)
////            "implementation"(Libs.Compose.paging)
////            "implementation"(Libs.Compose.pagingRuntime)
//            "implementation"(Libs.Compose.Material3.material3)
//
//            "debugImplementation"(Libs.Compose.test)
//            "debugImplementation"(Libs.Compose.uiTest)
//            "debugImplementation"(Libs.Compose.uiTestManifest)



            "kapt"(Libs.AndroidX.Lifecycle.compiler)

            "implementation"(Libs.AndroidX.Navigation.fragment)
            "implementation"(Libs.AndroidX.Navigation.uiKtx)

            "implementation"(Libs.Google.material)
            "implementation"(Libs.Google.gson)

            "implementation"(Libs.Hilt.android)
            "kapt"(Libs.Hilt.compiler)

            "implementation"(Libs.Retrofit.retrofit)
            "implementation"(Libs.Retrofit.gsonConverter)

            "implementation"(Libs.OkHttp.logging)
            "implementation"(Libs.OkHttp.okhttp)
        }
    }
}