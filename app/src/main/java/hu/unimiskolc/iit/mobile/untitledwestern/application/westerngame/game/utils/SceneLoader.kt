package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.utils

import android.content.Context
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.C2DGraphicsLayer
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.CCamera2D
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.GameObject
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.util.TextUtil
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.*
import org.json.JSONObject
import org.json.JSONTokener

class SceneLoader(
    sceneFileName: String,
    private val context: Context,
    private val scale: Float,
    private var horizon: Float,
    private var ratio: Float
) : JsonLoader() {
    private val sceneModel: JSONObject =
        JSONTokener(TextUtil.readFile(context, sceneFileName)).nextValue() as JSONObject
    private val layerLoader = LayerLoader()
    private val gameObjectLoader = GameObjectLoader()
    private val collectibleLoader = CollectibleLoader()
    private val scoreNumberLoader = ScoreNumberLoader()

    fun loadHorizon(): Float {
        horizon = loadFloat("horizon", sceneModel)
        return horizon
    }

    fun loadGround(): Float {
        return loadFloat("ground", sceneModel)
    }

    fun loadCollectibles(): ArrayList<Collectible> {
        val collectibles: ArrayList<Collectible> = ArrayList()
        val collectibleModels = loadArray("collectibles", sceneModel)

        for (i in 0 until collectibleModels.length()) {
            collectibles.addAll(
                collectibleLoader.makeObjects(
                    collectibleModels.getJSONObject(i),
                    context,
                    scale,
                    horizon
                )
            )
        }

        return collectibles
    }

    fun loadHubElements(name: String): ArrayList<GameObject> {
        return scoreNumberLoader.makeObjects(
            sceneModel.getJSONObject(name),
            context,
            scale,
            horizon
        )
    }

    fun loadGameObject(name: String): GameObject {
        return gameObjectLoader.makeObject(
            sceneModel.getJSONObject(name),
            context,
            scale,
            horizon
        )
    }

    fun loadGameObjects(name: String): List<GameObject> {
        return gameObjectLoader.makeObjects(
            sceneModel.getJSONObject(name),
            context,
            scale,
            horizon
        )
    }

    private fun loadPistol(characterName: String): GameObject {
        return gameObjectLoader.makeObject(
            sceneModel.getJSONObject(characterName).getJSONObject("pistol"),
            context,
            scale,
            horizon
        )
    }

    private fun loadBullets(gunslinger: Gunslinger, characterName: String) {
        val bulletSpritePath = loadString("bulletSpritePath", sceneModel)
        val numberOfBullets = loadInt("numberOfBullets", sceneModel.getJSONObject(characterName))

        for (i in 0 until numberOfBullets) {
            gunslinger.bullets.add(Bullet(context, 0f, 0f, scale, 0f, 0f, 0f))
            gunslinger.bullets.last().addSprite(bulletSpritePath, 1, 0)
        }
    }

    fun loadPlayer(lives: Int, velocity: Float = 100f): Player {
        val mBodyObject = loadGameObject("player")
        val mPistolObject = loadPistol("player")
        val mPlayer = Player(mBodyObject, mPistolObject, velocity, lives)

        loadBullets(mPlayer, "player")
        return mPlayer
    }

    fun loadBandits(lives: Int, velocity: Float = 100f): Bandit {
        val mBodyObject = loadGameObject("bandit")
        val mPistolObject = loadPistol("bandit")
        val mBandit = Bandit(mBodyObject, mPistolObject, velocity, lives)

        loadBullets(mBandit, "bandit")
        return mBandit
    }

    fun loadLayers(viewPortHalfHeight: Float): ArrayList<C2DGraphicsLayer> {
        val layers: ArrayList<C2DGraphicsLayer> = ArrayList()
        val layerModels = loadArray("layers", sceneModel)
        for (i in 0 until layerModels.length()) {
            layers.add(
                layerLoader.makeLayer(
                    layerModels.getJSONObject(i),
                    context, scale, horizon, ratio, viewPortHalfHeight, gameObjectLoader
                )
            )
        }
        //Hub-layer
        layers.add(C2DGraphicsLayer(0.0f))
        layers.last().setCamera(CCamera2D(0f, 0f, ratio, viewPortHalfHeight))
        return layers
    }
}