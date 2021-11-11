package hu.iit.me.untitledwestern

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import hu.iit.me.untitledwestern.engine.graph.ShaderProgram

import hu.iit.me.untitledwestern.engine.util.TextUtil

class MyGLRenderer(private val context: Context) : GLSurfaceView.Renderer {
    lateinit var shaderProgram: ShaderProgram
    lateinit var lineShader: ShaderProgram

    val projectionMatrix = FloatArray(16)
    val viewMatrix = FloatArray(16)

    var dummygame = DummyGame(context, this)

    init {
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 80f, 0f, 0f, 0f, 0f, 1f, 0f)
    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        shaderProgram = ShaderProgram()
        shaderProgram.createVertexShader(TextUtil.readFile(context, "shaders/vertexShader.txt"))
        shaderProgram.createFragmentShader(TextUtil.readFile(context, "shaders/fragmentShader.txt"))
        shaderProgram.bindAttribLocation("a_TexCoordinate", 0)
        shaderProgram.link()
        shaderProgram.createUniform("viewMatrix")
        shaderProgram.createUniform("worldMatrix")
        shaderProgram.createUniform("projectionMatrix")
        shaderProgram.createUniform("vColor")
        shaderProgram.createUniform("u_Texture")

        lineShader = ShaderProgram()
        lineShader.createVertexShader(TextUtil.readFile(context, "shaders/lineVertex.txt"))
        lineShader.createFragmentShader(TextUtil.readFile(context, "shaders/lineFragment.txt"))
        lineShader.link()
        lineShader.createUniform("projectionMatrix")
        lineShader.createUniform("modelMatrix")
        lineShader.createUniform("vColor")

        dummygame.init()
    }

    override fun onDrawFrame(gl: GL10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        //TODO: Make a better gameloop!!
        dummygame.updatePositions()
        dummygame.updateAnimations()

        dummygame.sceneManager.render(this)

        dummygame.bboxtest.draw(this)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 10000f)
    }

    fun cleanup() {
        if(shaderProgram != null){
            shaderProgram.cleanup()
        }
        if(lineShader != null){
            lineShader.cleanup()
        }
    }

}