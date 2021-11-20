package hu.iit.me.untitledwestern.engine.graph

import android.opengl.GLES32
import hu.iit.me.untitledwestern.engine.Texture2D
import hu.iit.me.untitledwestern.engine.util.BufferUtil
import java.nio.FloatBuffer

class Mesh
    (
    positions: FloatArray,
    textCoords: FloatArray,
    private var texture: Texture2D,
    numOfVertices: Int
) {
    private var vaoId: Int
    private var vboIdList: ArrayList<Int> = ArrayList()
    private var vertexCount: Int = numOfVertices

    init {
        //GLES20.glGetAttribLocation(shaderProgram.programId, "a_TexCoordinate").also{
        vaoId = createVao()
        GLES32.glBindVertexArray(vaoId)
        val textureBuffer = BufferUtil.createFloatBuffer(textCoords)
        vboIdList.add(createVbo(textureBuffer, 0, 2))
        val positionBuffer = BufferUtil.createFloatBuffer(positions)
        vboIdList.add(createVbo(positionBuffer, 1, 3))
        GLES32.glBindVertexArray(0)
    }

    private fun createVao(): Int {
        val vaos = IntArray(1)
        GLES32.glGenVertexArrays(1, vaos, 0)
        return vaos[0]
    }

    private fun createVbo(buffer: FloatBuffer, attribute: Int, size: Int): Int {
        val vboID = IntArray(1)
        GLES32.glGenBuffers(1, vboID, 0)

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vboID[0])

        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, buffer.capacity() * 4, buffer, GLES32.GL_STATIC_DRAW)

        GLES32.glVertexAttribPointer(attribute, size, GLES32.GL_FLOAT, false, 0, 0)

        // Don't know why, but this line solved the line-drawing problem??
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0)

        return vboID[0]
    }

    fun draw(shaderProgram: ShaderProgram) {
        val textAttrib = GLES32.glGetAttribLocation(shaderProgram.programId, "a_TexCoordinate")
        val posAttrib = GLES32.glGetAttribLocation(shaderProgram.programId, "vPosition")
        // Activate texture unit
        GLES32.glActiveTexture(GLES32.GL_TEXTURE0)
        // Bind the texture
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, texture.textureId)

        // Draw the mesh
        GLES32.glBindVertexArray(vaoId)
        GLES32.glEnableVertexAttribArray(textAttrib)
        GLES32.glEnableVertexAttribArray(posAttrib)

        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, vertexCount)

        // Restore state
        GLES32.glDisableVertexAttribArray(textAttrib)
        GLES32.glDisableVertexAttribArray(posAttrib)
        GLES32.glBindVertexArray(0)
    }
}