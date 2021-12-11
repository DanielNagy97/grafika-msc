package hu.iit.me.untitledwestern.game.utils

import android.content.Context
import hu.iit.me.untitledwestern.game.Coin
import org.json.JSONObject

class CoinLoader: GameObjectLoader() {
    override fun makeObjects(jsonObject: JSONObject,
                             context: Context,
                             scale: Float,
                             horizon: Float
    ): ArrayList<Coin> {
        var coins: ArrayList<Coin> = ArrayList()

        val positions = loadPositions(jsonObject, horizon)
        val type = loadType(jsonObject)
        val interval = loadInterval(jsonObject, type)
        val minY = loadMinX(jsonObject, type, horizon)
        val maxY = loadMaxY(jsonObject, type, horizon)
        val value = loadInt("value", jsonObject)

        for (position in positions){
            var newCoin = Coin(context, position.x, position.y,
                scale, interval, minY, maxY, value)
            loadSpritesToGameObject(jsonObject, newCoin)

            coins.add(newCoin)
        }
        return coins
    }
}