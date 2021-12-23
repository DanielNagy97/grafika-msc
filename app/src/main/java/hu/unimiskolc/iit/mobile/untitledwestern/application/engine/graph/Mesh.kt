package hu.unimiskolc.iit.mobile.untitledwestern.application.engine.graph

import android.opengl.GLES32.glBindVertexArray
import android.opengl.GLES32.glGenVertexArrays
import android.opengl.GLES32.glGenBuffers
import android.opengl.GLES32.glBindBuffer
import android.opengl.GLES32.glBufferData
import android.opengl.GLES32.glVertexAttribPointer
import android.opengl.GLES32.glGetAttribLocation
import android.opengl.GLES32.glActiveTexture
import android.opengl.GLES32.glBindTexture
import android.opengl.GLES32.glEnableVertexAttribArray
import android.opengl.GLES32.glDrawArrays
import android.opengl.GLES32.glDisableVertexAttribArray
import android.opengl.GLES32.GL_ARRAY_BUFFER
import android.opengl.GLES32.GL_STATIC_DRAW
import android.opengl.GLES32.GL_FLOAT
import android.opengl.GLES32.GL_TEXTURE0
import android.opengl.GLES32.GL_TEXTURE_2D
import android.opengl.GLES32.GL_TRIANGLES
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.Texture2D
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.util.BufferUtil
import java.nio.FloatBuffer

class Mesh
    (
    positions: FloatArray,
    textCoords: FloatArray,
    private var texture: Texture2D,
    private var vertexCount: Int
) {
    private var vaoId: Int
    private var vboIdList: ArrayList<Int> = ArrayList()

    init {
        //GLES20.glGetAttribLocation(shaderProgram.programId, "a_TexCoordinate").also{
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
}