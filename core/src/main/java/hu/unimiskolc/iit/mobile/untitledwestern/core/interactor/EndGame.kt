package hu.unimiskolc.iit.mobile.untitledwestern.core.interactor

import hu.unimiskolc.iit.mobile.untitledwestern.core.data.repository.GameRepository
import hu.unimiskolc.iit.mobile.untitledwestern.core.domain.Game

class EndGame(
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(game: Game) = gameRepository.update(game)
}