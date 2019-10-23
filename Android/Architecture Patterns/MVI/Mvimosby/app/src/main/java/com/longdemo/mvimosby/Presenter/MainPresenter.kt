package com.longdemo.mvimosby.Presenter

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.longdemo.mvimosby.State.MainViewState
import com.longdemo.mvimosby.State.PartialMainState
import com.longdemo.mvimosby.Utils.DataSource
import com.longdemo.mvimosby.View.MainView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class MainPresenter(
    private val dataSource: DataSource
): MviBasePresenter<MainView, MainViewState>() {

    override fun bindIntents() {

        val askQuestion = intent(MainView::askQuestionIntent)
            .switchMap { questionId ->
                dataSource.askQuestion(questionId)
                    .map { question -> PartialMainState.askQuestion(question) as PartialMainState}
                    .startWith(PartialMainState.Loading())
                    .onErrorReturn { error -> PartialMainState.Error(error) }
                    .observeOn(Schedulers.io())
            }

        val getAnswer = intent(MainView::getAnswerIntent)
            .switchMap { questionId ->
                dataSource.getAnswer(questionId)
                    .map { answer -> PartialMainState.getAnswer(answer) as PartialMainState }
                    .startWith(PartialMainState.Loading())
                    .onErrorReturn { error -> PartialMainState.Error(error) }
                    .observeOn(Schedulers.io())
            }

        val initialState = MainViewState(
            false,
            false,
            false,
            "Ask Your Question",
            null)

        var allIntents = Observable.merge(askQuestion, getAnswer)
            .observeOn(AndroidSchedulers.mainThread())

        subscribeViewState(allIntents.scan(
                initialState,
                BiFunction<MainViewState, PartialMainState, MainViewState> {
                        prevState, changedState -> viewStateReducer(prevState, changedState)

                }
            ), MainView::render)
    }

    internal fun viewStateReducer(
        prevState: MainViewState,
        changedState: PartialMainState
    ): MainViewState {

        val newSate = prevState

        if (changedState is PartialMainState.Loading) {
            newSate.loading = true
        }

        else if (changedState is PartialMainState.askQuestion) {
            newSate.loading = false
            newSate.questionShow = true
            newSate.answerShow = false
            newSate.textToShow = changedState.question
        }

        else if (changedState is PartialMainState.getAnswer) {
            newSate.loading = false
            newSate.questionShow = false
            newSate.answerShow = true
            newSate.textToShow = changedState.answer
        }

        else if (changedState is PartialMainState.Error) {
            newSate.loading = false
            newSate.questionShow = false
            newSate.answerShow = false
            newSate.error = changedState.error
        }

        return newSate
    }
}