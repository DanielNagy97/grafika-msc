package hu.iit.me.untitledwestern.engine.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class ImageUtil {
    companion object{
        fun loadBitmap(context: Context, fileName: String): Bitmap{
            var ins = context.assets.open(fileName)
            return BitmapFactory.decodeStream(ins)
        }
    }
}