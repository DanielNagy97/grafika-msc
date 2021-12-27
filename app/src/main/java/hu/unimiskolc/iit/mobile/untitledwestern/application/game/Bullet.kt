package hu.unimiskolc.iit.mobile.untitledwestern.application.game

import android.content.Context
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.BoundingBox2D
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.GameObject
import hu.unimiskolc.iit.mobile.untitledwestern.application.game.movement.Movement

class Bullet (
    context: Context,
    posX: Float,
    posY: Float,
    scale: Float,
    repeatingInterval: Float,
    minRepeatY: Float,
    maxRepeatX: Float
) : GameObject(context, posX, posY, scale, repeatingInterval, minRepeatY, maxRepeatX){
    var isFired: Boolean = false
    var movement: Movement = Movement(250f, 1)

    init {
        visible = false
    }

    fun updatePosition(dt: Float, viewPort: BoundingBox2D){
        if(isFired){
            position.x += movement.direction * (movement.speed * dt)
            checkViewPort(viewPort)
        }
    }

    private fun checkViewPort(viewPort: BoundingBox2D) {
        if(visible) {
            if(getBoundingBox().minpoint.x > viewPort.maxpoint.x
                || getBoundingBox().maxpoint.x < viewPort.minpoint.x) {
                isFired = false
                visible = false
            }
        }
    }

    fun checkOpponent(opponent: Character){
        if(isFired && getBoundingBox().checkOverlapping(opponent.body.getBoundingBox())){
            isFired = false
            visible = false
            opponent.lives--
            opponent.state.isInjured = true
        }
    }
}