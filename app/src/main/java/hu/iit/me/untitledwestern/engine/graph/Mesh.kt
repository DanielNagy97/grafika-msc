package hu.iit.me.untitledwestern.engine.graph

import android.opengl.GLES30
import hu.iit.me.untitledwestern.engine.Texture2D
import hu.iit.me.untitledwestern.engine.util.BufferUtil
import java.nio.FloatBuffer

class Mesh {
    private var vaoId: Int
    private var vboIdList: ArrayList<Int>
    private var vertexCount: Int
    private var texture: Texture2D

    constructor(positions: FloatArray, textCoords: FloatArray, texture:Texture2D, numOfVertices: Int){
        this.texture = texture
        this.vertexCount = numOfVertices
        vboIdList = ArrayList()

        vaoId = createVao()
        GLES30.glBindVertexArray(vaoId)

        //GLES20.glGetAttribLocation(shaderProgram.programId, "a_TexCoordinate").also{
        var textureBuffer = BufferUtil.createFloatBuffer(textCoords)
        vboIdList.add(createVbo(textureBuffer, 0, 2))

        var positionBuffer = BufferUtil.createFloatBuffer(positions)
        vboIdList.add(createVbo(positionBuffer, 1, 3))

        GLES30.glBindVertexArray(0)
    }

    private fun createVao(): Int {
        var vaos = IntArray(1)
        GLES30.glGenVertexArrays(1, vaos, 0)
        return vaos[0]
    }

    private fun createVbo(buffer: FloatBuffer, attribute: Int, size: Int): Int {
        var vboID = IntArray(1)
        GLES30.glGenBuffers(1, vboID, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vboID[0])
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, buffer.capacity() * 4, buffer, GLES30.GL_STATIC_DRAW)

        GLES30.glVertexAttribPointer(attribute, size, GLES30.GL_FLOAT, false, 0, 0)
        return vboID[0]
    }

    fun draw(shaderProgram: ShaderProgram) {
        var textAttrib = GLES30.glGetAttribLocation(shaderProgram.programId, "a_TexCoordinate")
        var posAttrib = GLES30.glGetAttribLocation(shaderProgram.programId, "vPosition")
        // Activate texture unit
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        // Bind the texture
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture.textureId)

        // Draw the mesh
        GLES30.glBindVertexArray(vaoId)
        GLES30.glEnableVertexAttribArray(textAttrib)
        GLES30.glEnableVertexAttribArray(posAttrib)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount)

        // Restore state
        GLES30.glDisableVertexAttribArray(textAttrib)
        GLES30.glDisableVertexAttribArray(posAttrib)
        GLES30.glBindVertexArray(0)
    }
}