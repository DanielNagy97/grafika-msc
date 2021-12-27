package hu.unimiskolc.iit.mobile.untitledwestern.application

import android.content.Context
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.C2DSceneManager
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.C2DScene
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.GameObject
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.C2DGraphicsLayer
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.math.Vector2D
import hu.unimiskolc.iit.mobile.untitledwestern.application.game.Bandit
import hu.unimiskolc.iit.mobile.untitledwestern.application.game.Collectible
import hu.unimiskolc.iit.mobile.untitledwestern.application.game.Player
import hu.unimiskolc.iit.mobile.untitledwestern.application.game.Hub
import hu.unimiskolc.iit.mobile.untitledwestern.application.game.states.MovementState
import hu.unimiskolc.iit.mobile.untitledwestern.application.game.utils.SceneLoader
import java.util.Collections
import kotlin.collections.ArrayList

class DummyGame(
    private var context: Context,
    var renderer: MyGLRenderer,
    private val scale: Float
    ) {
    var sceneManager: C2DSceneManager = C2DSceneManager()
    private var scene: C2DScene = C2DScene()

    lateinit var mPlayer: Player

    private lateinit var platforms: List<GameObject>
    private lateinit var holes: List<GameObject>
    private lateinit var barrels: List<GameObject>
    private lateinit var mBandit: Bandit

    var collectibles: ArrayList<Collectible> = ArrayList()

    private var ground: Float = -75f
    private var horizon: Float = -21f

    private var layers: ArrayList<C2DGraphicsLayer> = ArrayList()
    private var groundLayers: ArrayList<C2DGraphicsLayer> = ArrayList()
    private lateinit var gameLayer: C2DGraphicsLayer

    var score: Int = 0
    lateinit var hub: Hub

    var gameCameraLastXPos = 0f
    private val gameCameraBaseOffset: Float = 100f
    private var gameCameraOffset: Float = gameCameraBaseOffset

    fun init(){
        var sceneLoader = SceneLoader("scenes/scene01.json", context, scale, horizon, renderer.ratio)

        horizon = sceneLoader.loadHorizon()
        ground = sceneLoader.loadGround()

        collectibles = sceneLoader.loadCollectibles()
        platforms = sceneLoader.loadPlatforms()
        holes = sceneLoader.loadHoles()
        barrels = sceneLoader.loadBarrels()

        layers = sceneLoader.loadLayers()

        for (i in 4 until layers.size-1){
            groundLayers.add(layers[i])
        }

        gameLayer = layers[layers.size-2]
        gameCameraLastXPos = gameLayer.mCamera!!.mPosition.x

        hub = Hub(layers[layers.size-1],sceneLoader.loadScoreNumbers(), sceneLoader.loadHearts(), scale)
        mPlayer = sceneLoader.loadPlayer(hub.hearts.size)

        mBandit = sceneLoader.loadBandits(1)

        gameLayer.addGameObjects(holes)
        gameLayer.addGameObjects(platforms)
        gameLayer.addGameObjects(barrels)
        gameLayer.addGameObjects(collectibles)

        gameLayer.addGameObject(mPlayer.body)
        gameLayer.addGameObject(mPlayer.pistol)
        gameLayer.addGameObjects(mPlayer.bullets)

        gameLayer.addGameObject(mBandit.body)
        gameLayer.addGameObject(mBandit.pistol)
        gameLayer.addGameObjects(mBandit.bullets)

        hub.hubLayer.addGameObjects(hub.scoreNumbers)
        hub.hubLayer.addGameObjects(hub.hearts)
        //hub.hubLayer.mCamera!!.viewPort.mEnabled = true

        for(layer in layers){
            scene.registerLayer(layer)
        }
        sceneManager.registerScene(scene)
    }

    fun update(dt: Float) {
        updatePositions(dt)
        updateAnimations()
        updateCameras()

        mBandit.shootPlayer(mPlayer)
        score += mPlayer.checkCollectibles(collectibles)

        mPlayer.updateInvincible(dt)

        hub.updateScoreBoard(score)
        hub.updateHearts(mPlayer.lives)
    }

    private fun updateAnimations(){
        mPlayer.updateAnimations()
        mBandit.updateAnimations()
    }

    private fun updateCameras(){
        gameLayer.mCamera!!.setMPosition(Vector2D(mPlayer.body.position.x + gameCameraOffset, 0f))
        for (i in 0 until layers.size-2){
            layers[i].mCamera!!.moveLeft((gameLayer.mCamera!!.mPosition.x - gameCameraLastXPos) * layers[i].cameraSpeed)
        }
        gameCameraLastXPos = gameLayer.mCamera!!.mPosition.x
    }

    private fun updatePositions(dt: Float){
        mPlayer.updatePosition(ground, dt)
        mPlayer.checkPlatforms(platforms)
        mPlayer.updateBullet(dt, gameLayer.mCamera!!.viewPort, mBandit)
        if(!mPlayer.state.isInjured){
            mPlayer.checkHoles(holes)
            gameCameraOffset = mPlayer.checkBarrels(barrels, gameCameraOffset, dt)
        }

        if(gameCameraOffset>gameCameraBaseOffset){
            gameCameraOffset -=  mPlayer.movement.x.speed * 0.5f * dt
        }
        if(mPlayer.body.getBoundingBox().maxpoint.x<gameLayer.mCamera!!.viewPort.minpoint.x){
            mPlayer.movementState = MovementState.FALLING
            mPlayer.body.position.y = 282f
            mPlayer.lives--
            mPlayer.state.isInjured = true
        }

        mBandit.updatePosition(ground, dt)
        mBandit.checkPlatforms(platforms)
        if(!mBandit.body.getBoundingBox().checkOverlapping(gameLayer.mCamera!!.viewPort)){
            mBandit.movementState = MovementState.FALLING
        }
        mBandit.checkHoles(holes)
        mBandit.updateBullet(dt, gameLayer.mCamera!!.viewPort, mPlayer)
        score += mBandit.updateLives()

        // Infinite grounds
        for (groundLayer in groundLayers){
            val viewPort = groundLayer.mCamera!!.viewPort
            if(viewPort.maxpoint.x > groundLayer.mObjectList[0].getBoundingBox().maxpoint.x){
                groundLayer.mObjectList[1].position.x = groundLayer.mObjectList[0].getBoundingBox().maxpoint.x
                Collections.swap(groundLayer.mObjectList, 1, 0)
            }
            else if(viewPort.minpoint.x < groundLayer.mObjectList[0].getBoundingBox().minpoint.x){
                groundLayer.mObjectList[1].position.x =
                    groundLayer.mObjectList[0].getBoundingBox().minpoint.x - (groundLayer.mObjectList[0].getBoundingBox().maxpoint.x - groundLayer.mObjectList[0].getBoundingBox().minpoint.x)
                Collections.swap(groundLayer.mObjectList, 1, 0)
            }
        }
    }

    fun cleanup() {
        renderer.cleanup()
    }
}