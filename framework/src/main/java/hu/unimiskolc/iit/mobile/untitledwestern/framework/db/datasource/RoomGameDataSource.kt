package hu.unimiskolc.iit.mobile.untitledwestern.framework.db.datasource

import hu.unimiskolc.iit.mobile.untitledwestern.core.data.datasource.GameDataSource
import hu.unimiskolc.iit.mobile.untitledwestern.core.domain.Game
import hu.unimiskolc.iit.mobile.untitledwestern.framework.db.dao.GameDao
import hu.unimiskolc.iit.mobile.untitledwestern.framework.db.mapper.GameMapper

class RoomGameDataSource(private val dao: GameDao, private val mapper: GameMapper): GameDataSource{

    override suspend fun add(game: Game) = dao.insert(mapper.mapToEntity(game))

    override suspend fun update(game: Game) = dao.update(mapper.mapToEntity(game))

    override suspend fun remove(game: Game) = dao.delete(mapper.mapToEntity(game))

    override suspend fun fetchById(id: Int): Game? = dao.fetchById(id)?.let {mapper.mapFromEntity(it)}

    override suspend fun fetchAll(): List<Game> = dao.fetchAll().map { mapper.mapFromEntity(it) }

    override suspend fun fetchOrderByScore(limit: Int): List<Game> = dao.fetchOrderByScore(limit).map { mapper.mapFromEntity(it) }
}