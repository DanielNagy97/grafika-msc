package hu.iit.me.untitledwestern.game.utils

import android.content.Context
import hu.iit.me.untitledwestern.engine.GameObject
import org.json.JSONObject

class ScoreNumberLoader : GameObjectLoader() {
    override fun makeObjects(
        jsonObject: JSONObject,
        context: Context,
        scale: Float,
        horizon: Float
    ): ArrayList<GameObject> {
        var scoreNumbers: ArrayList<GameObject> = ArrayList()

        val n = loadInt("n", jsonObject)

        for (i in 1..n) {
            var newNumber = GameObject(
                context, 0.0f, 0.0f,
                scale,0.0f, 0.0f, 0.0f
            )
            loadSpritesToGameObject(jsonObject, newNumber)

            scoreNumbers.add(newNumber)
        }
        return scoreNumbers
    }
}