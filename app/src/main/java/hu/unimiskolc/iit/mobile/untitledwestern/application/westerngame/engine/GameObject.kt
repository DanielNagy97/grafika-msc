package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine

import android.content.Context
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.Renderer
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.math.Vector2D

open class GameObject(private val context: Context,
                      posX: Float, posY: Float,
                      private var scale: Float,
                      var repeatingInterval: Float,
                      var minRepeatY: Float = 0f,
                      var maxRepeatY: Float = 0f) {
    var mSprites: ArrayList<Sprite> = ArrayList()
    var currSprite: Int = 0
    var position: Vector2D = Vector2D(posX, posY)
    var color = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)

    private var rotationAngle: Float = 0f
    var visible: Boolean = true

    fun addSprite(fileNames: String, numberOfFrames: Int, Fps: Int) {
        mSprites.add(Sprite(context, fileNames, numberOfFrames, Fps, position, scale))
    }

    fun getBoundingBox(): BoundingBox2D {
        return mSprites[currSprite].getCurrentFrameTransformedBoundingBox()
    }

    fun enableBoundingBoxesVisibility() {
        for(sprite in mSprites){
            for(spriteFrame in sprite.mvFrames){
                spriteFrame.mBBoxTransformed.mEnabled = true
            }
        }
    }

    fun draw(renderer: Renderer) {
        val sprite = mSprites[currSprite]
        sprite.position = position
        sprite.mRotationAngle = rotationAngle
        sprite.mScale = scale
        sprite.color = color

        sprite.draw(renderer)

        sprite.getCurrentFrameTransformedBoundingBox().draw(renderer)
    }

    fun cleanup(){
        for(sprite in mSprites){
            sprite.cleanup()
        }
        mSprites.clear()
    }
}