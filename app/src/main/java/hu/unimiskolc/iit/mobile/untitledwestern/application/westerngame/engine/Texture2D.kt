package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine

import android.graphics.Bitmap
import android.opengl.GLES32.glBindTexture
import android.opengl.GLES32.glPixelStorei
import android.opengl.GLES32.glGenTextures
import android.opengl.GLES32.glEnable
import android.opengl.GLES32.glBlendFunc
import android.opengl.GLES32.glTexParameteri
import android.opengl.GLES32.glDeleteTextures
import android.opengl.GLES32.GL_TEXTURE_2D
import android.opengl.GLES32.GL_UNPACK_ALIGNMENT
import android.opengl.GLES32.GL_BLEND
import android.opengl.GLES32.GL_SRC_ALPHA
import android.opengl.GLES32.GL_ONE_MINUS_SRC_ALPHA
import android.opengl.GLES32.GL_TEXTURE_MIN_FILTER
import android.opengl.GLES32.GL_NEAREST
import android.opengl.GLES32.GL_TEXTURE_MAG_FILTER
import android.opengl.GLUtils.texImage2D
import android.opengl.Matrix
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.Renderer
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.graph.Mesh
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.math.Vector2D

class Texture2D {
    private lateinit var mesh: Mesh
    var width: Int
    var height: Int
    private val transformationMatrix: FloatArray
    private val rotationMatrix: FloatArray
    var textureId: Int
    private var position: Vector2D
    private var scale: Float
    private var rotationAngle: Float
    private var toFlip: Boolean
    var color = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)

    init{
        textureId = -1
        scale = 1f
        width = 0
        height = 0
        position = Vector2D(0.0f, 0.0f)
        rotationAngle = 0f
        toFlip = true
        transformationMatrix = FloatArray(16)
        rotationMatrix = FloatArray(16)
    }

    private fun bind(){
        glBindTexture(GL_TEXTURE_2D, textureId)
    }

    fun createTexture(bitmap: Bitmap): Boolean{
        loadTexture(bitmap)

        val positions = floatArrayOf(
            0.0f, height.toFloat(), 0.0f,
            width.toFloat(), height.toFloat(), 0.0f,
            width.toFloat(), 0.0f, 0.0f,
            width.toFloat(), 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f,
            0.0f, height.toFloat(), 0.0f,
        )

        val textCoords =  floatArrayOf(
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f
        )

        mesh = Mesh(positions, textCoords, this, 6)
        return true
    }

    private fun loadTexture(bitmap: Bitmap){
        width = bitmap.width
        height = bitmap.height

        val textures = IntArray(1)
        glGenTextures(1, textures, 0)
        textureId = textures[0]
        bind()

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)

        // Enable alpha
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        // Set filtering
        glTexParameteri(
            GL_TEXTURE_2D,
            GL_TEXTURE_MIN_FILTER,
            GL_NEAREST
        )
        glTexParameteri(
            GL_TEXTURE_2D,
            GL_TEXTURE_MAG_FILTER,
            GL_NEAREST
        )

        // Load the bitmap into the bound texture.
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)

        // Recycle the bitmap, since its data has been loaded into OpenGL.
        bitmap.recycle()
    }

    private fun flipOnYAxis(){
        Matrix.translateM(transformationMatrix, 0, width.toFloat(), 0.0f, 0f)
        Matrix.scaleM(transformationMatrix, 0, -1.0f, 1.0f, 1.0f)
    }

    private fun getWorldMatrix(): FloatArray {
        Matrix.setIdentityM(transformationMatrix, 0)
        Matrix.translateM(transformationMatrix, 0, position.x, position.y, 0f)
        Matrix.scaleM(transformationMatrix, 0, scale, scale, scale)

        if(toFlip){
            flipOnYAxis()
        }

        val x = 0.5f * width
        val y = 0.5f * height
        Matrix.translateM(transformationMatrix, 0, x, y, 0f)

        Matrix.setRotateM(rotationMatrix, 0, rotationAngle, 0f, 0f, -1.0f)
        Matrix.multiplyMM(transformationMatrix, 0, transformationMatrix, 0, rotationMatrix, 0)
        Matrix.translateM(transformationMatrix, 0, -x, -y, 0f)

        return transformationMatrix
    }

    fun draw(renderer: Renderer){
        renderer.shaderProgram.bind()

        renderer.shaderProgram.setUniform("projectionMatrix", renderer.projectionMatrix)
        renderer.shaderProgram.setUniform("u_Texture", 0)
        renderer.shaderProgram.setUniform4fv("vColor", color)
        renderer.shaderProgram.setUniform("worldMatrix", getWorldMatrix())
        renderer.shaderProgram.setUniform("viewMatrix", renderer.viewMatrix)

        mesh.draw(renderer.shaderProgram)

        renderer.shaderProgram.unbind()
    }

    fun draw(renderer: Renderer, position: Vector2D, scale:Float, rotationAngle:Float, flip:Boolean, color: FloatArray){
        this.position = position
        this.scale = scale
        this.rotationAngle = rotationAngle
        this.toFlip = flip
        this.color = color

        this.draw(renderer)
    }

    fun cleanup(){
        val names = IntArray(1)
        names[0] = textureId
        glDeleteTextures(1, names, 0)

        mesh.cleanup()


    }
}