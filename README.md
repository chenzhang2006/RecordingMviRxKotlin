# RecordingMviRxKotlin

### Summary

This application is an sample Android app to showcase **Model-View-Intent(MVI)** architecture with **RxJava** to implement the reactive chain architecture, written in **Kotlin**. It features:

* [Kotin](https://kotlinlang.org)
* Inspired by [Mosby MVI blog series](http://hannesdorfmann.com/android/mosby3-mvi-1) and [Contract of Model-View-Intent Architecture](https://proandroiddev.com/the-contract-of-the-model-view-intent-architecture-777f95706c1e)
* [RxJava2](https://github.com/ReactiveX/RxJava), [RxAndroid](https://github.com/ReactiveX/RxAndroid), [RxBinding](https://github.com/JakeWharton/RxBinding), [RxKotlin](https://github.com/ReactiveX/RxKotlin)
* [Android Architecture Components - ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel.html)
* [Dagger 2](https://github.com/google/dagger)
* [JUnit](https://junit.org/junit4), [JMockit](https://jmockit.github.io)

All popular Android architectural patterns share a common goal to abstract business logic out of the _View_ layer. But by reading _Hannes Dorfmann_'s [Mosby MVI](http://hannesdorfmann.com/android/mosby3-mvi-1) post, compared to the traditional **MVP** and **MVVM** design, I think **MVI** promotes and enforces the following principles and concepts:

* **Unidirectional**(push-only, no-pull) data flow
* **Reactive** processing chain
* **Immutable** data model
* Consolidated **ViewState** definition
* Highest-level of decoupling between _View_ & _Presenter_ layers, boiled down to _Intent_ & _ViewState_

### Model-View-Intent Details

Abstraction:

<img src="https://user-images.githubusercontent.com/17072625/39215583-6894318c-47e6-11e8-868e-a403873ebce1.png" alt="MVI simple flow"/>

Layers:

<img src="https://user-images.githubusercontent.com/17072625/39216243-e6d26620-47e8-11e8-9b78-1a5835d0e88d.png" alt="MVI contract"/>

