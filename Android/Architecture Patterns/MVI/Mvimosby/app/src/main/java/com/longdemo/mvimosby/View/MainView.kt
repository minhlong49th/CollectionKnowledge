package com.longdemo.mvimosby.View

import com.hannesdorfmann.mosby3.mvp.MvpView
import com.longdemo.mvimosby.State.MainViewState
import io.reactivex.Observable

interface MainView: MvpView {
    val askQuestionIntent: Observable<Int>
    val getAnswerIntent: Observable<Int>
    fun render(viewState: MainViewState)
}