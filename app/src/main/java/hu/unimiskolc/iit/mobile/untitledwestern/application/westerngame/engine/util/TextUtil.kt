package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.util

import android.content.Context

class TextUtil {
    companion object {
        fun readFile(context: Context, fileName: String): String {
            return context.assets.open(fileName).bufferedReader().use { it.readText() }
        }
    }
}
