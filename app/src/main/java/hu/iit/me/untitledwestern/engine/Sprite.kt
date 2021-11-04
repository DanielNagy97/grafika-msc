package hu.iit.me.untitledwestern.engine

import android.content.Context
import hu.iit.me.untitledwestern.MyGLRenderer
import hu.iit.me.untitledwestern.engine.math.Vector2D
import hu.iit.me.untitledwestern.engine.util.ImageUtil

class Sprite {
    private val context: Context
    // frames vector
    var mvFrames: ArrayList<SpriteFrame>

    // Actual frame
    var miActualFrame: Int

    // Position of the sprite
    var position: Vector2D

    // The last time the animation was updated
    private var miLastUpdate: Long

    // The number of frames per second
    private var miFps: Int

    // rotation angle
    var mRotationAngle: Float

    // scale
    var mScale: Float

    constructor(context: Context, filenames: String, numOfFrames: Int, Fps: Int, position: Vector2D, scale: Float){
        this.context = context
        this.miActualFrame = 0
        this.miLastUpdate = System.currentTimeMillis()
        this.miFps = Fps
        this.mvFrames = ArrayList()

        loadTextures(filenames, numOfFrames)

        this.position = position

        this.mScale = scale
        this.mRotationAngle = 0f
    }

    private fun loadTextures(fileNames: String, numOfFrames: Int){
        if (numOfFrames == 1){
            var tex: Texture2D = Texture2D()

            var bitmap = ImageUtil.loadBitmap(context, fileNames)

            tex.createTexture(bitmap)

            var newFrame: SpriteFrame = SpriteFrame(tex, "Frame_1")
            newFrame.addBoundingBox(Vector2D(0.0f, 0.0f), Vector2D(tex.width.toFloat(), tex.height.toFloat()))
            mvFrames.add(newFrame)
        }
        else {
            for (i in 1..numOfFrames){
                var tex: Texture2D = Texture2D()

                var fileName = fileNames.split("/").last()
                var filePath = "$fileNames/$fileName$i.png"
                var bitmap = ImageUtil.loadBitmap(context, filePath)

                tex.createTexture(bitmap)

                var newFrame: SpriteFrame = SpriteFrame(tex, "Frame_$i")
                newFrame.addBoundingBox(Vector2D(0.0f, 0.0f), Vector2D(tex.width.toFloat(), tex.height.toFloat()))
                mvFrames.add(newFrame)
            }
        }
    }

    fun getCurrentFrameTransformedBoundingBox(): BoundingBox2D {
        var currentFrame: SpriteFrame = mvFrames[miActualFrame]

        var original: BoundingBox2D = currentFrame.mBBoxOriginal
        var transformed: BoundingBox2D = currentFrame.mBBoxTransformed
        transformed.setIdentityForTransformation()
        transformed.setPoints(original.minpoint, original.maxpoint)
        transformed.transformByRotate(mRotationAngle)
        transformed.transformByScale(mScale)

        transformed.transformByTranslate(Vector2D(position.x, position.y))

        return transformed
    }

    fun draw(renderer: MyGLRenderer){
        var tex = mvFrames[miActualFrame].mFrame
        tex.draw(renderer, position, mScale, mRotationAngle)
        update()
    }

    private fun update() {
        if (1000.0f / miFps < (System.currentTimeMillis() - miLastUpdate)) {
            miLastUpdate = System.currentTimeMillis();
            if(++miActualFrame == mvFrames.size){
                miActualFrame = 0
            }
        }
    }

}