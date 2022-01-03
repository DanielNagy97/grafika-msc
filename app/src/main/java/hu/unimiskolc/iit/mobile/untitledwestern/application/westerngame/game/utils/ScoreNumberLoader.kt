package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.utils

import android.content.Context
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.GameObject
import org.json.JSONObject

class ScoreNumberLoader : GameObjectLoader() {
    override fun makeObjects(
        jsonObject: JSONObject,
        context: Context,
        scale: Float,
        horizon: Float
    ): ArrayList<GameObject> {
        val scoreNumbers: ArrayList<GameObject> = ArrayList()

        val n = loadInt("n", jsonObject)

        for (i in 1..n) {
            val newNumber = GameObject(
                context, 0.0f, 0.0f,
                scale,0.0f, 0.0f, 0.0f
            )
            loadSpritesToGameObject(jsonObject, newNumber)

            scoreNumbers.add(newNumber)
        }
        return scoreNumbers
    }
}