package hu.iit.me.untitledwestern

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
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

    var walking = false
    var shooting = false
    var jumping = false
    var falling = false
    var velocityX = 0f
    var velocityY = 0f
    var gForce = 1f
    var xdir = 1f
    var ydir = -1f

    init{
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 80f, 0f, 0f, 0f, 0f, 1f, 0f)
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

        var scale = 1f

        mControlPad = GameObject(context, -130f, -70f, scale)
        mControlPad.addSprite("sprites/control/pad/pad_background.png", 1, 0)

        mPlayerObject = GameObject(context, -50.0f, -20.0f, scale)
        mPlayerObject.addSprite("sprites/hero/idle", 5, 8)
        mPlayerObject.addSprite("sprites/hero/walk", 8, 12)
        mPlayerObject.addSprite("sprites/hero/jump", 3, 2)
        mPlayerObject.addSprite("sprites/hero/fall", 3, 2)

        mPistolObject = GameObject(context, mPlayerObject.position.x+30f, mPlayerObject.position.y+15.0f, scale)

        mPistolObject.addSprite("sprites/hero/pistol/pistol1.png", 1, 0)
        mPistolObject.addSprite("sprites/hero/pistol", 5, 12)

        mBackground = GameObject(context, -200f, -200f, scale)
        mBackground.addSprite("sprites/background/background.png", 1, 0)

        bboxtest = BoundingBox2D(Vector2D(-100f,-80f), Vector2D(60f, -60f))
        vertLine = Line()
    }

    override fun onDrawFrame(gl: GL10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        // TODO: Make an object that handles this! Like a character object or something...

        //mPlayerObject.position.y += -4f


        mPlayerObject.position.x += velocityX * xdir
        mPlayerObject.position.y += (velocityY * ydir) - gForce


        when {
            walking -> {
                mPlayerObject.currSprite = 1
                falling = false
                jumping = false
            }
            jumping -> {
                velocityY *= 0.9f
                mPlayerObject.currSprite = 2
                // This is very bad...
                if(mPlayerObject.mSprites[2].miActualFrame == mPlayerObject.mSprites[2].mvFrames.size-1){
                    falling = true
                    jumping = false
                    ydir = -1f
                }
                walking = false
            }
            falling -> {
                velocityY *= 1.1f
                mPlayerObject.currSprite = 3
                // This is very bad...
                if(mPlayerObject.mSprites[3].miActualFrame == mPlayerObject.mSprites[3].mvFrames.size-1){
                    falling = false
                    velocityY = 0f
                }
                walking = false
            }
            else -> {
                mPlayerObject.mSprites[1].miActualFrame = 0
                mPlayerObject.mSprites[2].miActualFrame = 0
                mPlayerObject.mSprites[3].miActualFrame = 0
                mPlayerObject.currSprite = 0
                velocityX = 0.0f
            }
        }

        if(shooting){
            mPistolObject.currSprite = 1
            if(mPistolObject.mSprites[1].miActualFrame == 0){
                shooting = false
            }
        }
        else{
            // TODO: Make a function that plays animation only once and a resetter!!
            mPistolObject.mSprites[1].miActualFrame = 1
            mPistolObject.currSprite = 0
        }


        if(mPlayerObject.getBoundingBox().checkOverlapping(bboxtest)){
            mPlayerObject.position.y = bboxtest.maxpoint.y
        }
        if(mPlayerObject.position.y < -80f){
            mPlayerObject.position.y = -80f
        }

        mPistolObject.position = Vector2D( mPlayerObject.position.x+30f, mPlayerObject.position.y+15.0f)

        mBackground.draw(this)
        mPlayerObject.draw(this)

        mPistolObject.draw(this)
        mControlPad.draw(this)

        bboxtest.draw(this)
    }


    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 10000f)
    }
}