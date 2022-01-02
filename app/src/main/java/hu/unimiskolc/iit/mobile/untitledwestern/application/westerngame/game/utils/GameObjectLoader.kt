package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.utils

import android.content.Context
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.GameObject
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.math.Vector2D
import org.json.JSONObject

open class GameObjectLoader: JsonLoader() {
    protected fun loadPositions(jsonObject: JSONObject, horizon: Float): ArrayList<Vector2D>{
        var result: ArrayList<Vector2D> = ArrayList()
        val pPositions = loadArray("positions", jsonObject)

        for (i in 0 until pPositions.length()) {
            val pPosition = pPositions.getJSONArray(i)
            var posX = pPosition[0].toString().toFloat()
            var posY = if (pPosition[1].toString() == "horizon") {
                horizon
            } else {
                pPosition[1].toString().toFloat()
            }
            result.add(Vector2D(posX, posY))
        }
        return result
    }

    protected fun loadType(jsonObject: JSONObject): String{
        return loadString("type", jsonObject)
    }

    protected fun loadInterval(jsonObject: JSONObject, type: String): Float{
        return if(type == "repeating") {
            loadFloat("interval", jsonObject)
        } else{
            0.0f
        }
    }

    protected fun loadMinX(jsonObject: JSONObject, type: String, horizon: Float): Float{
        return if(type == "repeating") {
            if(loadString("minY", jsonObject) == "horizon"){
                horizon
            } else {
                loadFloat("minY", jsonObject)
            }
        } else{
            0.0f
        }
    }

    protected fun loadMaxY(jsonObject: JSONObject, type: String, horizon: Float): Float{
        return if(type == "repeating") {
            if(loadString("maxY", jsonObject) == "horizon"){
                horizon
            } else {
                loadFloat("maxY", jsonObject)
            }
        } else{
            0.0f
        }
    }

    protected fun loadSpritesToGameObject(jsonObject: JSONObject, gameObject: GameObject){
        val pSprites = loadArray("sprites", jsonObject)
        for (i in 0 until pSprites.length()) {
            val spriteName = loadString("fileName", pSprites.getJSONObject(i))
            val numberOfFrames = loadInt("numberOfFrames", pSprites.getJSONObject(i))
            val fps = loadInt("Fps", pSprites.getJSONObject(i))
            gameObject.addSprite(spriteName, numberOfFrames, fps)
        }
    }


    open fun makeObjects(jsonObject: JSONObject,
                         context: Context,
                         scale: Float,
                         horizon: Float
    ): List<GameObject>{
        var gameObjects: ArrayList<GameObject> = ArrayList()

        val positions = loadPositions(jsonObject, horizon)
        val type = loadType(jsonObject)
        val interval = loadInterval(jsonObject, type)
        val minY = loadMinX(jsonObject, type, horizon)
        val maxY = loadMaxY(jsonObject, type, horizon)

        for (position in positions){
            var newGameObject = GameObject(context, position.x, position.y,
                scale, interval, minY, maxY)
            loadSpritesToGameObject(jsonObject, newGameObject)

            gameObjects.add(newGameObject)
        }
        return gameObjects
    }

    fun makeObject(jsonObject: JSONObject,
                   context: Context,
                   scale: Float,
                   horizon: Float
    ): GameObject {
        return makeObjects(jsonObject, context, scale, horizon)[0]
    }
}