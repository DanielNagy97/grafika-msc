package hu.iit.me.untitledwestern.engine.graph

import android.opengl.GLES30
import hu.iit.me.untitledwestern.engine.Texture2D
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Mesh {
    var vaoId: Int
    var vboIdList: ArrayList<Int>
    var vertexCount: Int
    var texture: Texture2D

    constructor(positions: FloatArray, textCoords: FloatArray, texture:Texture2D, numOfVertices: Int){
        this.texture = texture

        this.vertexCount = numOfVertices

        vboIdList = ArrayList()

        var vaos: IntArray = IntArray(1)
        GLES30.glGenVertexArrays(1, vaos, 0)
        vaoId = vaos[0]
        GLES30.glBindVertexArray(vaoId)

        // Texture VBO
        var vboID = IntArray(1)
        GLES30.glGenBuffers(1, vboID, 0)
        vboIdList.add(vboID[0])
        var textureBuffer =
            ByteBuffer.allocateDirect(textCoords.size * 4).run {
                order(ByteOrder.nativeOrder())
                asFloatBuffer().apply {
                    put(textCoords)
                    position(0)
                }
            }
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vboID[0])
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, textureBuffer.capacity() * 4, textureBuffer, GLES30.GL_STATIC_DRAW)

        // attributeNumber, coordinateSize
        GLES30.glVertexAttribPointer(0, 2, GLES30.GL_FLOAT, false, 0, 0)

        // Position VBO
        vboID = IntArray(1)
        GLES30.glGenBuffers(1, vboID, 0)
        vboIdList.add(vboID[0])

        var posBuffer =
            ByteBuffer.allocateDirect(positions.size * 4).run {
                order(ByteOrder.nativeOrder())
                asFloatBuffer().apply {
                    put(positions)
                    position(0)
                }
            }
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vboID[0])
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, posBuffer.capacity() * 4, posBuffer, GLES30.GL_STATIC_DRAW)
        GLES30.glVertexAttribPointer(1, 3, GLES30.GL_FLOAT, false, 0, 0)

        GLES30.glBindVertexArray(0)
    }

    fun draw(shaderProgram: ShaderProgram) {
        // Activate texture unit
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        // Bind the texture
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture.textureId)

        // Draw the mesh
        GLES30.glBindVertexArray(vaoId)
        GLES30.glEnableVertexAttribArray(0)
        GLES30.glEnableVertexAttribArray(1)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount)

        // Restore state
        GLES30.glDisableVertexAttribArray(0)
        GLES30.glDisableVertexAttribArray(1)
        GLES30.glBindVertexArray(0)
    }
}