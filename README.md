# Reptar
Roaring [RxJava](https://github.com/ReactiveX/RxJava). A collection of useful RxJava 2.X classes.

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
    //for Kotlin support
    compile 'com.github.Commit451.Reptar:reptar-kotlin:latest.version.here@aar'
}
```

# Usage
Usage can be found in the sample project.

### Observers
For instances where you only want to implement certain callbacks, use:
* `AdapterObserver`
* `AdapterSingleObserver`

### Composable Observers
Often times, you want to subscribe to `Observable`s and have certain rules surrounding the observers. For example, you may want to use a `SingleObserver`, but ignore any `CancellationException`s since your `Activity` or `Fragment` is probably destroyed when this exception is thrown. You could potentially override the `Observer` and do the check in the subclass. But, this can result in tons of subclasses that all perform simple boolean checks.

`ComposableSingleObserver` and `ComposableObserver` make it simple to add checks on the success and failure of observers. For example:
```java
someObservable
    .subscribe(new ComposableSingleObserver<Boolean>() {
        @Override
        public void success(Boolean aBoolean) {
            //do some success thing
        }

        @Override
        public void error(Throwable t) {
            //error block will never get CancellationExceptions
            onHandleError(t);
        }
    }.add(new CancellationFailureChecker()));
```
CancellationFailureChecker:
```java
public class CancellationFailureChecker implements FailureChecker {

    @Override
    public boolean check(Throwable t) {
        return t instanceof CancellationException;
    }
}
```

You could also just create an abstract Observer that you use throughout the app that contains these rules so that you do not have to repeat them:
```java
public abstract class CustomSingleObserver<T> extends ComposableSingleObserver<T> {

    public CustomSingleObserver() {
        add(new CancellationFailureChecker());
        //add other success and failure checks as desired
    }

}
```
See `SuccessChecker` and `FailureChecker` for more.

### Avoiding Null
RxJava 2.x does not allow propagating null. Read more [here](https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#nulls). `null` is still something we may not want to have fall through into the `onError` block though. For instance, if we want to check if a value exists, we could say that `null` means no, and a valid value means yes.

As a replacement, we can use `Result`. For example:
```java
Result<String> result;
if (random.nextInt() % 2 == 0) {
    result = new Result<>("hi");
} else {
    result = Result.empty();
}
Single.just(result)
        .subscribe(new ComposableSingleObserver<Result<String>>() {
            @Override
            public void success(Result<String> result) {
                if (result.hasValue()) {
                    Snackbar.make(root, "Has a result", Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    Snackbar.make(root, "No result", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void error(Throwable e) {
                //Note that an empty result would not be an error
            }
        });
```
This is similar to an `Optional` in [Guava](https://github.com/google/guava/wiki/UsingAndAvoidingNullExplained#optional)

# Retrofit Usage
For Retrofit, many times, you need to get the raw response from Retrofit, but you also want all non 2XX error codes to fall through to the `onError()`. For this, `ResponseSingleObservable` is perfect:
```java
gitHub.contributors("square", "okhttp")
    .subscribe(new ResponseSingleObserver<List<Contributor>>() {
        @Override
        protected void responseSuccess(List<Contributor> contributors) {
            int responseCode = response().code();
            //do what you need to with the response
        }

        @Override
        public void error(Throwable e) {
            if (e instanceof HttpException) {
                //check the response code, do what you need to
            } else {
                //handle any other error
            }
        }
    });
```

# Kotlin Usage
Kotlin extensions allow for easy composition of Single and Observable for Android:
```kotlin
gitHub.contributors("jetbrains", "kotlin")
    .fromIoToMainThread()
    .subscribe(object : CustomSingleObserver<List<Contributor>>() {
        override fun success(t: List<Contributor>) {
            Snackbar.make(root, "It worked!", Snackbar.LENGTH_SHORT)
                    .show()
        }

        override fun error(t: Throwable) {
            onHandleError(t)
        }
    })
```

License
--------

    Copyright 2017 Commit 451

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
