package hu.iit.me.untitledwestern

import android.content.Context
import hu.iit.me.untitledwestern.engine.*
import hu.iit.me.untitledwestern.engine.math.Vector2D

class DummyGame {
    var renderer: MyGLRenderer
    var context: Context

    var sceneManager: C2DSceneManager
    var scene: C2DScene


    lateinit var mPlayerObject: GameObject
    lateinit var mPistolObject: GameObject

    lateinit var bboxtest: BoundingBox2D

    var idle = true
    var shooting = false
    var jumping = false
    var falling = true
    var velocity = 1.5f

    var speedX = 0f
    var speedY = 0f
    var xdir = 1f
    var ydir = -1f

    constructor(context: Context, renderer:MyGLRenderer){
        this.renderer = renderer
        this.context = context

        sceneManager = C2DSceneManager()
        scene = C2DScene()
    }

    fun init(){
        var scale = 1f

        var mControlPad = GameObject(context, -130f, -70f, scale)
        mControlPad.addSprite("sprites/control/pad/pad_background.png", 1, 0)

        mPlayerObject = GameObject(context, -50.0f, -20.0f, scale)
        mPlayerObject.addSprite("sprites/hero/idle", 5, 8)
        mPlayerObject.addSprite("sprites/hero/walk", 8, 12)
        mPlayerObject.addSprite("sprites/hero/jump/jump3.png", 1, 0)
        mPlayerObject.addSprite("sprites/hero/fall/fall3.png", 1, 0)

        mPistolObject = GameObject(
            context,
            mPlayerObject.position.x + 30f,
            mPlayerObject.position.y + 15.0f,
            scale
        )

        mPistolObject.addSprite("sprites/hero/pistol/pistol1.png", 1, 0)
        mPistolObject.addSprite("sprites/hero/pistol", 6, 20)

        var mBackground = GameObject(context, -200f, -200f, scale)
        mBackground.addSprite("sprites/background/background.png", 1, 0)

        bboxtest = BoundingBox2D(Vector2D(-100f, -80f), Vector2D(50f, -40f))

        var layer0: C2DGraphicsLayer = C2DGraphicsLayer("background-layer", 0)
        layer0.addGameObject(mBackground)

        var layer1: C2DGraphicsLayer = C2DGraphicsLayer("game-layer", 1)
        layer1.addGameObject(mPlayerObject)
        layer1.addGameObject(mPistolObject)

        var layer2: C2DGraphicsLayer = C2DGraphicsLayer("input-layer", 2)
        layer2.addGameObject(mControlPad)

        scene.registerLayer(layer0)
        scene.registerLayer(layer1)
        scene.registerLayer(layer2)

        sceneManager.registerScene(scene)
    }

    fun updatePositions(){
        // Position handling
        mPlayerObject.position.x += xdir * speedX
        mPlayerObject.position.y += ydir * speedY

        if (falling) {
            if (speedY == 0f) {
                speedY = 0.5f
            }

            //max speed
            if (speedY < 3.5f) {
                speedY *= 1.2f
            } else {
                speedY = 3.5f
            }

            //checking ground
            if (mPlayerObject.position.y < -80f) {
                falling = false
                speedY = 0f
                mPlayerObject.position.y = -80f
                if (speedX > 0) {
                    idle = false
                }
            }
        } else if (jumping) {
            if (speedY <= 0f) {
                ydir = 1f
                speedY = 5f
            } else if (speedY >= 0.3f && ydir == 1f) {
                speedY *= 0.9f
            } else if (speedY < 0.3f) {
                //falling begins
                jumping = false
                falling = true
                ydir = -1f
            }
        }


        if (mPlayerObject.getBoundingBox().checkOverlapping(bboxtest)) {
            mPlayerObject.position.y = bboxtest.maxpoint.y
            if (falling) {
                falling = false
                speedY = 0f
                if (speedX > 0) {
                    idle = false
                }
            }
        } else {
            if (!falling && mPlayerObject.position.y == bboxtest.maxpoint.y) {
                falling = true
            }
        }


        if(xdir == 1f){
            mPistolObject.position =
                Vector2D(mPlayerObject.position.x + (mPlayerObject.getBoundingBox().maxpoint.x-mPlayerObject.getBoundingBox().minpoint.x), mPlayerObject.position.y + (mPlayerObject.getBoundingBox().maxpoint.y-mPlayerObject.getBoundingBox().minpoint.y)/3)
        }
        else{
            mPistolObject.position =
                Vector2D(mPlayerObject.position.x - (mPistolObject.getBoundingBox().maxpoint.x-mPistolObject.getBoundingBox().minpoint.x), mPlayerObject.position.y + (mPlayerObject.getBoundingBox().maxpoint.y-mPlayerObject.getBoundingBox().minpoint.y)/3)
        }
    }

    fun updateAnimations(){
        // Animation
        if (falling) {
            mPlayerObject.currSprite = 3
        } else if (jumping) {
            mPlayerObject.currSprite = 2
        } else if (idle) {
            mPlayerObject.currSprite = 0
        } else if (!idle) {
            mPlayerObject.currSprite = 1
        }

        mPlayerObject.mSprites[mPlayerObject.currSprite].toFlip = xdir != 1f

        if (shooting) {
            mPistolObject.currSprite = 1
            if (mPistolObject.mSprites[1].miActualFrame == mPistolObject.mSprites[1].mvFrames.size-1) {
                shooting = false
            }
        } else{
            // TODO: Make a function that plays animation only once and a resetter!!
            mPistolObject.mSprites[1].miActualFrame = 0
            mPistolObject.currSprite = 0
        }
        mPistolObject.mSprites[mPistolObject.currSprite].toFlip = xdir != 1f
    }

    fun cleanup() {
        renderer.cleanup()
        mPlayerObject.cleanup()
        mPistolObject.cleanup()

    }
}