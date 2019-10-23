package com.longdemo.mvimosby.State

class MainViewState (
    internal var loading: Boolean,
    internal var questionShow: Boolean,
    internal var answerShow: Boolean,
    internal var textToShow: String,
    internal var error: Throwable?
)