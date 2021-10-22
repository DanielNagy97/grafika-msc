package hu.iit.me.untitledwestern.engine.graph

import android.opengl.GLES20
import java.lang.Exception
import java.nio.FloatBuffer

class ShaderProgram {
    val programId: Int
    private var uniforms: HashMap<String, Int> = HashMap<String, Int>()
    private var vertexShaderId: Int = 0
    private var fragmentShaderId: Int = 0

    init {
        programId = GLES20.glCreateProgram()
        if (programId == 0) {
            //throw Exception("Could not create shader program!")
        }
    }

    fun createUniform(uniformName: String) {

        var uniformLocation: Int = GLES20.glGetUniformLocation(programId, uniformName)
        if(uniformLocation < 0) {
            //throw Exception("Could not find uniform: $uniformName")
        }
        uniforms[uniformName] = uniformLocation

    }

    fun setUniform(uniformName: String, value: FloatArray){
        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(uniforms[uniformName]!!, 1, false, value, 0)
    }

    fun setUniform(uniformName: String, value: Int){
        GLES20.glUniform1i(uniforms[uniformName]!!, value)
    }

    fun setUniform4f(uniformName: String, value: FloatArray){
        GLES20.glUniform4fv(uniforms[uniformName]!!, 1, value, 0)
    }

    fun bindAttribLocation(attribName: String, location: Int){
        GLES20.glBindAttribLocation(programId, location, attribName)
    }

    fun setVertexAttribArray(attribName: String, vertexStride: Int, vertexBuffer: FloatBuffer): Int {
        return GLES20.glGetAttribLocation(programId, attribName).also {

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                it,
                3,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )
        }
    }

    fun disableVertexAttribArray(arrayId: Int) {
        GLES20.glDisableVertexAttribArray(arrayId)
    }

    fun createVertexShader(shaderCode: String) {
        vertexShaderId = createShader(shaderCode, GLES20.GL_VERTEX_SHADER)
    }

    fun createFragmentShader(shaderCode: String) {
        fragmentShaderId = createShader(shaderCode, GLES20.GL_FRAGMENT_SHADER)
    }

    private fun createShader(shaderCode: String, shaderType: Int): Int {
        return GLES20.glCreateShader(shaderType).also { shaderId ->
            if(shaderId == 0) {
                //throw Exception("Error creating shader. Id: $shaderId")
            }
            // add the source code to the shader and compile it
            GLES20.glShaderSource(shaderId, shaderCode)
            GLES20.glCompileShader(shaderId)

            //TODO: Check if compilation was successful!! glGetShaderiv?

            GLES20.glAttachShader(programId, shaderId)
        }
    }

    fun link() {
        GLES20.glLinkProgram(programId)
        //TODO: Check if linking was successful, glGetProgrami

        if (vertexShaderId != 0) {
            GLES20.glDetachShader(programId, vertexShaderId)
        }

        if (fragmentShaderId != 0) {
            GLES20.glDetachShader(programId, fragmentShaderId)
        }

        GLES20.glValidateProgram(programId)
        //TODO: Check validate status!
    }

    fun bind() {
        GLES20.glUseProgram(programId)
    }

    fun unbind() {
        GLES20.glUseProgram(0)
    }

    fun cleanup() {
        unbind()
        if (programId != 0) {
            GLES20.glDeleteProgram(programId)
        }
    }
}