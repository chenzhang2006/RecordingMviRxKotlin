# RecordingMviRxKotlin

### Summary

This application is an sample Android app to showcase **Model-View-Intent(MVI)** architecture with **RxJava** to implement the reactive chain, written in **Kotlin**. It features:

* [Kotin](https://kotlinlang.org)
* Inspired by [Mosby MVI blog series](http://hannesdorfmann.com/android/mosby3-mvi-1) (but I am NOT using the framework Mosby in my application)
* [RxJava2](https://github.com/ReactiveX/RxJava), [RxAndroid](https://github.com/ReactiveX/RxAndroid), [RxBinding](https://github.com/JakeWharton/RxBinding), [RxKotlin](https://github.com/ReactiveX/RxKotlin)
* [Android Architecture Components - ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel.html)
* [Dagger 2](https://github.com/google/dagger)
* [JUnit](https://junit.org/junit4), [JMockit](https://jmockit.github.io)

All popular Android architectural patterns(**MVP**, **MVVP**, **MVI**) share a common goal to abstract business logic out of the _View_ layer. But by reading _Hannes Dorfmann_'s [Mosby MVI](http://hannesdorfmann.com/android/mosby3-mvi-1) post, compared to **MVP** and **MVVM** design, **MVI** promotes and enforces the following principles and concepts that were lacked by other patterns:

* **Unidirectional**(push-only, no-pull principle) data flow
* **Reactive** processing chain
* **Immutable** data model
* Consolidated **ViewState** definition
* **Highest-level of decoupling** between _View_ & _Presenter_ layers; All interactions are boiled down to 2 abstractions: _Intent_ & _ViewState_

### Model-View-Intent Design Details

I can summarize the design and motivation behind this project. But regarding the detailed rationale in **MVI**, the best way to understand the fundamentals is to read [MVI blog series](http://hannesdorfmann.com/android/mosby3-mvi-1)

#### Mathematical Abstraction:

<img src="https://user-images.githubusercontent.com/17072625/39215583-6894318c-47e6-11e8-868e-a403873ebce1.png" alt="MVI mathematical model"/>

#### Code Component Design:

<img src="https://user-images.githubusercontent.com/17072625/39271226-678cf86c-48a6-11e8-99d2-46f6b98016db.png" alt="MVI contract"/>

#### What is the importance of a consolidated ViewState(Model)

[Post #1 in the series ](http://hannesdorfmann.com/android/mosby3-mvi-1) perfectly explained the rationale.

#### What is "intent" in MVI?

_Intent_ represents user's intent, through his/her screen gesture. E.g. button click or item selection. Described in [Post #2 in series](http://hannesdorfmann.com/android/mosby3-mvi-2)

#### Why need _ViewModel_?

Because [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel.html) survives the configuration change through screen rotation.

#### Intent and ViewState are ALL the interactions between _View_ with _ViewModel_

```
interface MviView<I : MviIntent, in S : MviViewState> {
       fun intents(): Observable<I>
       fun render(state: S)
   }
```

```
interface MviViewModel<I : MviIntent, S : MviViewState> {
    fun processIntents(intents: io.reactivex.Observable<I>)
    fun states(): Observable<S>
}
```

#### Action from Intent

Intents are mapped to their logic *Action*. E.g. user "pull to refresh" intent mapped to "reload" action

#### Processor

Rx operation chain to process *Action*; _Processors_ & _ApiRepository_ are where business logic lives. _Processors_ return intermediate & final _Result_(including errors).

#### Result

Output from *Processor*, including errors

#### Reducer

Responsible to merge *Result* into the previous _ViewState_ and create a new _ViewState_. Well described in [Post #3 in the series](http://hannesdorfmann.com/android/mosby3-mvi-3)