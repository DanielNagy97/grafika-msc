package hu.iit.me.untitledwestern

import android.content.Context
import hu.iit.me.untitledwestern.engine.*
import hu.iit.me.untitledwestern.engine.math.Vector2D
import hu.iit.me.untitledwestern.game.Bullet
import hu.iit.me.untitledwestern.game.Collectible
import hu.iit.me.untitledwestern.game.Player
import hu.iit.me.untitledwestern.game.utils.SceneLoader
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

    lateinit var platforms: List<GameObject>
    private var layers: ArrayList<C2DGraphicsLayer> = ArrayList()

    private lateinit var gameLayer: C2DGraphicsLayer
    private lateinit var hubLayer: C2DGraphicsLayer

    var collectibles: ArrayList<Collectible> = ArrayList()

    var scoreNumbers: ArrayList<GameObject> = ArrayList()

    private var ground: Float = -75f
    private var horizon: Float = -21f

    var score: Int = 0
    var hearts: ArrayList<GameObject> = ArrayList()

    fun init(){
        var sceneLoader = SceneLoader("scenes/scene01.json", context, scale, horizon, renderer.ratio)

        horizon = sceneLoader.loadHorizon()
        ground = sceneLoader.loadGround()

        mPlayer = sceneLoader.loadPlayer()

        for (i in 0 until 3){
            mPlayer.bullets.add(Bullet(context, 0f,0f, scale, 0f, 0f, 0f))
            mPlayer.bullets.last().addSprite("sprites/bullet/bullet.png", 1, 0)
        }

        collectibles = sceneLoader.loadCollectibles()
        platforms = sceneLoader.loadPlatforms()

        scoreNumbers = sceneLoader.loadScoreNumbers()

        hearts = sceneLoader.loadHearts()

        layers = sceneLoader.loadLayers()

        gameLayer = layers[layers.size-2]
        hubLayer = layers[layers.size-1]

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



        for (plat in platforms){
            gameLayer.addGameObject(plat)
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


        gameLayer.mCamera!!.viewPort.mEnabled = true

        for(layer in layers){
            scene.registerLayer(layer)
        }
        sceneManager.registerScene(scene)
    }

    fun update(dt: Float) {
        score += mPlayer.checkCollectibles(collectibles)
        updatePositions(dt)
        updateAnimations()
        updateCameras(dt)
        updateScoreBoard()
    }

    private fun updateAnimations(){
        mPlayer.updateAnimations()
    }

    private fun updateCameras(dt: Float){
        for (i in 0 until layers.size-2){
            layers[i].mCamera!!.moveLeft(mPlayer.xdir * mPlayer.speedX * layers[i].cameraSpeed * dt)
        }
        gameLayer.mCamera!!.setMPosition(Vector2D(mPlayer.body.position.x + 90, 0f))
    }

    private fun updatePositions(dt: Float){
        mPlayer.updatePosition(ground, platforms, dt)

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
                for (i in 0 until mPlayer.bullets.size){
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

    fun cleanup() {
        renderer.cleanup()
    }
}