package hu.unimiskolc.iit.mobile.untitledwestern.application.game.utils

import android.content.Context
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.GameObject
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