package hu.iit.me.untitledwestern.engine.graph

import android.opengl.GLES30
import hu.iit.me.untitledwestern.engine.Texture2D
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Square {
    private val COORDS_PER_VERTEX = 3
    private val texture: Texture2D
    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3) // order to draw vertices
    private val vertexBuffer: FloatBuffer
    private val textureBuffer: FloatBuffer
    private val drawListBuffer: ShortBuffer

    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    init{
        drawListBuffer =
                // (# of coordinate values * 2 bytes per short)
            ByteBuffer.allocateDirect(drawOrder.size * 2).run {
                order(ByteOrder.nativeOrder())
                asShortBuffer().apply {
                    put(drawOrder)
                    position(0)
                }
            }
    }

    constructor(positions: FloatArray, textCoords: FloatArray, texture:Texture2D){
        vertexBuffer =
                // (# of coordinate values * 4 bytes per float)
            ByteBuffer.allocateDirect(positions.size * 4).run {
                order(ByteOrder.nativeOrder())
                asFloatBuffer().apply {
                    put(positions)
                    position(0)
                }
            }

        textureBuffer =
            ByteBuffer.allocateDirect(textCoords.size * 4).run {
                order(ByteOrder.nativeOrder())
                asFloatBuffer().apply {
                    put(textCoords)
                    position(0)
                }
            }

        this.texture = texture
    }

    fun draw(shaderProgram: ShaderProgram) {
        GLES30.glGetAttribLocation(shaderProgram.programId, "a_TexCoordinate").also{
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture.textureId)

            textureBuffer.position(0)
            GLES30.glVertexAttribPointer(
                it,
                2,
                GLES30.GL_FLOAT,
                false,
                0,
                textureBuffer
            )
            GLES30.glEnableVertexAttribArray(it)
        }

        shaderProgram.setVertexAttribArray("vPosition",
            vertexStride,
            vertexBuffer).also{
            GLES30.glDrawElements(
                GLES30.GL_TRIANGLES,
                drawOrder.size,
                GLES30.GL_UNSIGNED_SHORT,
                drawListBuffer
            )
            shaderProgram.disableVertexAttribArray(it)
        }
    }
}