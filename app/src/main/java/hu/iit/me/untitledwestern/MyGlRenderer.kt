package hu.iit.me.untitledwestern

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import hu.iit.me.untitledwestern.engine.BoundingBox2D
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import hu.iit.me.untitledwestern.engine.graph.ShaderProgram
import hu.iit.me.untitledwestern.engine.GameObject
import hu.iit.me.untitledwestern.engine.Line
import hu.iit.me.untitledwestern.engine.math.Vector2D
import hu.iit.me.untitledwestern.engine.util.TextUtil

class MyGLRenderer (private val context: Context): GLSurfaceView.Renderer{
    lateinit var shaderProgram: ShaderProgram
    lateinit var lineShader: ShaderProgram

    val projectionMatrix = FloatArray(16)
    val viewMatrix = FloatArray(16)

    lateinit var mBackground: GameObject
    lateinit var mPlayerObject: GameObject
    lateinit var mPistolObject: GameObject

    lateinit var mControlPad: GameObject

    lateinit var bboxtest: BoundingBox2D

    lateinit var vertLine: Line

    init{
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 6f, 0f, 0f, 0f, 0f, 1f, 0f)
    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

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

        vertexShaderCode = TextUtil.readFile(context, "shaders/lineVertex.txt")
        fragmentShaderCode = TextUtil.readFile(context, "shaders/lineFragment.txt")
        lineShader = ShaderProgram()
        lineShader.createVertexShader(vertexShaderCode)
        lineShader.createFragmentShader(fragmentShaderCode)
        lineShader.link()
        lineShader.createUniform("projectionMatrix")
        lineShader.createUniform("modelMatrix")
        lineShader.createUniform("vColor")

        var scale = 0.03f

        mControlPad = GameObject(context, -3.2f, -1.9f, scale)
        mControlPad.addSprite("sprites/control/pad/pad_background.png", 1, 0)

        mPlayerObject = GameObject(context, -1.0f, -1.0f, scale)
        mPlayerObject.addSprite("sprites/hero/idle", 5, 8)
        mPlayerObject.addSprite("sprites/hero/walk", 8, 12)

        mPistolObject = GameObject(context, -1.0f, -1.0f, scale)
        mPistolObject.addSprite("sprites/hero/pistol/pistol1.png", 1, 0)
        mPistolObject.addSprite("sprites/hero/pistol", 5, 12)

        mBackground = GameObject(context, -4.0f, -4.0f, 0.02f)
        mBackground.addSprite("sprites/background/background.png", 1, 0)

        bboxtest = BoundingBox2D(Vector2D(0f,0f), Vector2D(0.5f, 0.5f))
        vertLine = Line()
    }

    override fun onDrawFrame(gl: GL10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        mBackground.draw(this)
        mPlayerObject.draw(this)

        // TODO: Make an object that handles this! Like a character object or something...
        mPistolObject.position = Vector2D( mPlayerObject.position.x+1f, mPlayerObject.position.y+0.40f)
        mPistolObject.draw(this)

        mControlPad.draw(this)

        //bboxtest.draw(this)

        mPlayerObject.currSprite = 0

        if (mPistolObject.currSprite == 1 && mPistolObject.mSprites[1].miActualFrame == 0){
            mPistolObject.mSprites[1].miActualFrame = 0 // TODO: reset sprite
            mPistolObject.currSprite = 0
        }

        //vertLine.draw(this)
    }


    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }
}