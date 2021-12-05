package hu.iit.me.untitledwestern.engine.util

import android.content.Context
import hu.iit.me.untitledwestern.engine.C2DGraphicsLayer
import hu.iit.me.untitledwestern.engine.CCamera2D
import hu.iit.me.untitledwestern.engine.GameObject
import org.json.JSONObject

class JsonParserUtil {
    companion object{
        fun makeLayer(jsonObject: JSONObject,
                      context: Context,
                      scale: Float,
                      horizon: Float,
                      aspect: Float
        ): C2DGraphicsLayer {
            var newLayer: C2DGraphicsLayer

            val name = jsonObject.getString("name")
            val cameraSpeed = jsonObject.getDouble("cameraSpeed").toString().toFloat()
            newLayer = C2DGraphicsLayer(name, 0, cameraSpeed)

            val layerGameObjects = jsonObject.getJSONArray("gameObjects")
            for (i in 0 until layerGameObjects.length()) {
                val newObjs = makeGameObjects(layerGameObjects.getJSONObject(i),
                                              context, scale, horizon)
                for (gameObj in newObjs) {
                    newLayer.addGameObject(gameObj)
                }
            }
            newLayer.setCamera(CCamera2D(0f, 0f, 0, aspect))

            return newLayer
        }

        fun makeGameObjects(jsonObject: JSONObject,
                            context: Context,
                            scale: Float,
                            horizon: Float
        ): List<GameObject>{
            var gameObjects: ArrayList<GameObject> = ArrayList()
            val pPositions = jsonObject.getJSONArray("positions")

            val pSprites = jsonObject.getJSONArray("sprites")

            val type = jsonObject.getString("type")
            var interval = 0.0f

            var minY = 0.0f
            var maxY = 0.0f

            if(type == "repeating"){
                interval = jsonObject.getString("interval").toFloat()
                minY = if(jsonObject.getString("minY") == "horizon"){
                    horizon
                } else {
                    jsonObject.getString("minY").toFloat()
                }
                maxY = if(jsonObject.getString("maxY") == "horizon"){
                    horizon
                } else {
                    jsonObject.getString("maxY").toFloat()
                }
            }
            for (i in 0 until pPositions.length()) {
                val pPosition = pPositions.getJSONArray(i)
                var posY = if(pPosition[1].toString() == "horizon"){
                    horizon
                } else {
                    pPosition[1].toString().toFloat()
                }

                var newGameObject = GameObject(context, pPosition[0].toString().toFloat(), posY,
                                               scale, interval, minY, maxY)
                for (i in 0 until pSprites.length()) {
                    val spriteName = pSprites.getJSONObject(i).getString("fileName")
                    val numberOfFrames = pSprites.getJSONObject(i).getInt("numberOfFrames")
                    val Fps = pSprites.getJSONObject(i).getInt("Fps")
                    newGameObject.addSprite(spriteName, numberOfFrames, Fps)
                }
                gameObjects.add(newGameObject)
            }

            return gameObjects
        }
    }
}