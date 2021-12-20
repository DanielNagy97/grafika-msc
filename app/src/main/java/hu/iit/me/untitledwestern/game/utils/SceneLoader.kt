package hu.iit.me.untitledwestern.game.utils

import android.content.Context
import hu.iit.me.untitledwestern.engine.C2DGraphicsLayer
import hu.iit.me.untitledwestern.engine.CCamera2D
import hu.iit.me.untitledwestern.engine.GameObject
import hu.iit.me.untitledwestern.engine.util.*
import hu.iit.me.untitledwestern.game.Bullet
import hu.iit.me.untitledwestern.game.Collectible
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
    private val collectibleLoader = CollectibleLoader()
    private val scoreNumberLoader = ScoreNumberLoader()

    fun loadHorizon(): Float {
        return loadFloat("horizon", sceneModel)
    }

    fun loadGround(): Float {
        return loadFloat("ground", sceneModel)
    }

    fun loadCollectibles(): ArrayList<Collectible> {
        var collectibles : ArrayList<Collectible> = ArrayList()
        val collectibleModels = loadArray("collectibles", sceneModel)

        for (i in 0 until collectibleModels.length()){
            collectibles.addAll(collectibleLoader.makeObjects(collectibleModels.getJSONObject(i), context, scale, horizon))
        }

        return collectibles
    }

    fun loadScoreNumbers(): ArrayList<GameObject> {
        return scoreNumberLoader.makeObjects(sceneModel.getJSONObject("scoreNumbers"), context, scale, horizon)
    }

    fun loadHearts(): ArrayList<GameObject> {
        return scoreNumberLoader.makeObjects(sceneModel.getJSONObject("hearts"), context, scale, horizon)
    }

    fun loadPlayer(lives: Int, velocity: Float = 100f): Player {
        val mPlayerObject = gameObjectLoader.makeObject(sceneModel.getJSONObject("player"), context, scale, horizon)
        val mPistolObject = gameObjectLoader.makeObject(sceneModel.getJSONObject("player").getJSONObject("pistol"), context, scale, horizon)
        val mPlayer = Player(mPlayerObject, mPistolObject, velocity, lives)

        for (i in 0 until 3){
            mPlayer.bullets.add(Bullet(context, 0f,0f, scale, 0f, 0f, 0f))
            mPlayer.bullets.last().addSprite("sprites/bullet/bullet.png", 1, 0)
        }
        return mPlayer
    }

    fun loadPlatforms(): List<GameObject> {
        return gameObjectLoader.makeObjects(sceneModel.getJSONObject("platforms"), context, scale, horizon)
    }

    fun loadHoles(): List<GameObject> {
        return gameObjectLoader.makeObjects(sceneModel.getJSONObject("holes"), context, scale, horizon)
    }

    fun loadBarrels(): List<GameObject> {
        return gameObjectLoader.makeObjects(sceneModel.getJSONObject("barrels"), context, scale, horizon)
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