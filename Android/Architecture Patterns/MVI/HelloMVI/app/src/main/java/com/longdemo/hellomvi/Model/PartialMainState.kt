package com.longdemo.hellomvi.Model

interface PartialMainState {
    class Loading: PartialMainState
    class GetImageLink(var imageLink: String): PartialMainState
    class Error(var error: Throwable): PartialMainState
}