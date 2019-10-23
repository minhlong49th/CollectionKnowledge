package com.longdemo.mvimosby.State

interface PartialMainState {
    class askQuestion(var question: String): PartialMainState
    class getAnswer(var answer: String): PartialMainState
    class Loading: PartialMainState
    class Error(var error: Throwable): PartialMainState
}