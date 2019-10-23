package com.longdemo.mvimosby

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.RxView
import com.longdemo.mvimosby.Presenter.MainPresenter
import com.longdemo.mvimosby.State.MainViewState
import com.longdemo.mvimosby.Utils.DataSource
import com.longdemo.mvimosby.View.MainView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * https://expertise.jetruby.com/how-to-implement-mvi-architecture-with-mosby-in-android-app-bc3d64e04739
 */
class MainActivity : MviActivity<MainView, MainPresenter>(), MainView {

    lateinit var questionList: List<String>
    lateinit var answerList: List<String>

    override val askQuestionIntent: Observable<Int>
        get() = RxView.clicks(btn_question)
            .map { click -> getRandomNumberInRange(0, questionList.size - 1) }

    override val getAnswerIntent: Observable<Int>
        get() = RxView.clicks(btn_answer)
            .map { click -> getRandomNumberInRange(0, answerList.size - 1) }

    override fun render(viewState: MainViewState) {

        if (viewState.loading) {
            progress_bar.visibility = View.VISIBLE
            btn_question.isEnabled = false
            btn_answer.isEnabled = false
        } else if (viewState.error != null) {
            progress_bar.visibility = View.GONE
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        } else if (!viewState.questionShow || viewState.answerShow) {
            progress_bar.visibility = View.GONE
            btn_question.isEnabled = true
            btn_answer.isEnabled = false
            text_view.text = viewState.textToShow
        } else {
            progress_bar.visibility = View.GONE
            btn_question.isEnabled = false
            btn_answer.isEnabled = true
            text_view.text = viewState.textToShow
        }
    }

    override fun createPresenter(): MainPresenter {
        return MainPresenter(DataSource(questionList, answerList))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionList = createQuestionList()
        answerList = createAnswerList()
    }

    private fun getRandomNumberInRange(min: Int, max: Int): Int? {
        if (min > max)
            throw IllegalArgumentException("Max must be greater than Min")

        val r = Random()
        return r.nextInt(max - min + 1) + min
    }

    private fun createAnswerList(): List<String> {
        return Arrays.asList(
            "Question 1",
            "Question 2",
            "Question 3",
            "Question 4",
            "Question 5"
        )
    }

    private fun createQuestionList(): List<String> {
        return Arrays.asList(
            "Answer 1",
            "Answer 2",
            "Answer 3",
            "Answer 4",
            "Answer 5"
        )
    }
}
