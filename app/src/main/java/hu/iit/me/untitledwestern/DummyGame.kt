package hu.iit.me.untitledwestern

import android.content.Context
import android.util.Log
import hu.iit.me.untitledwestern.engine.*
import hu.iit.me.untitledwestern.engine.math.Vector2D
import hu.iit.me.untitledwestern.game.Bullet
import hu.iit.me.untitledwestern.game.Coin
import hu.iit.me.untitledwestern.game.Player
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

    lateinit var mPlayer: Player

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

        for (i in 0 until 3){
            mPlayer.bullets.add(Bullet(context, 0f,0f, scale, 0f, 0f, 0f))
            mPlayer.bullets.last().addSprite("sprites/bullet/bullet.png", 1, 0)
        }

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
        for (bullet in mPlayer.bullets){
            layers.last().addGameObject(bullet)
        }


        layers.last().mCamera!!.viewPort.mEnabled = true

        for(layer in layers){
            scene.registerLayer(layer)
        }
        sceneManager.registerScene(scene)
    }

    fun update(dt: Float) {
        mPlayer.checkCoins(coins)
        updatePositions(dt)
        updateAnimations()
        updateCameras(dt)
    }

    private fun updateAnimations(){
        mPlayer.updateAnimations()
    }

    private fun updateCameras(dt: Float){
        for (i in 0 until layers.size-1){
            layers[i].mCamera!!.moveLeft(mPlayer.xdir * mPlayer.speedX * layers[i].cameraSpeed * dt)
        }
        layers.last().mCamera!!.setMPosition(Vector2D(mPlayer.body.position.x + 90, 0f))
    }

    private fun updatePositions(dt: Float){
        mPlayer.updatePosition(ground, platforms, dt)

        for (i in 0 until mPlayer.bullets.size){
            mPlayer.bullets[i].updatePosition(dt)
        }


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

            if(i == layers.size-1){
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

    fun cleanup() {
        renderer.cleanup()
    }
}