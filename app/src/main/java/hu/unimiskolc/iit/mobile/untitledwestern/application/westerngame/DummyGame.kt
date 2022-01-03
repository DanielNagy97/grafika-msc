package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame

import android.content.Context
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.C2DSceneManager
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.C2DScene
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.GameObject
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.C2DGraphicsLayer
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.math.Vector2D
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.Bandit
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.Collectible
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.Player
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.Hub
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.states.GameState
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.utils.SceneLoader
import java.util.Collections
import kotlin.collections.ArrayList

class DummyGame(
    private var context: Context,
    private val scale: Float,
    private val sceneFileName: String,
    private var showBoundingBoxes: Boolean = false
    ) {
    lateinit var renderer: Renderer
    var sceneManager: C2DSceneManager = C2DSceneManager()
    private var scene: C2DScene = C2DScene()

    lateinit var mPlayer: Player

    private lateinit var platforms: List<GameObject>
    private lateinit var holes: List<GameObject>
    private lateinit var barrels: List<GameObject>
    private lateinit var mBandit: Bandit

    private var collectibles: ArrayList<Collectible> = ArrayList()

    private var ground: Float = -75f
    private var horizon: Float = -21f

    private var layers: ArrayList<C2DGraphicsLayer> = ArrayList()
    private var groundLayers: ArrayList<C2DGraphicsLayer> = ArrayList()
    private lateinit var gameLayer: C2DGraphicsLayer

    var score: Int = 0
    lateinit var hub: Hub

    private var gameCameraLastXPos = 0f
    private val gameCameraBaseOffset: Float = 100f
    private var gameCameraOffset: Float = gameCameraBaseOffset

    var gameState: GameState = GameState.NOT_STARTED

    private var elapsedAfterDeath = 0f

    private val nightColor = floatArrayOf(0.2f, 0.4f, 0.7f)
    private var isDayLight: Boolean = true
    private var lastScoreAtLightChange: Int = 0
    private var lastScoreAtSpeedUp: Int = 0
    private val maxSeed = 150f

    fun init(renderer: Renderer){
        this.renderer = renderer
        val sceneLoader = SceneLoader(sceneFileName, context, scale, horizon, renderer.ratio)

        horizon = sceneLoader.loadHorizon()
        ground = sceneLoader.loadGround()

        collectibles = sceneLoader.loadCollectibles()
        platforms = sceneLoader.loadPlatforms()
        holes = sceneLoader.loadHoles()
        barrels = sceneLoader.loadBarrels()

        layers = sceneLoader.loadLayers(renderer.viewPortHalfHeight)

        for (i in 4 until layers.size-1){
            groundLayers.add(layers[i])
        }

        gameLayer = layers[layers.size-2]
        gameCameraLastXPos = gameLayer.mCamera!!.mPosition.x

        hub = Hub(layers[layers.size-1],
                  sceneLoader.loadScoreNumbers(),
                  sceneLoader.loadHearts(),
                  sceneLoader.loadGameOverText(),
                  sceneLoader.loadStartGameText(),
                  scale)
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
        hub.hubLayer.addGameObject(hub.gameOverText)
        hub.hubLayer.addGameObject(hub.startGameText)

        if(showBoundingBoxes){
            for(gameObject in gameLayer.mObjectList){
                gameObject.enableBoundingBoxesVisibility()
            }
        }

        for(layer in layers){
            scene.registerLayer(layer)
        }
        sceneManager.registerScene(scene)
    }

    fun update(dt: Float) {
        if(gameState == GameState.NOT_STARTED){
            hub.blinkStartText(dt)
        }
        else{
            hub.startGameText.visible = false
        }
        if(gameState != GameState.ENDED){
            updatePositions(dt)
            updateAnimations()
            updateCameras()
            updateColors()

            mBandit.shootPlayer(mPlayer)
            mPlayer.updateInvincible(dt)

            makeChangesAccordingToScore()

            hub.updateScoreBoard(score)
            hub.updateHearts(mPlayer.lives)
            if(mPlayer.lives <= 0){
                gameState = GameState.ENDED
                hub.gameOverText.visible = true
            }
        }
        else {
            elapsedAfterDeath += dt
            if(elapsedAfterDeath > 3){
                renderer.view.endGame()
            }
        }
    }

    private fun makeChangesAccordingToScore(){
        if(score-lastScoreAtLightChange > 500 && score != lastScoreAtLightChange){
            isDayLight = !isDayLight
            lastScoreAtLightChange = score
        }
        if(score-lastScoreAtSpeedUp > 100 && score != lastScoreAtSpeedUp){
            if(mPlayer.movement.x.speed<maxSeed){
                mPlayer.movement.x.speed *= 1.05f
            }
            lastScoreAtSpeedUp = score
        }
    }

    private fun updateColors() {
        val changeRate = 0.003f

        for(i in 0 until layers.size-1){
            for(j in 0..2){
                if(isDayLight) {
                    if (layers[i].color[j] < 1f) {
                        layers[i].color[j] += changeRate
                    }
                    if (layers[i].color[j] > 1f) {
                        layers[i].color[j] = 1f
                    }
                }
                else {
                    if(layers[i].color[j] > nightColor[j]){
                        layers[i].color[j] -= changeRate
                    }
                }
            }
        }
    }

    private fun updateAnimations(){
        mPlayer.updateAnimations()
        mBandit.updateAnimations()
    }

    private fun updateCameras(){
        gameLayer.mCamera!!.mPosition = Vector2D(mPlayer.body.position.x + gameCameraOffset, 0f)
        for (i in 0 until layers.size-2){
            layers[i].mCamera!!.moveLeft((gameLayer.mCamera!!.mPosition.x - gameCameraLastXPos) * layers[i].cameraSpeed)
        }
        gameCameraLastXPos = gameLayer.mCamera!!.mPosition.x
    }

    private fun updatePositions(dt: Float){
        mPlayer.updatePosition(ground, renderer.viewPortHalfHeight, dt)
        mPlayer.checkPlatforms(platforms)
        score += mPlayer.updateBullet(dt, gameLayer.mCamera!!.viewPort, mBandit)
        score += mPlayer.checkCollectibles(collectibles)
        gameCameraOffset = mPlayer.updateCameraOffset(barrels, holes, gameCameraOffset, gameCameraBaseOffset, dt)
        mPlayer.checkIfPushedFromViewport(gameLayer.mCamera!!.viewPort.minpoint.x, renderer.viewPortHalfHeight)

        mBandit.updatePosition(ground, renderer.viewPortHalfHeight, dt)
        mBandit.checkPlatforms(platforms)
        mBandit.checkHoles(holes)
        mBandit.updateBullet(dt, gameLayer.mCamera!!.viewPort, mPlayer)
        mBandit.checkIfOutsideOfViewport(gameLayer.mCamera!!.viewPort)
        mBandit.updateLives(gameLayer.mCamera!!.viewPort.minpoint.x)

        calculateInfiniteGrounds()
    }

    private fun calculateInfiniteGrounds() {
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
        sceneManager.cleanup()
    }
}