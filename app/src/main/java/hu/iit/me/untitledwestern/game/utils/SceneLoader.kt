package hu.iit.me.untitledwestern.game.utils

import android.content.Context
import hu.iit.me.untitledwestern.engine.C2DGraphicsLayer
import hu.iit.me.untitledwestern.engine.CCamera2D
import hu.iit.me.untitledwestern.engine.GameObject
import hu.iit.me.untitledwestern.engine.util.*
import hu.iit.me.untitledwestern.game.Coin
import hu.iit.me.untitledwestern.game.Player
import org.json.JSONObject
import org.json.JSONTokener

class SceneLoader(
    sceneFileName: String,
    private val context: Context,
    private val scale: Float,
    private val horizon: Float,
    private var ratio: Float
    ): JsonLoader() {
    private val sceneModel: JSONObject = JSONTokener(TextUtil.readFile(context, sceneFileName)).nextValue() as JSONObject
    private val layerLoader = LayerLoader()
    private val gameObjectLoader = GameObjectLoader()
    private val coinLoader = CoinLoader()
    private val scoreNumberLoader = ScoreNumberLoader()

    fun loadHorizon(): Float {
        return loadFloat("horizon", sceneModel)
    }

    fun loadGround(): Float {
        return loadFloat("ground", sceneModel)
    }

    fun loadCoins(): ArrayList<Coin> {
        return coinLoader.makeObjects(sceneModel.getJSONObject("coins"), context, scale, horizon)
    }

    fun loadScoreNumbers(): ArrayList<GameObject> {
        return scoreNumberLoader.makeObjects(sceneModel.getJSONObject("scoreNumbers"), context, scale, horizon)
    }

    fun loadHearts(): ArrayList<GameObject> {
        return scoreNumberLoader.makeObjects(sceneModel.getJSONObject("hearts"), context, scale, horizon)
    }

    fun loadPlayer(): Player {
        val mPlayerObject = gameObjectLoader.makeObject(sceneModel.getJSONObject("player"), context, scale, horizon)
        val mPistolObject = gameObjectLoader.makeObject(sceneModel.getJSONObject("player").getJSONObject("pistol"), context, scale, horizon)

        return Player(mPlayerObject, mPistolObject, 100f)
    }

    fun loadPlatforms(): List<GameObject> {
        return gameObjectLoader.makeObjects(sceneModel.getJSONObject("platforms"), context, scale, horizon)
    }

    fun loadLayers(): ArrayList<C2DGraphicsLayer>{
        var layers: ArrayList<C2DGraphicsLayer> = ArrayList()
        val layerModels = loadArray("layers", sceneModel)
        for (i in 0 until layerModels.length()){
            layers.add(
                layerLoader.makeLayer(layerModels.getJSONObject(i),
                context, scale, horizon, ratio, gameObjectLoader))
        }
        //Hub-layer
        layers.add(C2DGraphicsLayer("hub-layer", 0, 0.0f))
        layers.last().setCamera(CCamera2D(0f, 0f, 0, 16f/9f))
        return layers
    }
}