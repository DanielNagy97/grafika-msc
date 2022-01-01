package hu.unimiskolc.iit.mobile.untitledwestern.application.fragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.unimiskolc.iit.mobile.untitledwestern.application.R
import hu.unimiskolc.iit.mobile.untitledwestern.application.databinding.EndGameFragmentBinding
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

    fun fetchHighScores(context: Context, binding: EndGameFragmentBinding) {
        viewModelScope.launch {
            val scores = interactors.getHighScores(4)
            highScores.postValue(scores)

            //val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, arrayListOf(1,2,3,4))
            val scoreAdapter = ScoreAdapter(context, R.layout.adapter_view_layout, scores)
            binding?.highScores.myListView.adapter = scoreAdapter
        }
    }
}