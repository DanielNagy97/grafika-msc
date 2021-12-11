package hu.iit.me.untitledwestern.game

import android.content.Context
import hu.iit.me.untitledwestern.engine.GameObject

class Coin (
    context: Context,
    posX: Float,
    posY:Float,
    scale: Float,
    repeatingInterval: Float,
    minRepeatY: Float,
    maxRepeatX: Float,
    val value: Int
) : GameObject(context,posX, posY, scale, repeatingInterval, minRepeatY, maxRepeatX){

}