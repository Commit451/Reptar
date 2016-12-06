# Reptar
Roaring RxJava

[![Build Status](https://travis-ci.org/Commit451/Reptar.svg?branch=master)](https://travis-ci.org/Commit451/Reptar) [![](https://jitpack.io/v/Commit451/Reptar.svg)](https://jitpack.io/#Commit451/Reptar)

# Gradle Dependency
Add the jitpack url to the project:
```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
then, in your app `build.gradle`
```groovy
dependencies {
    compile 'com.github.Commit451.Reptar:reptar:latest.version.here@aar'
    //for Retrofit support
    compile 'com.github.Commit451.Reptar:reptar-retrofit:latest.version.here@aar'
}
```

# Usage
For instances where you only want to implement the callbacks you need:
* `AdapterObserver`
* `AdapterSingleObserver`

For `Observer`s where you only care about `onNext` and `onError`, use `SimpleObserver`

For `SingleObserver`s where you only care about `onSuccess` and `onError`, use `SimpleSingleObserver`

#Usage Retrofit
For Retrofit, many times, you need to get the raw response from Retrofit, but you also want all non 2XX error codes to fall through to the `onError()`. For this, `ResponseSingleObservable` is perfect:
```java
gitHub.contributors("square", "okhttp")
    .subscribe(new ResponseSingleObserver<List<Contributor>>() {
        @Override
        protected void onResponseSuccess(List<Contributor> contributors) {
            int responseCode = getResponse().code();
            //do what you need to with the response
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof HttpException) {
                //check the response code, do what you need to
            } else {
                //handle any other error
            }
        }
    });
```

License
--------

    Copyright 2016 Commit 451

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
