# RecordingMviRxKotlin

### Summary

This application is an sample Android app to showcase **Model-View-Intent(MVI)** architecture with **RxJava** to implement the reactive chain architecture, written in **Kotlin**. It features:

* [Kotin](https://kotlinlang.org)
* [Inspired by Mosby MVI](http://hannesdorfmann.com/android/mosby3-mvi-1)
* [RxJava2](https://github.com/ReactiveX/RxJava), [RxAndroid](https://github.com/ReactiveX/RxAndroid), [RxBinding](https://github.com/JakeWharton/RxBinding), [RxKotlin](https://github.com/ReactiveX/RxKotlin)
* [Android Architecture Components - ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel.html)
* [Dagger 2](https://github.com/google/dagger)
* [JUnit](https://junit.org/junit4), [JMockit](https://jmockit.github.io)

All popular Android architectural patterns share a common goal to abstract business logic out of the _View_ layer. But by reading _Hannes Dorfmann_'s [Mosby MVI](http://hannesdorfmann.com/android/mosby3-mvi-1) post, compared to the traditional **MVP** and **MVVM** design, I think **MVI** promotes and enforces the following principles and concepts:

* **Unidirectional**(push-only, no-pull) data flow
* **Reactive** processing chain
* **Immutable** data model
* Consolidated **ViewState** definition
* Highest-level of decoupling between View & Presenter layers, boiled down to _Intent_ & _ViewState_

