package hu.unimiskolc.iit.mobile.untitledwestern.application.game

import android.content.Context
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.GameObject

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
    var xDir: Int = 1
    var speed: Float = 250f

    init {
        visible = false
    }

    fun updatePosition(dt: Float){
        if(isFired){
            position.x += xDir * (speed * dt)
        }
    }
}