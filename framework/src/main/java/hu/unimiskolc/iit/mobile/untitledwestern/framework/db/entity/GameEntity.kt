package hu.unimiskolc.iit.mobile.untitledwestern.framework.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "game")
data class GameEntity (
    @PrimaryKey
    val id: Int,
    val started: Date,
    val ended: Date,
    val score: Int
)