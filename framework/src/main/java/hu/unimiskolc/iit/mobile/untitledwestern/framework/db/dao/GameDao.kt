package hu.unimiskolc.iit.mobile.untitledwestern.framework.db.dao

import androidx.room.*
import hu.unimiskolc.iit.mobile.untitledwestern.framework.db.entity.GameEntity

@Dao
interface GameDao {
    @Insert
    suspend fun insert(entity: GameEntity)

    @Update
    suspend fun update(entity: GameEntity)

    @Delete
    suspend fun delete(entity: GameEntity)

    @Query("SELECT * FROM game WHERE id = :id")
    suspend fun fetchById(id: Int) : GameEntity?

    @Query("SELECT * FROM game")
    suspend fun fetchAll() : List<GameEntity>

    @Query("SELECT * FROM game ORDER BY score DESC LIMIT :limit")
    suspend fun fetchOrderByScore(limit: Int) : List<GameEntity>
}