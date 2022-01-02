package hu.unimiskolc.iit.mobile.untitledwestern.application.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.unimiskolc.iit.mobile.untitledwestern.core.domain.Game
import hu.unimiskolc.iit.mobile.untitledwestern.core.interactor.GameInteractors
import kotlinx.coroutines.launch

class EndGameViewModel(private val interactors: GameInteractors): ViewModel() {

    private val score: MutableLiveData<Int> = MutableLiveData()
    private val highScores: MutableLiveData<List<Game>> = MutableLiveData()

    fun getScore(): LiveData<Int> = score

    fun setScore(value: Int) {
        score.postValue(value)
    }

    fun getHighScores(): LiveData<List<Game>> = highScores

    fun setHighScores(value: List<Game>) {
        highScores.postValue(value)
    }

    fun fetchHighScores() {
        viewModelScope.launch {
            val scores = interactors.getHighScores(4)
            highScores.postValue(scores)
        }
    }
}