package hu.unimiskolc.iit.mobile.untitledwestern.core.data.datasource

import hu.unimiskolc.iit.mobile.untitledwestern.core.domain.Game

interface GameDataSource {
    suspend fun add(game: Game)
    suspend fun update(game: Game)
    suspend fun remove(game: Game)
    suspend fun fetchById(id: Int) : Game?
    suspend fun fetchAll(): List<Game>
    suspend fun fetchOrderByScore(limit: Int): List<Game>
}