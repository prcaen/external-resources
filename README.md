External Resources
==================

ExternalResources is a Android library which allows you to use resources over the air.

Main features
-------------
* Download external strings, booleans, integers, colors, strings-array and integers-array from internet on a given URL.
* Each time the configuration changes, the given URL is call with query parameters
* Configurable and easy to use.
* Callback triggered when configuration change.

How to use?
1. In `YourApplication.java` in `onCreate` add the following line:
```java
ExternalResources.initialize(this, "http://your-base-url.com/");
```
2. In `YourApplication.java` in `onConfigurationChanged` add the following line:
```java
ExternalResources.getInstance().onConfigurationChanged(newConfig);
```
3. In `YourActivity.java` you can access a resource through:
```java
ExternalResources.getInstance().getString("my_string");
```

*Optional*
You can register a listener:
```java
ExternalResources.getInstance().register(this);
```

Don't forget to unregister it through:
```java
ExternalResources.getInstance().unregister(this);
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