package hu.iit.me.untitledwestern

import android.content.Context
import hu.iit.me.untitledwestern.engine.*
import hu.iit.me.untitledwestern.engine.math.Vector2D
import hu.iit.me.untitledwestern.game.Character
import hu.iit.me.untitledwestern.game.Coin
import hu.iit.me.untitledwestern.game.utils.SceneLoader
import java.util.*
import kotlin.collections.ArrayList

class DummyGame(
    private var context: Context,
    var renderer: MyGLRenderer,
    private val scale: Float
    ) {
    var sceneManager: C2DSceneManager = C2DSceneManager()
    private var scene: C2DScene = C2DScene()

    lateinit var mPlayer: Character

    lateinit var platforms: List<GameObject>
    private var layers: ArrayList<C2DGraphicsLayer> = ArrayList()

    var coins: ArrayList<Coin> = ArrayList()

    private var ground: Float = -75f
    private var horizon: Float = -21f

    fun init(){
        var sceneLoader = SceneLoader("scenes/scene01.json", context, scale, horizon, renderer.ratio)

        horizon = sceneLoader.loadHorizon()
        ground = sceneLoader.loadGround()

        mPlayer = sceneLoader.loadPlayer()
        coins = sceneLoader.loadCoins()
        platforms = sceneLoader.loadPlatforms()

        layers = sceneLoader.loadLayers()

        for (coin in coins){
            layers.last().addGameObject(coin)
        }
        for (plat in platforms){
            layers.last().addGameObject(plat)
        }

        layers.last().addGameObject(mPlayer.body)
        layers.last().addGameObject(mPlayer.pistol)

        layers.last().mCamera!!.viewPort.mEnabled = true

        for(layer in layers){
            scene.registerLayer(layer)
        }
        sceneManager.registerScene(scene)
    }

    fun updateAnimations(){
        mPlayer.updateAnimations()
    }

    fun updateCameras(dt: Float){
        for (i in 0 until layers.size-1){
            layers[i].mCamera!!.moveLeft(mPlayer.xdir * mPlayer.speedX * layers[i].cameraSpeed *dt)
        }
        layers.last().mCamera!!.setMPosition(Vector2D(mPlayer.body.position.x+90, 0f))
    }

    fun updatePositions(dt: Float){
        mPlayer.updatePosition(ground, platforms, coins, dt)

        // Infinite grounds
        for (i in 4 until layers.size){
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
        }
    }

    fun cleanup() {
        renderer.cleanup()
    }
}