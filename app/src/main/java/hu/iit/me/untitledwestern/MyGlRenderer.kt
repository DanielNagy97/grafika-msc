package hu.iit.me.untitledwestern

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import hu.iit.me.untitledwestern.engine.graph.ShaderProgram
import hu.iit.me.untitledwestern.engine.GameObject
import hu.iit.me.untitledwestern.engine.util.TextUtil

class MyGLRenderer (private val context: Context): GLSurfaceView.Renderer{
    lateinit var shaderProgram: ShaderProgram

    val projectionMatrix = FloatArray(16)
    val viewMatrix = FloatArray(16)

    lateinit var mBackground: GameObject
    lateinit var mPlayerObject: GameObject

    init{
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f)
        var vertexShaderCode = TextUtil.readFile(context, "shaders/vertexShader.txt")
        var fragmentShaderCode = TextUtil.readFile(context, "shaders/fragmentShader.txt")

        shaderProgram = ShaderProgram()
        shaderProgram.createVertexShader(vertexShaderCode)
        shaderProgram.createFragmentShader(fragmentShaderCode)
        shaderProgram.bindAttribLocation("a_TexCoordinate", 0)
        shaderProgram.link()
        shaderProgram.createUniform("viewMatrix")
        shaderProgram.createUniform("worldMatrix")
        shaderProgram.createUniform("projectionMatrix")
        shaderProgram.createUniform("vColor")
        shaderProgram.createUniform("u_Texture")

        mPlayerObject = GameObject(context, -0.5f, -0.5f, 0.015f)
        mPlayerObject.addSprite("sprites/hero/idle", 5, 8)
        mPlayerObject.addSprite("sprites/hero/walk", 8, 12)

        mBackground = GameObject(context, -2.0f, -2.0f, 0.01f)
        mBackground.addSprite("sprites/background", 1, 0)
    }

    override fun onDrawFrame(gl: GL10) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        //val time = SystemClock.uptimeMillis() % 4000L
        // mPlayer.mRotationAngle = 0.090f * time.toInt()

        // TODO: Rotate the bitmap!
        mPlayerObject.rotationAngle = 180f
        mBackground.rotationAngle = 180f

        mBackground.draw(this)
        mPlayerObject.draw(this)
        mPlayerObject.currSprite = 0
    }


    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }
}