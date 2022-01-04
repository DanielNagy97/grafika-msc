package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.graph

import android.opengl.GLES32.*
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.Texture2D
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.util.BufferUtil
import java.nio.FloatBuffer

class Mesh(
    positions: FloatArray,
    textCoords: FloatArray,
    private var texture: Texture2D,
    private var vertexCount: Int
) {
    private var vaoId: Int
    private var vboIdList: ArrayList<Int> = ArrayList()

    init {
        vaoId = createVao()
        glBindVertexArray(vaoId)

        val textureBuffer = BufferUtil.createFloatBuffer(textCoords)
        vboIdList.add(createVbo(textureBuffer, 0, 2))

        val positionBuffer = BufferUtil.createFloatBuffer(positions)
        vboIdList.add(createVbo(positionBuffer, 1, 3))

        glBindVertexArray(0)
    }

    private fun createVao(): Int {
        val vaos = IntArray(1)
        glGenVertexArrays(1, vaos, 0)
        return vaos[0]
    }

    private fun createVbo(buffer: FloatBuffer, attribute: Int, size: Int): Int {
        val vboID = IntArray(1)
        glGenBuffers(1, vboID, 0)

        glBindBuffer(GL_ARRAY_BUFFER, vboID[0])

        glBufferData(GL_ARRAY_BUFFER, buffer.capacity() * 4, buffer, GL_STATIC_DRAW)

        glVertexAttribPointer(attribute, size, GL_FLOAT, false, 0, 0)

        glBindBuffer(GL_ARRAY_BUFFER, 0)

        return vboID[0]
    }

    fun draw(shaderProgram: ShaderProgram) {
        val textAttrib = glGetAttribLocation(shaderProgram.programId, "a_TexCoordinate")
        val posAttrib = glGetAttribLocation(shaderProgram.programId, "vPosition")

        // Activate texture unit
        glActiveTexture(GL_TEXTURE0)

        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, texture.textureId)

        // Draw the mesh
        glBindVertexArray(vaoId)
        glEnableVertexAttribArray(textAttrib)
        glEnableVertexAttribArray(posAttrib)

        glDrawArrays(GL_TRIANGLES, 0, vertexCount)

        // Restore state
        glDisableVertexAttribArray(textAttrib)
        glDisableVertexAttribArray(posAttrib)
        glBindVertexArray(0)
    }

    fun cleanup() {
        glDisableVertexAttribArray(0)

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glDeleteBuffers(vboIdList.size, vboIdList.toIntArray(), 0)
        vboIdList.clear()

        // Delete the VAO
        glBindVertexArray(0)
        glDeleteVertexArrays(1, intArrayOf(vaoId), 0)
    }
}