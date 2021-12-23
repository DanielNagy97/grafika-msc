package hu.unimiskolc.iit.mobile.untitledwestern.application

import android.content.Context
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.*
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.math.Vector2D
import hu.unimiskolc.iit.mobile.untitledwestern.application.game.Collectible
import hu.unimiskolc.iit.mobile.untitledwestern.application.game.Player
import hu.unimiskolc.iit.mobile.untitledwestern.application.game.utils.SceneLoader
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

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

    var collectibles: ArrayList<Collectible> = ArrayList()

    private var ground: Float = -75f
    private var horizon: Float = -21f

    private var layers: ArrayList<C2DGraphicsLayer> = ArrayList()
    private lateinit var gameLayer: C2DGraphicsLayer
    private lateinit var hubLayer: C2DGraphicsLayer

    var score: Int = 0
    var scoreNumbers: ArrayList<GameObject> = ArrayList()
    var hearts: ArrayList<GameObject> = ArrayList()

    var gameCameraLastXPos = 0f

    private val gameCameraBaseOffset: Float = 80f
    private var gameCameraOffset: Float = gameCameraBaseOffset

    fun init(){
        var sceneLoader = SceneLoader("scenes/scene01.json", context, scale, horizon, renderer.ratio)

        horizon = sceneLoader.loadHorizon()
        ground = sceneLoader.loadGround()

        scoreNumbers = sceneLoader.loadScoreNumbers()
        hearts = sceneLoader.loadHearts()

        mPlayer = sceneLoader.loadPlayer(hearts.size)

        collectibles = sceneLoader.loadCollectibles()
        platforms = sceneLoader.loadPlatforms()
        holes = sceneLoader.loadHoles()
        barrels = sceneLoader.loadBarrels()

        layers = sceneLoader.loadLayers()

        gameLayer = layers[layers.size-2]
        hubLayer = layers[layers.size-1]

        gameCameraLastXPos = gameLayer.mCamera!!.mPosition.x

        // x position
        scoreNumbers.last().position.x = hubLayer.mCamera!!.viewPort.minpoint.x + 2 * scale
        for (i in scoreNumbers.size-1 downTo 1){
            scoreNumbers[i-1].position.x = scoreNumbers[i].getBoundingBox().maxpoint.x + 2 * scale
        }
        // y position
        for (num in scoreNumbers){
            num.position.y = hubLayer.mCamera!!.viewPort.maxpoint.y - (num.getBoundingBox().maxpoint.y-num.getBoundingBox().minpoint.y) - 2 * scale
        }

        // x position
        hearts[0].position.x = hubLayer.mCamera!!.viewPort.maxpoint.x - (hearts[0].getBoundingBox().maxpoint.x-hearts[0].getBoundingBox().minpoint.x) - 2 * scale
        for (i in 1 until hearts.size){
            hearts[i].position.x = hearts[i-1].getBoundingBox().minpoint.x - (hearts[i].getBoundingBox().maxpoint.x-hearts[i].getBoundingBox().minpoint.x) - 2 * scale
        }
        // y position
        for (heart in hearts){

            heart.position.y = hubLayer.mCamera!!.viewPort.maxpoint.y - (heart.getBoundingBox().maxpoint.y-heart.getBoundingBox().minpoint.y) - 2 * scale
        }


        for (hole in holes){
            gameLayer.addGameObject(hole)
        }
        for (plat in platforms){
            gameLayer.addGameObject(plat)
        }
        for (barrel in barrels){
            gameLayer.addGameObject(barrel)
        }
        for (coin in collectibles){
            gameLayer.addGameObject(coin)
        }

        for (num in scoreNumbers){
            hubLayer.addGameObject(num)
        }

        for (heart in hearts){
            hubLayer.addGameObject(heart)
        }

        gameLayer.addGameObject(mPlayer.body)
        gameLayer.addGameObject(mPlayer.pistol)
        for (bullet in mPlayer.bullets){
            gameLayer.addGameObject(bullet)
        }


        hubLayer.mCamera!!.viewPort.mEnabled = true

        for(layer in layers){
            scene.registerLayer(layer)
        }
        sceneManager.registerScene(scene)
    }

    fun update(dt: Float) {
        score += mPlayer.checkCollectibles(collectibles)
        updatePositions(dt)
        updateAnimations()
        updateCameras()
        updateScoreBoard()
        updateHearts()
    }

    private fun updateAnimations(){
        mPlayer.updateAnimations()
    }

    private fun updateCameras(){
        gameLayer.mCamera!!.setMPosition(Vector2D(mPlayer.body.position.x + gameCameraOffset, 0f))
        for (i in 0 until layers.size-2){
            layers[i].mCamera!!.moveLeft((gameLayer.mCamera!!.mPosition.x - gameCameraLastXPos) * layers[i].cameraSpeed)
        }
        gameCameraLastXPos = gameLayer.mCamera!!.mPosition.x
    }

    private fun updatePositions(dt: Float){
        gameCameraOffset = mPlayer.updatePosition(ground, platforms, holes, barrels, gameCameraOffset, dt)
        if(gameCameraOffset>gameCameraBaseOffset){
            gameCameraOffset -=  mPlayer.speedX*0.5f * dt
        }
        if(mPlayer.body.getBoundingBox().maxpoint.x<gameLayer.mCamera!!.viewPort.minpoint.x){
            mPlayer.falling = true
            mPlayer.body.position.y = 282f
            mPlayer.lives--
        }

        for (i in 0 until mPlayer.bullets.size){
            mPlayer.bullets[i].updatePosition(dt)
        }

        // Infinite grounds
        for (i in 4 until layers.size-1){
            val viewPort = layers[i].mCamera!!.viewPort
            if(viewPort.maxpoint.x > layers[i].mObjectList[0].getBoundingBox().maxpoint.x){
                layers[i].mObjectList[1].position.x = layers[i].mObjectList[0].getBoundingBox().maxpoint.x
                Collections.swap(layers[i].mObjectList, 1, 0)
            }
            else if(viewPort.minpoint.x < layers[i].mObjectList[0].getBoundingBox().minpoint.x){
                layers[i].mObjectList[1].position.x =
                    layers[i].mObjectList[0].getBoundingBox().minpoint.x - (layers[i].mObjectList[0].getBoundingBox().maxpoint.x -layers[i].mObjectList[0].getBoundingBox().minpoint.x)
                Collections.swap(layers[i].mObjectList, 1, 0)
            }

            // Bullet handling
            if(i == layers.size-2){
                for (i: Int in 0 until mPlayer.bullets.size){
                    if(mPlayer.bullets[i].visible){
                        if(mPlayer.bullets[i].getBoundingBox().minpoint.x > viewPort.maxpoint.x
                            || mPlayer.bullets[i].getBoundingBox().maxpoint.x < viewPort.minpoint.x){
                            mPlayer.bullets[i].isFired = false
                            mPlayer.bullets[i].visible = false
                        }
                    }
                }
            }
        }
    }

    private fun updateScoreBoard() {
        var nDigits: Int = (floor(log10(score.toDouble())) + 1).toInt()
        if (nDigits < 0) {
            nDigits = 0
        }
        for(i in 1..nDigits){
            val nthNumber: Int = floor(score / 10.0.pow((i - 1).toDouble()) % 10).toInt()
            hubLayer.mObjectList[i-1].currSprite = nthNumber
        }
    }

    private fun updateHearts() {
        for (i in 0 until hearts.size){
            hearts[i].visible = i < mPlayer.lives
        }
    }

    fun cleanup() {
        renderer.cleanup()
    }
}