package com.longdemo.mvimosby.Utils

import io.reactivex.Observable


class DataSource(
    internal val questionList: List<String>,
    internal val answerList: List<String>
) {
    fun askQuestion(questionId: Int): Observable<String> {
        return Observable.just(questionList[questionId])
    }

    fun getAnswer(questionId: Int): Observable<String> {
        return Observable.just(answerList[questionId])
    }
}