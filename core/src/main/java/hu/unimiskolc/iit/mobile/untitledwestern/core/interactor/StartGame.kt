package hu.unimiskolc.iit.mobile.untitledwestern.core.interactor

import android.os.SystemClock
import hu.unimiskolc.iit.mobile.untitledwestern.core.data.repository.GameRepository
import hu.unimiskolc.iit.mobile.untitledwestern.core.domain.Game
import java.util.*

class StartGame (
    private val gameRepository: GameRepository
        ){

    suspend operator fun invoke() : Game {
        val newGame = Game((SystemClock.elapsedRealtime()/1000.0).toInt(), Date(), null, 0)
        gameRepository.add(newGame)
        gameRepository
        return newGame
    }
}