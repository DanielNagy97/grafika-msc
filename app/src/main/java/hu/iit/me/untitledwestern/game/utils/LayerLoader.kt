package hu.iit.me.untitledwestern.game.utils

import android.content.Context
import hu.iit.me.untitledwestern.engine.C2DGraphicsLayer
import hu.iit.me.untitledwestern.engine.CCamera2D
import org.json.JSONObject

class LayerLoader: JsonLoader() {
    fun makeLayer(jsonObject: JSONObject,
                  context: Context,
                  scale: Float,
                  horizon: Float,
                  aspect: Float,
                  gameObjectLoader: GameObjectLoader
    ): C2DGraphicsLayer {
        var newLayer: C2DGraphicsLayer

        val name = loadString("name", jsonObject)
        val cameraSpeed = loadFloat("cameraSpeed", jsonObject)
        newLayer = C2DGraphicsLayer(name, 0, cameraSpeed)

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
        newLayer.setCamera(CCamera2D(0f, 0f, 0, aspect))

        return newLayer
    }
}