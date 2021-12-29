package hu.unimiskolc.iit.mobile.untitledwestern.core.interactor

import hu.unimiskolc.iit.mobile.untitledwestern.core.data.repository.GameRepository
import hu.unimiskolc.iit.mobile.untitledwestern.core.domain.Game

class GetHighScores (
    private val gameRepository: GameRepository
    ){

    suspend operator fun invoke(limit: Int) : List<Game> {
        return gameRepository.fetchOrderByScore(limit)
        }
}