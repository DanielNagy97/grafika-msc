package hu.unimiskolc.iit.mobile.untitledwestern.application.engine

import android.content.Context
import hu.unimiskolc.iit.mobile.untitledwestern.application.MyGLRenderer
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.math.Vector2D

open class GameObject(private val context: Context,
                      posX: Float, posY: Float,
                      private var scale: Float,
                      var repeatingInterval: Float,
                      var minRepeatY: Float = 0f,
                      var maxRepeatY: Float = 0f) {
    var mSprites: ArrayList<Sprite> = ArrayList()
    var currSprite: Int = 0
    var position: Vector2D = Vector2D(posX, posY)

    private var rotationAngle: Float = 0f
    var visible: Boolean = true

    fun addSprite(fileNames: String, numberOfFrames: Int, Fps: Int) {
        mSprites.add(Sprite(context, fileNames, numberOfFrames, Fps, position, scale))
    }

    fun getBoundingBox(): BoundingBox2D {
        return mSprites[currSprite].getCurrentFrameTransformedBoundingBox()
    }

    fun draw(renderer: MyGLRenderer) {
        val sprite = mSprites[currSprite]
        sprite.position = position
        sprite.mRotationAngle = rotationAngle
        sprite.mScale = scale

        sprite.draw(renderer)

        sprite.getCurrentFrameTransformedBoundingBox().draw(renderer)
    }

    fun cleanup(){
        for(sprite in mSprites){
            sprite.cleanup()
        }
    }
}