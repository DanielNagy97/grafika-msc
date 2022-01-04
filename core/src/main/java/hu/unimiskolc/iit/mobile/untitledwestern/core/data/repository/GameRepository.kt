package hu.unimiskolc.iit.mobile.untitledwestern.core.data.repository

import hu.unimiskolc.iit.mobile.untitledwestern.core.data.datasource.GameDataSource
import hu.unimiskolc.iit.mobile.untitledwestern.core.domain.Game

class GameRepository(private val dataSource: GameDataSource) {
    suspend fun add(game: Game) = dataSource.add(game)
    suspend fun update(game: Game) = dataSource.update(game)
    suspend fun remove(game: Game) = dataSource.remove(game)
    suspend fun fetchById(id: Int): Game? = dataSource.fetchById(id)
    suspend fun fetchAll(): List<Game> = dataSource.fetchAll()
    suspend fun fetchOrderByScore(limit: Int): List<Game> = dataSource.fetchOrderByScore(limit)
}