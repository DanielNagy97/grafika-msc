package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine

import android.opengl.Matrix
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.math.Vector2D

class CCamera2D(x: Float, y: Float, var aspect: Float, private val viewPortHalfHeight: Float) {
    var mPosition: Vector2D = Vector2D(x, y)
    set(newPos) {
        field = newPos
        viewPort.setPoints(
            Vector2D(mPosition.x - viewPortHalfHeight * aspect, -viewPortHalfHeight),
            Vector2D(mPosition.x + viewPortHalfHeight * aspect, viewPortHalfHeight)
        )
    }
    var viewPort: BoundingBox2D = calcViewPort()

    private fun calcViewPort(): BoundingBox2D {
        return BoundingBox2D(Vector2D(-viewPortHalfHeight * aspect, -viewPortHalfHeight), Vector2D(viewPortHalfHeight * aspect, viewPortHalfHeight))
    }

    fun recalculateViewPort(){
        viewPort = calcViewPort()
    }

    fun moveLeft(value: Float){
        mPosition.x += value
        viewPort.setPoints(
            Vector2D(mPosition.x - viewPortHalfHeight * aspect, -viewPortHalfHeight),
                           Vector2D(mPosition.x + viewPortHalfHeight * aspect, viewPortHalfHeight)
        )
    }

    fun setViewMatrix(viewMatrix: FloatArray) {
        Matrix.setLookAtM(viewMatrix, 0, 0.0f, 0.0f, viewPortHalfHeight, 0.0f, 0.0f, 0.0f, 0.0f, 1f, 0.0f)
        Matrix.translateM(viewMatrix, 0, -mPosition.x, mPosition.y, 0f)
    }
}