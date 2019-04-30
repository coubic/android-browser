package dependencies

@Suppress("unused")
object Depends {

    /* ========================================
     * Gradle
     * ========================================*/

    object GradlePlugin {
        const val android = "com.android.tools.build:gradle:3.2.1"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val kotlinSerialization =
            "org.jetbrains.kotlin:kotlin-serialization:${Kotlin.version}"
        const val playServices = "com.google.gms:google-services:4.2.0"
        // Fragmentの引数をTypeSafeに渡せるようにする
        const val safeArgs =
            "android.arch.navigation:navigation-safe-args-gradle-plugin:${AndroidX.Navigation.version}"
        // クラッシュレポートなど
        const val fabric = "io.fabric.tools:gradle:1.+"
        // OSSライセンスページの自動生成
        const val ossLicenses = "com.google.android.gms:oss-licenses-plugin:0.9.3"
    }


    /* ========================================
     * Test
     * ========================================*/

    object Test {
        const val junit = "junit:junit:4.12"
        const val testRunner = "androidx.test:runner:1.1.0"
        // Android関連のクラスもUnitテストできるようにする
        const val robolectric = "org.robolectric:robolectric:4.1"
        // MockitoをKotlinらしく書く
        const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0"
        // Mockitoでfinalクラスもモックできるようにする
        const val mockitoInline = "org.mockito:mockito-inline:2.23.4"

        object Espresso {
            // UIのテスト
            private const val version = "3.1.0-alpha3"
            const val core = "androidx.test.espresso:espresso-core:$version"
            const val idlingResource =
                "com.android.support.test.espresso:espresso-idling-resource:$version"
        }
    }


    /* ========================================
     * Google
     * ========================================*/

    object AndroidX {
        const val appCompat = "androidx.appcompat:appcompat:1.0.0"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.0.0"
        const val cardView = "androidx.cardview:cardview:1.0.0"
        const val constraint = "androidx.constraintlayout:constraintlayout:1.1.2"
        const val design = "com.google.android.material:material:1.1.0-alpha01"
        const val coreKtx = "androidx.core:core-ktx:1.0.0-alpha1"
        const val annotation = "androidx.annotation:annotation:1.0.0-alpha1"

        object DataBinding {
            const val version = "3.2.1"
            const val common = "androidx.databinding:databinding-common:$version"
            const val runtime = "androidx.databinding:databinding-runtime:$version"
        }

        object Lifecycle {
            const val extensions = "androidx.lifecycle:lifecycle-extensions:2.0.0"
            const val liveData = "androidx.lifecycle:lifecycle-livedata:2.0.0"
        }

        object Navigation {
            const val version = "1.0.0-alpha08"
            const val runtime = "android.arch.navigation:navigation-runtime:$version"
            const val runtimeKtx = "android.arch.navigation:navigation-runtime-ktx:$version"
            const val fragment = "android.arch.navigation:navigation-fragment:$version"
            const val fragmentKtx = "android.arch.navigation:navigation-fragment-ktx:$version"
            const val ui = "android.arch.navigation:navigation-ui:$version"
            const val uiKtx = "android.arch.navigation:navigation-ui-ktx:$version"
        }
    }

    object Gms {
        // OSSライセンスページの自動生成
        const val ossLicenses = "com.google.android.gms:play-services-oss-licenses:16.0.1"
    }

    object Firebase {
        const val dynamicLinks = "com.google.firebase:firebase-dynamic-links:16.1.5"
    }


    /* ========================================
     * Structure
     * ========================================*/

    object Kotlin {
        const val version = "1.3.11"
        const val stdlibCommon = "org.jetbrains.kotlin:kotlin-stdlib-common:$version"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"
    }

    /* ========================================
     * Debug
     * ========================================*/

    object Fabric {
        // クラッシュレポート
        const val crashlytics = "com.crashlytics.sdk.android:crashlytics:2.9.8@aar"
    }

    object LeakCanary {
        // メモリリーク検出
        private const val version = "1.6.2"
        const val core = "com.squareup.leakcanary:leakcanary-android:$version"
        const val noOp = "com.squareup.leakcanary:leakcanary-android-no-op:$version"
        const val supportFragment = "com.squareup.leakcanary:leakcanary-support-fragment:$version"
    }

    object Timber {
        // ログ出力を便利にする
        const val core = "com.jakewharton.timber:timber:4.7.1"
    }

}