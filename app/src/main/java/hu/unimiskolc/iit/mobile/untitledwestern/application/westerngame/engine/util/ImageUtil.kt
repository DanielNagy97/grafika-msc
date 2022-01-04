package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix

class ImageUtil {
    companion object {
        fun loadBitmap(context: Context, fileName: String): Bitmap {
            val ins = context.assets.open(fileName)
            val bitmap = BitmapFactory.decodeStream(ins)

            val flip = Matrix()
            flip.postScale(1f, -1f)

            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, flip, true)
        }
    }
}