# Android App Template

This project implements you need for your app android:

##Custom theme
Material theme, from Andorid API support level 16-21 can customize which implements toolbar, status bar transparent in API Level 19, fragments pager to view examples and integration with facebook.

##Communication with your server
an easy comunucacion your REST API with a class called RESTClient, intuitive android-based async-http, running on separate thread to the UI.

##simple database
You can create your database with just simply create the attributes you need at the package within DB Models based on AdaFramework.

##services
android services can be a headache, here's an example, a location service that notifies any part of your app with just subscribe via EventBus. An example speaks a thousand words, look at the Services package.


## Screenshots
<img src="/screenshots/s1.png" alt="screenshot" title="screenshot" width="270" height="486" />

## DEMO

[demo apk](/demo-release.apk)

## libraries used
### Gradle


```
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])

    compile 'com.android.support:appcompat-v7:21.0.2'
    compile 'com.android.support:support-v4:21.+'
    compile files('libs/com.mobandme.ada_v2.4.1.jar')
    compile 'com.google.android.gms:play-services-base:6.5.+'
    compile 'com.google.android.gms:play-services-maps:6.5.+'
    compile 'com.google.android.gms:play-services-location:6.5.+'
    compile 'com.afollestad:material-dialogs:0.4.4'
    compile 'fr.avianey:facebook-android-api:+@aar'
    compile 'com.pkmmte.view:circularimageview:1.1'
    compile 'com.koushikdutta.ion:ion:1.+'
    compile 'com.loopj.android:android-async-http:1.4.5'
    compile 'com.mcxiaoke.viewpagerindicator:library:2.4.1'
    compile 'de.greenrobot:eventbus:2.4.0'
    compile 'me.alexrs:wave-drawable:1.0.0'
    compile 'com.xgc1986.ParallaxPagerTransformer:library:0.2-SNAPSHOT@aar'
}

```


## License


```

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

