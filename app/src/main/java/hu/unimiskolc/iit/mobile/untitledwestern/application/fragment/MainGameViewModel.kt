package hu.unimiskolc.iit.mobile.untitledwestern.application.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.unimiskolc.iit.mobile.untitledwestern.core.domain.Game
import hu.unimiskolc.iit.mobile.untitledwestern.core.interactor.GameInteractors
import kotlinx.coroutines.launch
import java.util.*

class MainGameViewModel(private val interactors: GameInteractors): ViewModel() {
    private lateinit var game: Game

    fun getGame(): Game {
        return game
    }

    fun startGame() {
        viewModelScope.launch {
            game = interactors.startGame()
        }
    }

    fun endGame(score: Int) {
        viewModelScope.launch {
            game.ended = Date()
            game.score = score
            interactors.endGame(game)
        }
    }
}