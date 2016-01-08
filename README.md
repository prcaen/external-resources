# External Resources

[![Build status](https://api.travis-ci.org/prcaen/external-resources.svg?branch=develop)](https://travis-ci.org/prcaen/external-resources)

ExternalResources is a Android library which allows you to use resources over the air.

## Main features
* Download external strings, booleans, integers, colors, strings-array and integers-array from internet on a given URL.
* Each time the configuration changes, the given URL is call with query parameters
* Configurable and easy to use.
* Callback triggered when configuration change.
* **Tested**: > 100 tests.

## Usage
### Basic
1. A sample application:

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

2. A sample activity:

     ```java
     public class SampleActivity extends Activity implements OnExternalResourcesChangeListener {
     
       private TextView sampleTextView;
     
       @Override
       protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
     
         setContentView(R.layout.main_activity);
     
         ExternalResources.getInstance().register(this);
     
         sampleTextView = (TextView) findViewById(R.id.sample);
         sampleTextView.setText(ExternalResources.getInstance().getString("my_string"));
       }
     
       @Override
       protected void onResume() {
         super.onResume();
     
         ExternalResources.getInstance().register(this);
       }
     
       @Override
       protected void onPause() {
         ExternalResources.getInstance().unregister(this);
         
         super.onPause();
       }
     
       @Override
       public void onExternalResourcesChange(ExternalResources externalResources) {
         sampleTextView.setText(externalResources.getString("my_string"));
       }
     
     }
     ```
*Warning!*: If external resources are not dowloaded, this method throw a `NotFoundException`.

### Advanced
#### Default resources
In your application class add the following lines in `onCreate`:

```java
@Override
public void onCreate() {
    super.onCreate();

    Resources defaults = Resources.fromJson(getAssets().open("defaults.json"))
    ExternalResources.initialize(this, "http://your-url.com/path.json", defaults);
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
