# RecordingMviRxKotlin

<img src="https://user-images.githubusercontent.com/17072625/39311626-707d6020-493b-11e8-8289-aacb9969d4e2.png" alt="screenshot"/>

Demo Video: https://www.youtube.com/watch?v=eZ-89Ni6hng&feature=youtu.be

### Summary

This application is an sample Android app(started Spring 2018, updated Summer 2019) to showcase **Model-View-Intent(MVI)** architecture with **RxJava** to implement the reactive chain, written in **Kotlin**. It features:

* [Kotlin](https://kotlinlang.org)
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

```
render ( param: viewState = businessLogic ( listen ( userIntents () ) ) )
```

#### Code Component Design:

<img src="https://user-images.githubusercontent.com/17072625/39271226-678cf86c-48a6-11e8-99d2-46f6b98016db.png" alt="MVI contract"/>

#### Why data *Immutability* and *Unidirectional* flow are important

Because in my professional experience, when the business requirement got much more complex and the dev team became large in size, we want to keep things simple, so we can engineer and debug code more effectively.

When we expose data models from business logic out to the wild, we don't want other components in our app to modify model/state and we loose track of them. *Data immutablility* helps set the rules.

On the same token, *Unidirectional* data flow establishes and maintains the *Source of Truth* within *Business Logic* layer. Regardless how UI are affected by device state changes, we always have the truth on the backend and are able to "recover".


#### What is the importance of a consolidated ViewState(Model)

[Post #1 in the series](http://hannesdorfmann.com/android/mosby3-mvi-1) perfectly explained the rationale.

#### What is "intent" in MVI?

_Intent_ represents user's intent by screen gestures(NOT Android's system *Intent*). E.g. button click or item selection. Described in [Post #2 in series](http://hannesdorfmann.com/android/mosby3-mvi-2)

#### Why need _ViewModel_?

Because [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel.html) survives the configuration change through screen rotation. Perfect fit to hold the hot observable to replay the last *ViewState* post-screen-rotation.

We could also use *Dagger2*'s *Custom Scope*(e.g. @ActivitiyScope) to maintain presenters as an alternative solution, in order for *model* to survive configuration changes.

[Post #6 in the series](http://hannesdorfmann.com/android/mosby3-mvi-6) explained this area in Mosby but I am using *ViewModel* & *RxJava state stream* to achieve the same result.

#### *Intent* and *ViewState* are ALL the interactions between *View* with *ViewModel*

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

Intents are mapped to their logic *Action*. *Action* is the direct input for the *Business Logic(Processor & ApiRepository)* layer. E.g. user "pull to refresh" intent mapped to "reload" action. There might be many-to-many relationship between *Intent* and *Action*

#### Processor

Rx operation chain to process *Action*; _Processors_ & _ApiRepository_ are where business logic lives. _Processors_ return intermediate & final _Result_(including errors).

#### Result

Output from *Processor*, including errors

#### Reducer

Responsible to merge *Result* into the previous _ViewState_ and create a new _ViewState_, which is observed and rendered on UI layer. Well described in [Post #3 in the series](http://hannesdorfmann.com/android/mosby3-mvi-3)

### Other Notes About the Application

* *Material Design* with AppBar/CollapsingToolbar, NavigationDrawer, etc.
* Mocked backend *ApiRepository* and some UI observers for sake of simplicity and demo purposes
* *RecordingsViewModelTest* to demo unit test on *ViewModel*. But ALL components(UI, ViewModel, Processors, ApiRepository) are testable, thanks to the decoupled architectural components.