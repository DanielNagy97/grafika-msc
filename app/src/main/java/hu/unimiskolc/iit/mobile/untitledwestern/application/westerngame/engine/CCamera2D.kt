package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine

import android.opengl.Matrix
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.math.Vector2D

class CCamera2D(x: Float, y: Float, var aspect: Float) {
    var mPosition: Vector2D = Vector2D(x, y)

    // TODO: Make the 82f constant or something
    var viewPort = BoundingBox2D(Vector2D(-82f * aspect, -82f), Vector2D(82f * aspect, 82f))

    fun recalculateViewPort(){
        viewPort = BoundingBox2D(Vector2D(-82f * aspect, -82f), Vector2D(82f * aspect, 82f))
    }

    fun moveLeft(value: Float){
        mPosition.x += value
        viewPort.setPoints(
            Vector2D(mPosition.x - 82f * aspect, -82f),
                           Vector2D(mPosition.x + 82f * aspect, 82f)
        )
    }

    @JvmName("setMPosition1")
    fun setMPosition(newPos: Vector2D){
        mPosition = newPos
        viewPort.setPoints(
            Vector2D(mPosition.x - 82f * aspect, -82f),
                            Vector2D(mPosition.x + 82f * aspect, 82f)
        )
    }

    fun setViewMatrix(viewMatrix: FloatArray) {
        Matrix.setLookAtM(viewMatrix, 0, 0.0f, 0.0f, 82f, 0.0f, 0.0f, 0.0f, 0.0f, 1f, 0.0f)
        Matrix.translateM(viewMatrix, 0, -mPosition.x, mPosition.y, 0f)
    }
}