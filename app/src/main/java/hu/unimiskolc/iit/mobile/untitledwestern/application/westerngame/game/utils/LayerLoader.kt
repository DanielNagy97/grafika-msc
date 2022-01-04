package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.utils

import android.content.Context
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.C2DGraphicsLayer
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.CCamera2D
import org.json.JSONObject

class LayerLoader : JsonLoader() {
    fun makeLayer(
        jsonObject: JSONObject,
        context: Context,
        scale: Float,
        horizon: Float,
        aspect: Float,
        viewPortHalfHeight: Float,
        gameObjectLoader: GameObjectLoader
    ): C2DGraphicsLayer {
        val newLayer: C2DGraphicsLayer

        val cameraSpeed = loadFloat("cameraSpeed", jsonObject)
        newLayer = C2DGraphicsLayer(cameraSpeed)

        val layerGameObjects = loadArray("gameObjects", jsonObject)
        for (i in 0 until layerGameObjects.length()) {
            val newObjects = gameObjectLoader.makeObjects(
                layerGameObjects.getJSONObject(i),
                context, scale, horizon
            )
            for (gameObj in newObjects) {
                newLayer.addGameObject(gameObj)
            }
        }
        newLayer.setCamera(CCamera2D(0f, 0f, aspect, viewPortHalfHeight))

        return newLayer
    }
}