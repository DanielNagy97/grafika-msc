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

    lateinit var skyLayer: C2DGraphicsLayer
    lateinit var mountainLayer: C2DGraphicsLayer
    lateinit var rockLayer: C2DGraphicsLayer
    lateinit var duneLayer: C2DGraphicsLayer
    lateinit var groundLayer: C2DGraphicsLayer
    lateinit var groundLayer2: C2DGraphicsLayer
    lateinit var groundLayer3: C2DGraphicsLayer
    lateinit var groundLayer4: C2DGraphicsLayer
    lateinit var groundLayer5: C2DGraphicsLayer
    lateinit var groundLayer6: C2DGraphicsLayer
    lateinit var groundLayer7: C2DGraphicsLayer
    lateinit var groundLayer8: C2DGraphicsLayer
    lateinit var gameLayer: C2DGraphicsLayer

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

        mPlayerObject = GameObject(context, -70.0f, -20.0f, scale)
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

        bboxtest = BoundingBox2D(Vector2D(-100f, -82f), Vector2D(50f, -75f))

        var horizont = -21f

        var sky = GameObject(context, -200f, horizont, scale)
        sky.addSprite("sprites/sky/sky1.png", 1, 0)
        skyLayer = C2DGraphicsLayer("sky-layer", 0)
        skyLayer.addGameObject(sky)
        skyLayer.setCamera(CCamera2D(0f, 0f, 6))


        var mountains1 = GameObject(context, -90f, horizont, scale)
        mountains1.addSprite("sprites/mountains/mountains2.png", 1, 0)

        var mountains2 = GameObject(context, 10f, horizont, scale)
        mountains2.addSprite("sprites/mountains/mountains3.png", 1, 0)
        mountainLayer = C2DGraphicsLayer("mountain-layer", 0)
        mountainLayer.addGameObject(mountains1)
        mountainLayer.addGameObject(mountains2)
        mountainLayer.setCamera(CCamera2D(0f, 0f, 5))

        var rocks = GameObject(context, 80f, horizont, scale)
        rocks.addSprite("sprites/rocks/rocks2.png", 1, 0)
        rockLayer = C2DGraphicsLayer("rocks-layer", 0)
        rockLayer.addGameObject(rocks)
        rockLayer.setCamera(CCamera2D(0f, 0f, 4))

        var dunes = GameObject(context, -280f, horizont, scale)
        dunes.addSprite("sprites/dunes/dunes1.png", 1, 0)
        duneLayer = C2DGraphicsLayer("dunes-layer", 0)
        duneLayer.addGameObject(dunes)
        duneLayer.setCamera(CCamera2D(0f, 0f, 3))



        var ground8 = GameObject(context, -200f, -82f+14f+24f+11f+5f+3f+2f+1f, scale)
        ground8.addSprite("sprites/ground/ground8.png", 1, 0)
        var ground8b = GameObject(context, -200f-400f, -82f+14f+24f+11f+5f+3f+2f+1f, scale)
        ground8b.addSprite("sprites/ground/ground8.png", 1, 0)
        groundLayer8 = C2DGraphicsLayer("ground-layer4", 0)
        groundLayer8.addGameObject(ground8)
        groundLayer8.addGameObject(ground8b)
        groundLayer8.setCamera(CCamera2D(0f, 0f, 2))


        var ground7 = GameObject(context, -200f, -82f+14f+24f+11f+5f+3f+2f, scale)
        ground7.addSprite("sprites/ground/ground7.png", 1, 0)
        var ground7b = GameObject(context, -200f-400f, -82f+14f+24f+11f+5f+3f+2f, scale)
        ground7b.addSprite("sprites/ground/ground7.png", 1, 0)
        groundLayer7 = C2DGraphicsLayer("ground-layer4", 0)
        groundLayer7.addGameObject(ground7)
        groundLayer7.addGameObject(ground7b)
        groundLayer7.setCamera(CCamera2D(0f, 0f, 2))


        var ground6 = GameObject(context, -200f, -82f+14f+24f+11f+5f+3f, scale)
        ground6.addSprite("sprites/ground/ground6.png", 1, 0)
        var ground6b = GameObject(context, -200f-400f, -82f+14f+24f+11f+5f+3f, scale)
        ground6b.addSprite("sprites/ground/ground6.png", 1, 0)
        groundLayer6 = C2DGraphicsLayer("ground-layer4", 0)
        groundLayer6.addGameObject(ground6)
        groundLayer6.addGameObject(ground6b)
        groundLayer6.setCamera(CCamera2D(0f, 0f, 2))


        var ground5 = GameObject(context, -200f, -82f+14f+24f+11f+5f, scale)
        ground5.addSprite("sprites/ground/ground5.png", 1, 0)
        var ground5b = GameObject(context, -200f-400f, -82f+14f+24f+11f+5f, scale)
        ground5b.addSprite("sprites/ground/ground5.png", 1, 0)
        groundLayer5 = C2DGraphicsLayer("ground-layer4", 0)
        groundLayer5.addGameObject(ground5)
        groundLayer5.addGameObject(ground5b)
        groundLayer5.setCamera(CCamera2D(0f, 0f, 2))


        var ground4 = GameObject(context, -200f, -82f+14f+24f+11f, scale)
        ground4.addSprite("sprites/ground/ground4.png", 1, 0)
        var ground4b = GameObject(context, -200f-400f, -82f+14f+24f+11f, scale)
        ground4b.addSprite("sprites/ground/ground4.png", 1, 0)
        groundLayer4 = C2DGraphicsLayer("ground-layer4", 0)
        groundLayer4.addGameObject(ground4)
        groundLayer4.addGameObject(ground4b)
        groundLayer4.setCamera(CCamera2D(0f, 0f, 2))


        var ground3 = GameObject(context, -200f, -82f+14f+24f, scale)
        ground3.addSprite("sprites/ground/ground3.png", 1, 0)
        var ground3b = GameObject(context, -200f-400f, -82f+14f+24f, scale)
        ground3b.addSprite("sprites/ground/ground3.png", 1, 0)
        groundLayer3 = C2DGraphicsLayer("ground-layer3", 0)
        groundLayer3.addGameObject(ground3)
        groundLayer3.addGameObject(ground3b)
        groundLayer3.setCamera(CCamera2D(0f, 0f, 2))


        var ground2 = GameObject(context, -200f, -82f+14f, scale)
        ground2.addSprite("sprites/ground/ground2.png", 1, 0)
        var ground2b = GameObject(context, -200f-400f, -82f+14f, scale)
        ground2b.addSprite("sprites/ground/ground2.png", 1, 0)

        var vulture = GameObject(context, -120f, -60f, scale)
        vulture.addSprite("sprites/vulture", 8, 3)

        groundLayer2 = C2DGraphicsLayer("ground-layer2", 0)
        groundLayer2.addGameObject(ground2)
        groundLayer2.addGameObject(ground2b)
        groundLayer2.addGameObject(vulture)
        groundLayer2.setCamera(CCamera2D(0f, 0f, 2))


        var gameGround = GameObject(context, -200f, -82f, scale)
        gameGround.addSprite("sprites/ground/ground1.png", 1, 0)
        var gameGroundb = GameObject(context, -200f-400f, -82f, scale)
        gameGroundb.addSprite("sprites/ground/ground1.png", 1, 0)

        gameLayer = C2DGraphicsLayer("game-layer", 1)
        gameLayer.addGameObject(gameGround)
        gameLayer.addGameObject(gameGroundb)
        gameLayer.addGameObject(mPlayerObject)
        gameLayer.addGameObject(mPistolObject)
        gameLayer.setCamera(CCamera2D(0f, 0f, 1))

        scene.registerLayer(skyLayer)
        scene.registerLayer(mountainLayer)
        scene.registerLayer(rockLayer)
        scene.registerLayer(duneLayer)

        scene.registerLayer(groundLayer8)
        scene.registerLayer(groundLayer7)
        scene.registerLayer(groundLayer6)
        scene.registerLayer(groundLayer5)
        scene.registerLayer(groundLayer4)
        scene.registerLayer(groundLayer3)
        scene.registerLayer(groundLayer2)

        scene.registerLayer(gameLayer)

        sceneManager.registerScene(scene)
    }

    private fun updateCameras(){

        mountainLayer.mCamera!!.moveLeft(xdir * speedX * 0.05f)
        rockLayer.mCamera!!.moveLeft(xdir * speedX * 0.09f)
        duneLayer.mCamera!!.moveLeft(xdir * speedX * 0.1f)
        groundLayer8.mCamera!!.moveLeft(xdir * speedX * 0.2f)
        groundLayer7.mCamera!!.moveLeft(xdir * speedX * 0.3f)
        groundLayer6.mCamera!!.moveLeft(xdir * speedX * 0.4f)
        groundLayer5.mCamera!!.moveLeft(xdir * speedX * 0.5f)
        groundLayer4.mCamera!!.moveLeft(xdir * speedX * 0.6f)
        groundLayer3.mCamera!!.moveLeft(xdir * speedX * 0.7f)
        groundLayer2.mCamera!!.moveLeft(xdir * speedX * 0.8f)
        gameLayer.mCamera!!.mPosition = Vector2D(mPlayerObject.position.x+80f, 0f)
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
            if (mPlayerObject.position.y < -75f) {
                falling = false
                speedY = 0f
                mPlayerObject.position.y = -75f
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

/*
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

 */


        if(xdir == 1f){
            mPistolObject.position =
                Vector2D(mPlayerObject.position.x + (mPlayerObject.getBoundingBox().maxpoint.x-mPlayerObject.getBoundingBox().minpoint.x), mPlayerObject.position.y + (mPlayerObject.getBoundingBox().maxpoint.y-mPlayerObject.getBoundingBox().minpoint.y)/3)
        }
        else{
            mPistolObject.position =
                Vector2D(mPlayerObject.position.x - (mPistolObject.getBoundingBox().maxpoint.x-mPistolObject.getBoundingBox().minpoint.x), mPlayerObject.position.y + (mPlayerObject.getBoundingBox().maxpoint.y-mPlayerObject.getBoundingBox().minpoint.y)/3)
        }

        updateCameras()
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