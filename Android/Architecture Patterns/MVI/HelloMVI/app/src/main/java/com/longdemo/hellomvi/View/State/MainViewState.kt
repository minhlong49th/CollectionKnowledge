package com.longdemo.hellomvi.View.State

class MainViewState(
    internal var isLoading: Boolean,
    internal var isImageViewShow: Boolean,
    internal var imageLink: String,
    internal var error: Throwable?
)