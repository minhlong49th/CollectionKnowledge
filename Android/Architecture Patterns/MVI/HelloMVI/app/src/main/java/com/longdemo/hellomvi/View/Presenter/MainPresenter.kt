package com.longdemo.hellomvi.View.Presenter

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.longdemo.hellomvi.Model.MainView
import com.longdemo.hellomvi.Model.PartialMainState
import com.longdemo.hellomvi.Utils.DataSource
import com.longdemo.hellomvi.View.State.MainViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction

class MainPresenter(internal var dataSource: DataSource): MviBasePresenter<MainView, MainViewState>() {
    override fun bindIntents() {

        val gotData = intent(ViewIntentBinder<MainView, Int> {it.imageIntent})
            .switchMap { index ->
                dataSource.getImageLinkFromList(index)
                    .map { imageLink -> PartialMainState.GetImageLink(imageLink) as PartialMainState }
                    .startWith(PartialMainState.Loading())
                    .onErrorReturn { error -> PartialMainState.Error(error) }
            }

        val initState = MainViewState(false, false, "", null)
        val initIntent = gotData.observeOn(AndroidSchedulers.mainThread())

        subscribeViewState(initIntent.scan(
            initState,
            BiFunction<MainViewState, PartialMainState, MainViewState> {
                prevState, changedState -> this.viewStateReducer(prevState, changedState)
            }
        ))
        {
            obj, viewState -> obj.render(viewState)
        }
    }

    internal fun viewStateReducer(prevState: MainViewState,
                                  changedState: PartialMainState): MainViewState {
        if (changedState is PartialMainState.Loading) {
            prevState.isLoading = true
            prevState.isImageViewShow = false
        }

        if (changedState is PartialMainState.GetImageLink) {
            prevState.isLoading = false
            prevState.isImageViewShow = true
            prevState.imageLink = changedState.imageLink
        }

        if (changedState is PartialMainState.Error) {
            prevState.isLoading = false
            prevState.isImageViewShow = false
            prevState.error = changedState.error
        }

        return prevState
    }
}