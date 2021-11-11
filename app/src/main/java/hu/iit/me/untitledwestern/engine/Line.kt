package hu.iit.me.untitledwestern.engine

import android.opengl.GLES30
import hu.iit.me.untitledwestern.MyGLRenderer
import hu.iit.me.untitledwestern.engine.math.Vector2D
import hu.iit.me.untitledwestern.engine.util.BufferUtil
import java.nio.FloatBuffer

class Line {
    private val VertexBuffer: FloatBuffer
    private val VertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    var color = floatArrayOf(0.9f, 0.1f, 0.0f, 1.0f)

    companion object {
        const val COORDS_PER_VERTEX = 3
        var minpoint = Vector2D(0f, 0.0f)
        var maxpoint = Vector2D(0.6f, 0.8f)
        var LineCoords = floatArrayOf(
            minpoint.x, minpoint.y, 0.0f,
            minpoint.x, maxpoint.y, 0.0f,
            maxpoint.x, maxpoint.y, 0.0f,
            maxpoint.x, minpoint.y, 0.0f,
            minpoint.x, minpoint.y, 0.0f
        )
    }

    init {
        VertexBuffer = BufferUtil.createFloatBuffer(LineCoords)
    }

    fun draw(renderer: MyGLRenderer) {
        renderer.lineShader.bind()

        renderer.lineShader.setUniform("projectionMatrix", renderer.projectionMatrix)
        renderer.lineShader.setUniform("modelMatrix", renderer.viewMatrix)
        renderer.lineShader.setUniform4fv("vColor", color)

        var posAttrib = GLES30.glGetAttribLocation(renderer.lineShader.programId, "vPosition")
        GLES30.glEnableVertexAttribArray(posAttrib)
        GLES30.glVertexAttribPointer(
            posAttrib, COORDS_PER_VERTEX,
            GLES30.GL_FLOAT, false,
            VertexStride, VertexBuffer
        )

        GLES30.glEnableVertexAttribArray(posAttrib)
        GLES30.glLineWidth(4.0f)
        GLES30.glDrawArrays(GLES30.GL_LINE_STRIP, 0, 5)
        GLES30.glDisableVertexAttribArray(posAttrib)

        renderer.lineShader.unbind()
    }
}