package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.util

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class BufferUtil {
    companion object {
        fun createFloatBuffer(inputArray: FloatArray): FloatBuffer {
            return ByteBuffer.allocateDirect(inputArray.size * 4).run {
                order(ByteOrder.nativeOrder())
                asFloatBuffer().apply {
                    put(inputArray).flip()
                    position(0)
                }
            }
        }
    }
}