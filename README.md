# Coubic Android Browser

[![JitPack](https://jitpack.io/v/coubic/android-browser.svg)](https://jitpack.io/#coubic/android-browser)

Custom Activity including WebView to manage toolbar title and access token flexibly.

## Download

### Project build.gradle

```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

### App build.gradle

```groovy
dependencies {
    ...
    implementation 'com.coubic.browser:android-browser:LATEST_VERSION'
}
```

`LATEST_VERSION` is  [![JitPack](https://jitpack.io/v/coubic/android-browser.svg)](https://jitpack.io/#coubic/android-browser).

## Usage

You can call `WebViewActivity` with some parameters.

```kotlin
startActivity(
  WebViewActivity.createIntent(
    context = activity,
    url = "https://github.com/coubic/android-browser",
    title = "Coubic Custom Browser" // Optional
    subtitle = "https://github.com/coubic/android-browser", // Optional
    token = "token" // Optional
  )
)
```

You can customize theme in AndroidManifest.xml.

```xml
<style name="AppTheme.WebView">
  <item name="android:statusBarColor">@color/primary_dark</item>
  <item name="toolbarStyle">@style/WebViewToolbar</item>
  <item name="titleTextColor">@color/white</item>
</style>

<style name="WebViewToolbar" parent="Widget.AppCompat.Toolbar">
  <item name="android:background">@color/primary</item>
  <item name="titleTextColor">@color/white</item>
  <item name="subtitleTextColor">@color/text_grey_alpha_50</item>
</style>
```

```xml
<activity
  android:name="com.coubic.browser.WebViewActivity"
  android:configChanges="keyboard|keyboardHidden|screenLayout|screenSize|orientation"
  android:theme="@style/AppTheme.WebView" />
```

## Contributing
We are always welcome your contribution!
If you find a bug or want to add new feature, please raise issue.

## License

```
Copyright 2019 Coubic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
