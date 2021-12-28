package hu.unimiskolc.iit.mobile.untitledwestern.application.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EndGameViewModel: ViewModel() {

    private val score: MutableLiveData<Int> = MutableLiveData()

    fun getScore(): LiveData<Int> = score

    fun setScore(value: Int) {
        score.postValue(value)
    }
}