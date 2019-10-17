package com.longdemo.hellomvi.Model

import com.hannesdorfmann.mosby3.mvp.MvpView
import com.longdemo.hellomvi.View.State.MainViewState
import io.reactivex.Observable

interface MainView : MvpView {
    var imageIntent: Observable<Int>

    fun render(viewState: MainViewState)
}