# External Resources

[![Build status](https://api.travis-ci.org/prcaen/external-resources.svg?branch=master)](https://travis-ci.org/prcaen/external-resources) [ ![Download](https://api.bintray.com/packages/prcaen/maven/external-resources/images/download.svg) ](https://bintray.com/prcaen/maven/external-resources/_latestVersion)

Update your Android resources (strings, integers, booleans, ...) over the air.

## Main features

- Use native Android resources or default raw JSON / XML files.
- Define your own URL builder which allow you calling your server with query strings or url params
- Define if a config change through onConfigurationChanged if the library should call your server.
- Cache based on Http last modified header.
- Event triggered when resources have changed.
- Event triggered when ressources loading has fail.
- Define your own converter. Json is the default one. This library also provide a Xml converter.
- **Tested**: > 120 tests.

## Download
The libary is available on *JCenter* or *MavenCentral*
Via Gradle:
```gradle
compile 'fr.pierrickcaen:external-resources:2.0.1'
```

Via Maven:
```xml
<dependency>
    <groupId>fr.pierrickcaen</groupId>
    <artifactId>external-resources</artifactId>
    <version>2.0.1</version>
</dependency>
```

## Usage
### Basic

1. Initialization:

  a. Via a String base URL:
  
  eg: If your base url is http://test.com/android-resources.json, it will be append by query parameters: http://test.com/android-resources.json?locale=fr_FR&density_dpi=320&screen_height_dp=100&navigation_hidden=0&... 

     ```java
          public class SampleApplication extends Application {
          
              @Override
              public void onCreate() {
                  super.onCreate();
          
                  ExternalResources.initialize(this, "http://your-url.com/path.json");
              }
          
              @Override
              public void onConfigurationChanged(Configuration newConfig) {
                  super.onConfigurationChanged(newConfig);
          
                  ExternalResources.getInstance().onConfigurationChanged(newConfig);
              }
          
          }
     ```
     
  b. Via URL implementation
     
     ```java
          public class SampleApplication extends Application {
          
              @Override
              public void onCreate() {
                    super.onCreate();
          
                    ExternalResources.initialize(this, new Url() {
                         private Locale locale;
                         
                         @Override public void locale(locale locale) {
                              this.locale = locale;
                         }
                         
                         // ...

                         @Override public String build() {
                              return "http://your-url.com/path.json?locale=" + locale;
                         }
                  });
              }
          
              @Override
              public void onConfigurationChanged(Configuration newConfig) {
                  super.onConfigurationChanged(newConfig);
          
                  ExternalResources.getInstance().onConfigurationChanged(newConfig);
              }
          
          }
     ```

2. Register & Unregister OnExternalResourcesChangeListener
     ```java
     public class SampleActivity extends Activity implements OnExternalResourcesChangeListener {
          @Override protected void onResume() {
              super.onResume();
              ExternalResources.getInstance().register(this);
          }
     
          @Override protected void onPause() {
               ExternalResources.getInstance().unregister(this);
               super.onPause();
          }
          
          @Override public void onExternalResourcesChange(ExternalResources externalResources) {
               // TODO: update your view here.
          }
     }
     ```
3. Use resources as you wish
     
     * String via `getString(@StringRes int resId)` or `getString(@NonNull String key)` or `getString(@StringRes int resId, Object... formatArgs)` or `getString(@NonNull String key, Object... formatArgs)`.
     * String array via `getStringArray(@ArrayRes int resId)` or `getStringArray(@NonNull String key)`.
     * Boolean via `getBoolean(@BoolRes int resId)` or `getBoolean(@NonNull String key)`
     * Color via `getColor(@ColorRes int resId)` or `getColor(@NonNull String key)`
     * Dimension via `getDimension(@DimenRes int resId)` or `getDimension(@NonNull String key)`
     * Integer via `getInteger(@IntegerRes int resId)` or `getInteger(@NonNull String key)`
     * Int array via `getIntArray(@ArrayRes int resId)` or `getIntArray(@NonNull String key)`

     **Warning!**: If external resources is not found, these methods throws a `NotFoundException`.


### Advanced
#### Default resources
In your application class add the following lines in `onCreate`:

```java
@Override
public void onCreate() {
    super.onCreate();

    Resources defaults = Resources.fromJson(getAssets().open("defaults.json"))
    ExternalResources.Builder builder = new ExternalResources.Builder(this, new MyUrlImpl());
    builder.defaultResources(defaults);
}
```
     
Sample app
----------
See the sample application available in `sample-app/src/main`

License
-------

     Copyright (C) 2015 Pierrick CAEN (http://www.pierrickcaen.fr)

     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
