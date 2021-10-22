package hu.iit.me.untitledwestern.engine

import android.content.Context
import hu.iit.me.untitledwestern.MyGLRenderer
import hu.iit.me.untitledwestern.engine.math.Vector2D

class GameObject {
    private val context: Context
    var mSprites: ArrayList<Sprite>
    var currSprite: Int
    var position: Vector2D

    var scale: Float
    var rotationAngle: Float

    constructor(context: Context, posX: Float, posY: Float, scale: Float){
        this.context = context
        this.position = Vector2D(posX, posY)

        this.scale = scale
        rotationAngle = 0f
        currSprite = 0
        mSprites = ArrayList()
    }

    fun addSprite(fileNames: String, numberOfFrames: Int, Fps: Int) {
        mSprites.add(Sprite(context, fileNames, numberOfFrames, Fps, position, scale))
    }

    fun draw(renderer: MyGLRenderer) {
        var sprite = mSprites[currSprite]
        sprite.position = position
        sprite.mRotationAngle = rotationAngle
        sprite.mScale = scale

        sprite.draw(renderer)
    }
}