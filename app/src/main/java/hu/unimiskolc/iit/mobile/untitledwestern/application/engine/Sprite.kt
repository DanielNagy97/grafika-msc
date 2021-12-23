package hu.unimiskolc.iit.mobile.untitledwestern.application.engine

import android.content.Context
import hu.unimiskolc.iit.mobile.untitledwestern.application.MyGLRenderer
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.math.Vector2D
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.util.ImageUtil

class Sprite(
    private val context: Context,
    filenames: String,
    numOfFrames: Int,
    private var miFps: Int,// Position of the sprite
    var position: Vector2D,
    var mScale: Float
) {
    var mvFrames: ArrayList<SpriteFrame>
    var miActualFrame: Int
    private var miLastUpdate: Long
    var mRotationAngle: Float
    var toFlip: Boolean

    init {
        this.miActualFrame = 0
        this.miLastUpdate = System.currentTimeMillis()
        this.mvFrames = ArrayList()
        loadTextures(filenames, numOfFrames)
        this.mRotationAngle = 0f
        this.toFlip = false
    }

    private fun loadTextures(fileNames: String, numOfFrames: Int){
        if (numOfFrames == 1){
            val tex = Texture2D()

            val bitmap = ImageUtil.loadBitmap(context, fileNames)

            tex.createTexture(bitmap)

            val newFrame = SpriteFrame(tex)
            newFrame.addBoundingBox(Vector2D(0.0f, 0.0f), Vector2D(tex.width.toFloat(), tex.height.toFloat()))
            mvFrames.add(newFrame)
        }
        else {
            for (i in 1..numOfFrames){
                val tex = Texture2D()

                val fileName = fileNames.split("/").last()
                val filePath = "$fileNames/$fileName$i.png"
                val bitmap = ImageUtil.loadBitmap(context, filePath)

                tex.createTexture(bitmap)

                val newFrame = SpriteFrame(tex)
                newFrame.addBoundingBox(Vector2D(0.0f, 0.0f), Vector2D(tex.width.toFloat(), tex.height.toFloat()))
                mvFrames.add(newFrame)
            }
        }
    }

    fun getCurrentFrameTransformedBoundingBox(): BoundingBox2D {
        val currentFrame: SpriteFrame = mvFrames[miActualFrame]

        val original: BoundingBox2D = currentFrame.mBBoxOriginal
        val transformed: BoundingBox2D = currentFrame.mBBoxTransformed
        transformed.setPoints(original.minpoint, original.maxpoint)

        transformed.transformByRotate(mRotationAngle)
        transformed.transformByScale(mScale)
        transformed.transformByTranslate(Vector2D(position.x, position.y))

        return transformed
    }

    fun draw(renderer: MyGLRenderer){
        val tex = mvFrames[miActualFrame].mFrame
        tex.draw(renderer, position, mScale, mRotationAngle, toFlip)
        update()
    }

    private fun update() {
        if (1000.0f / miFps < (System.currentTimeMillis() - miLastUpdate)) {
            miLastUpdate = System.currentTimeMillis()
            if(++miActualFrame == mvFrames.size){
                miActualFrame = 0
            }
        }
    }

    fun cleanup(){
        for(frame in mvFrames){
            val tex = frame.mFrame
            tex.cleanup()
        }
    }

}