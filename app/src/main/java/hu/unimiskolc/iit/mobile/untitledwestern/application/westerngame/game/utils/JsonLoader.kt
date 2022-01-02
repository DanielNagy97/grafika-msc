package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.utils

import org.json.JSONArray
import org.json.JSONObject

open class JsonLoader {
    protected fun loadString(name: String, jsonObject: JSONObject): String {
        return jsonObject.getString(name)
    }

    protected fun loadInt(name: String, jsonObject: JSONObject): Int {
        return jsonObject.getInt(name)
    }

    protected fun loadFloat(name: String, jsonObject: JSONObject): Float {
        return jsonObject.getDouble(name).toFloat()
    }

    protected fun loadArray(name: String, jsonObject: JSONObject): JSONArray {
        return jsonObject.getJSONArray(name)
    }
}