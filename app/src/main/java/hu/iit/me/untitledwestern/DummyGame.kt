package hu.iit.me.untitledwestern

import android.content.Context
import hu.iit.me.untitledwestern.engine.*
import hu.iit.me.untitledwestern.engine.math.Vector2D
import hu.iit.me.untitledwestern.engine.util.JsonParserUtil
import hu.iit.me.untitledwestern.engine.util.TextUtil
import hu.iit.me.untitledwestern.game.Character
import org.json.JSONObject
import org.json.JSONTokener
import java.util.*
import kotlin.collections.ArrayList

class DummyGame(private var context: Context,
                var renderer: MyGLRenderer,
                private val scale: Float) {
    var sceneManager: C2DSceneManager = C2DSceneManager()
    private var scene: C2DScene = C2DScene()

    lateinit var mPlayer: Character

    lateinit var platforms: List<GameObject>
    private var layers: ArrayList<C2DGraphicsLayer> = ArrayList()

    lateinit var coins: List<GameObject>

    private var ground: Float = -75f
    private var horizon: Float = -21f

    fun init(){
        val sceneModel = JSONTokener(TextUtil.readFile(context,
            "scenes/scene01.json")).nextValue() as JSONObject

        horizon = sceneModel.getDouble("horizon").toString().toFloat()
        ground = sceneModel.getDouble("ground").toString().toFloat()

        var mPlayerObject =
            JsonParserUtil.makeGameObjects(sceneModel.getJSONObject("player"),
                                           context, scale, horizon)[0]
        var mPistolObject =
            JsonParserUtil.makeGameObjects(sceneModel.getJSONObject("player")
                .getJSONObject("pistol"),
                context, scale, horizon)[0]

        mPlayer = Character(mPlayerObject, mPistolObject, 100f)

        coins = JsonParserUtil.makeGameObjects(sceneModel.getJSONObject("coins"),
                                               context, scale, horizon)
        platforms = JsonParserUtil.makeGameObjects(sceneModel.getJSONObject("platforms"),
                                                   context, scale, horizon)

        val layerModels = sceneModel.getJSONArray("layers")
        for (i in 0 until layerModels.length()){
            layers.add(JsonParserUtil.makeLayer(layerModels.getJSONObject(i),
                                                context, scale, horizon, renderer.ratio))
        }

        for (coin in coins){
            layers.last().addGameObject(coin)
        }
        for (plat in platforms){
            layers.last().addGameObject(plat)
        }

        layers.last().addGameObject(mPlayerObject)
        layers.last().addGameObject(mPistolObject)

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