package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.utils

import android.content.Context
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.Collectible
import org.json.JSONObject

class CollectibleLoader: GameObjectLoader() {
    override fun makeObjects(jsonObject: JSONObject,
                             context: Context,
                             scale: Float,
                             horizon: Float
    ): ArrayList<Collectible> {
        val collectibles: ArrayList<Collectible> = ArrayList()

        val positions = loadPositions(jsonObject, horizon)
        val type = loadType(jsonObject)
        val interval = loadInterval(jsonObject, type)
        val minY = loadMinX(jsonObject, type, horizon)
        val maxY = loadMaxY(jsonObject, type, horizon)
        val value = loadInt("value", jsonObject)

        for (position in positions){
            val newCoin = Collectible(context, position.x, position.y,
                scale, interval, minY, maxY, value)
            loadSpritesToGameObject(jsonObject, newCoin)

            collectibles.add(newCoin)
        }
        return collectibles
    }
}