package hu.unimiskolc.iit.mobile.untitledwestern.framework.db.mapper

import hu.unimiskolc.iit.mobile.untitledwestern.core.domain.Game
import hu.unimiskolc.iit.mobile.untitledwestern.framework.db.entity.GameEntity

class GameMapper {
    fun mapToEntity(data: Game) : GameEntity = GameEntity(data.id, data.started, data.ended ?: data.started, data.score)
    fun mapFromEntity(entity: GameEntity) : Game =
        Game(
            entity.id,
            entity.started,
            entity.ended,
            entity.score
        )
}