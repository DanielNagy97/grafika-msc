package hu.unimiskolc.iit.mobile.untitledwestern.application.game

import android.content.Context
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.GameObject

class Collectible (
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