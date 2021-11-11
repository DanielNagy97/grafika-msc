package hu.iit.me.untitledwestern.engine

import android.opengl.GLES30
import android.opengl.Matrix
import hu.iit.me.untitledwestern.MyGLRenderer
import hu.iit.me.untitledwestern.engine.math.Vector2D
import hu.iit.me.untitledwestern.engine.util.BufferUtil

class BoundingBox2D {
    private val AABB_POINTS_2D = 4
    var minpoint: Vector2D
    var maxpoint: Vector2D

    var bbPoints: Array<Vector2D>
    private var boxHalfWidth: Float
    private var boxHalfHeight: Float

    private val transformationMatrix: FloatArray
    private val rotationMatrix: FloatArray

    var mEnabled: Boolean

    var color = floatArrayOf(0.9f, 0.1f, 0.0f, 1.0f)

    val COORDS_PER_VERTEX = 3
    private val VertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    constructor(newMinPoint: Vector2D, newMaxPoint: Vector2D){
        boxHalfWidth = 0f
        boxHalfHeight = 0f

        mEnabled = true

        bbPoints = Array(AABB_POINTS_2D) {Vector2D(0f, 0f)}
        /*
        for (i in 0 until bbPoints.size){
            bbPoints[i] = Vector2D(0f, 0f)
        }

         */

        transformationMatrix = FloatArray(16)
        rotationMatrix = FloatArray(16)

        minpoint = Vector2D(newMinPoint.x, newMinPoint.y)
        maxpoint = Vector2D(newMaxPoint.x, newMaxPoint.y)

        setUpBBPoints()
        searchMinMax()

        boxHalfWidth = (maxpoint.x - minpoint.x) / 2.0f
        boxHalfHeight = (maxpoint.y - minpoint.y) / 2.0f
    }

    fun setPoints(min: Vector2D, max: Vector2D){
        minpoint.set(min.x, min.y)
        maxpoint.set(max.x, max.y)

        setUpBBPoints()
        searchMinMax()

        boxHalfWidth = (maxpoint.x - minpoint.x) / 2.0f
        boxHalfHeight = (maxpoint.y - minpoint.y) / 2.0f
    }

    private fun setUpBBPoints() {
        bbPoints[0].set(minpoint.x, minpoint.y)

        bbPoints[1].set(maxpoint.x, minpoint.y)

        bbPoints[2].set(maxpoint.x, maxpoint.y)

        bbPoints[3].set(minpoint.x, maxpoint.y)
    }

    private fun searchMinMax() {
        var min = Vector2D(bbPoints[0].x, bbPoints[0].y)
        var max = Vector2D(bbPoints[0].x, bbPoints[0].y)

        for (i in 0 until AABB_POINTS_2D){
            if (bbPoints[i].x < min.x) {
                min.x = bbPoints[i].x
            }
            if (bbPoints[i].y < min.y) {
                min.y = bbPoints[i].y
            }

            if (bbPoints[i].x > max.x) {
                max.x = bbPoints[i].x
            }
            if (bbPoints[i].y > max.y) {
                max.y = bbPoints[i].y
            }
        }

        minpoint.set(min.x, min.y)

        maxpoint.set(max.x, max.y)
    }

    fun transformByScale(scale: Float) {
        Matrix.setIdentityM(transformationMatrix, 0)
        Matrix.scaleM(transformationMatrix, 0, scale, scale, scale)

        for (i in 0 until AABB_POINTS_2D){
            transformPoint(bbPoints[i])
        }
        searchMinMax()
        setUpBBPoints()
    }

    fun transformPoint(vec: Vector2D){
        var x = vec.x
        var y = vec.y

        vec.x = x * transformationMatrix[0] + y * transformationMatrix[4] + transformationMatrix[12]
        vec.y = x * transformationMatrix[1] + y * transformationMatrix[5] + transformationMatrix[13]
    }

    fun transformByTranslate(translateVector: Vector2D) {
        Matrix.setIdentityM(transformationMatrix, 0)
        Matrix.translateM(transformationMatrix, 0, translateVector.x, translateVector.y, 0f)

        for (i in 0 until AABB_POINTS_2D){
            transformPoint(bbPoints[i])
        }
        searchMinMax()
        setUpBBPoints()

    }

    fun transformByRotate(rotationAngle: Float) {
        Matrix.setIdentityM(transformationMatrix, 0)
        var x = boxHalfWidth
        var y = boxHalfHeight
        Matrix.translateM(transformationMatrix, 0, x, y, 0f)
        Matrix.setRotateM(rotationMatrix, 0, rotationAngle, 0f, 0f, -1.0f)
        Matrix.multiplyMM(transformationMatrix, 0, transformationMatrix, 0, rotationMatrix, 0)
        Matrix.translateM(transformationMatrix, 0, -x, -y, 0f)

        for (i in 0 until AABB_POINTS_2D){
            transformPoint(bbPoints[i])
        }
        searchMinMax()
        setUpBBPoints()
    }

    fun draw(renderer: MyGLRenderer) {
        // TODO: Try out the bounding box!!!
        if (!mEnabled){
            return
        }

        var vertices = floatArrayOf(
            minpoint.x, minpoint.y, 0.0f,
            minpoint.x, maxpoint.y, 0.0f,
            maxpoint.x, maxpoint.y, 0.0f,
            maxpoint.x, minpoint.y, 0.0f,
            minpoint.x, minpoint.y, 0.0f
        )

        renderer.lineShader.bind()

        renderer.lineShader.setUniform("projectionMatrix", renderer.projectionMatrix)
        renderer.lineShader.setUniform("modelMatrix", renderer.viewMatrix)
        renderer.lineShader.setUniform4fv("vColor", color)

        var posAttrib = GLES30.glGetAttribLocation(renderer.lineShader.programId, "vPosition")
        GLES30.glEnableVertexAttribArray(posAttrib)
        var positionBuffer = BufferUtil.createFloatBuffer(vertices)
        GLES30.glVertexAttribPointer(
            posAttrib, Line.COORDS_PER_VERTEX,
            GLES30.GL_FLOAT, false,
            VertexStride, positionBuffer
        )

        GLES30.glEnableVertexAttribArray(posAttrib)
        GLES30.glLineWidth(4.0f)
        GLES30.glDrawArrays(GLES30.GL_LINE_STRIP, 0, 5)
        GLES30.glDisableVertexAttribArray(posAttrib)

        renderer.lineShader.unbind()

        /*
        minpoint = Vector2D(0f, 0f)
        maxpoint = Vector2D(0.5f, 0.5f)
        var vertices = floatArrayOf(
            minpoint.x, minpoint.y, 0.0f,
            minpoint.x, maxpoint.y, 0.0f,
            maxpoint.x, maxpoint.y, 0.0f,
            maxpoint.x, minpoint.y, 0.0f,
            minpoint.x, minpoint.y, 0.0f
        )


        var vaos = IntArray(1)
        GLES30.glGenVertexArrays(1, vaos, 0)
        GLES30.glBindVertexArray(vaos[0])

        var vboID = IntArray(1)
        GLES30.glGenBuffers(1, vboID, 0)
        var positionBuffer = BufferUtil.createFloatBuffer(vertices)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vboID[0])
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, positionBuffer.capacity() * 4, positionBuffer, GLES30.GL_STATIC_DRAW)

        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, 0)
        // Don't know why, but this line solved the line-drawing problem??
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)

        shaderProgram.bind()

        shaderProgram.setUniform("projectionMatrix", projectionMatrix)

        var modelMatrix = FloatArray(16)
        //Matrix.setIdentityM(modelMatrix, 0)
        shaderProgram.setUniform("modelMatrix", modelMatrix)

        shaderProgram.setUniform4f("vColor", floatArrayOf(0.8f, 0.4f, 0.4f, 1.0f))

        var posAttrib = GLES30.glGetAttribLocation(shaderProgram.programId, "vPosition")
        // render the VAO
        GLES30.glBindVertexArray(vaos[0])
        GLES30.glEnableVertexAttribArray(posAttrib)

        GLES30.glDrawArrays(GLES30.GL_LINE_STRIP, 0, 5)

        GLES30.glDisableVertexAttribArray(posAttrib)
        GLES30.glBindVertexArray(0)

        shaderProgram.unbind()

         */
    }

    fun checkOverlapping(box: BoundingBox2D): Boolean {
        if (maxpoint.x < box.minpoint.x || minpoint.x > box.maxpoint.x) {
            return false
        }
        if (maxpoint.y < box.minpoint.y || minpoint.y > box.maxpoint.y) {
            return false
        }
        return true
    }
}