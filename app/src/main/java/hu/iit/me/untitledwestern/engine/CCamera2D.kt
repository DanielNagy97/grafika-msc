package hu.iit.me.untitledwestern.engine

import android.opengl.Matrix
import hu.iit.me.untitledwestern.engine.math.Vector2D

class CCamera2D(x: Float, y: Float, var id: Int, var aspect: Float) {
    var mPosition: Vector2D = Vector2D(x, y)
    private val transformationMatrix: FloatArray = FloatArray(16)
    //val aspect = 3120f / 1440f

    val viewPort = BoundingBox2D(Vector2D(-82f * aspect, -82f), Vector2D(82f * aspect, 82f))

    fun moveLeft(value: Float){
        mPosition.x += value
        viewPort.setPoints(Vector2D(mPosition.x - 82f * aspect, -82f),
                           Vector2D(mPosition.x + 82f * aspect, 82f))
    }

    @JvmName("setMPosition1")
    fun setMPosition(newPos: Vector2D){
        mPosition = newPos
        viewPort.setPoints(Vector2D(mPosition.x - 82f * aspect, -82f),
                            Vector2D(mPosition.x + 82f * aspect, 82f))
    }

    fun moveRight(value: Float){
        mPosition.x -= value
    }

    fun moveUp(value: Float){
        mPosition.y += value
    }

    fun moveDown(value: Float){
        mPosition.x -= value
    }

    fun moveRelative(valueX: Float, valueY: Float){
        mPosition.x += valueX
        mPosition.y += valueY
    }

    fun setViewMatrix(viewMatrix: FloatArray) {
        Matrix.setLookAtM(viewMatrix, 0, 0.0f, 0.0f, 82f, 0.0f, 0.0f, 0.0f, 0.0f, 1f, 0.0f)
        //Matrix.setIdentityM(transformationMatrix, 0)
        Matrix.translateM(viewMatrix, 0, -mPosition.x, mPosition.y, 0f)

        //return transformationMatrix
    }

    fun setPosition(x: Float, y: Float){
        mPosition.set(x, y)
    }
}