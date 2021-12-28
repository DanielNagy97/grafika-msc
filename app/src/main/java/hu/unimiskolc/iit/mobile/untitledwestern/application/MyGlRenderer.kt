package hu.unimiskolc.iit.mobile.untitledwestern.application

import android.content.Context
import android.opengl.GLES32.glClear
import android.opengl.GLES32.glViewport
import android.opengl.GLES32.glClearColor
import android.opengl.GLES32.GL_COLOR_BUFFER_BIT
import android.opengl.GLES32.GL_DEPTH_BUFFER_BIT
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.Timer
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.graph.ShaderProgram
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.util.TextUtil
import hu.unimiskolc.iit.mobile.untitledwestern.application.fragment.MainGameFragment
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer(private val context: Context) : GLSurfaceView.Renderer {
    lateinit var shaderProgram: ShaderProgram
    lateinit var lineShader: ShaderProgram

    val projectionMatrix = FloatArray(16)
    var viewMatrix = FloatArray(16)

    var ratio: Float = 16f/9f
    var timer: Timer = Timer()

    var dummygame = DummyGame(context, this, 1f)

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        shaderProgram = ShaderProgram()
        shaderProgram.createVertexShader(TextUtil.readFile(context, "shaders/vertexShader.vert"))
        shaderProgram.createFragmentShader(TextUtil.readFile(context, "shaders/fragmentShader.frag"))
        shaderProgram.bindAttribLocation("a_TexCoordinate", 0)
        shaderProgram.link()
        shaderProgram.createUniform("viewMatrix")
        shaderProgram.createUniform("worldMatrix")
        shaderProgram.createUniform("projectionMatrix")
        shaderProgram.createUniform("vColor")
        shaderProgram.createUniform("u_Texture")

        lineShader = ShaderProgram()
        lineShader.createVertexShader(TextUtil.readFile(context, "shaders/lineVertex.vert"))
        lineShader.createFragmentShader(TextUtil.readFile(context, "shaders/lineFragment.frag"))
        lineShader.link()
        lineShader.createUniform("projectionMatrix")
        lineShader.createUniform("modelMatrix")
        lineShader.createUniform("vColor")
        dummygame.init()
    }

    override fun onDrawFrame(gl: GL10) {
        val dt: Float = timer.getElapsedTime()
        dummygame.update(dt)

        glClear(GL_COLOR_BUFFER_BIT)
        glClear(GL_DEPTH_BUFFER_BIT)
        dummygame.sceneManager.render(this)
       //dummygame.hub.hubLayer.mCamera!!.viewPort.draw(this)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        glViewport(0, 0, width, height)
        ratio = width.toFloat() / height.toFloat()

        for(layer in dummygame.sceneManager.mScenes[0].mLayers) {
            layer.mCamera!!.aspect = ratio
            layer.mCamera!!.recalculateViewPort()
            dummygame.hub.calculateLayout()
        }

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 10000f)
    }

    fun cleanup() {
        shaderProgram.cleanup()
        lineShader.cleanup()
    }
}