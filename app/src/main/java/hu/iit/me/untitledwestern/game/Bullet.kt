package hu.iit.me.untitledwestern.game

import android.content.Context
import hu.iit.me.untitledwestern.engine.GameObject

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