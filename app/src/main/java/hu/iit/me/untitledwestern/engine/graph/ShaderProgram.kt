package hu.iit.me.untitledwestern.engine.graph

import android.opengl.GLES30
import java.lang.Exception

class ShaderProgram {
    val programId: Int
    private var uniforms: HashMap<String, Int>
    private var vertexShaderId: Int = 0
    private var fragmentShaderId: Int = 0

    init {
        programId = GLES30.glCreateProgram()
        if (programId == 0) {
            throw Exception("Could not create shader program!")
        }
        uniforms = HashMap<String, Int>()
    }

    fun createUniform(uniformName: String) {
        var uniformLocation: Int = GLES30.glGetUniformLocation(programId, uniformName)
        if(uniformLocation < 0) {
            throw Exception("Could not find uniform: $uniformName")
        }
        uniforms[uniformName] = uniformLocation

    }

    fun setUniform(uniformName: String, value: FloatArray){
        GLES30.glUniformMatrix4fv(uniforms[uniformName]!!, 1, false, value, 0)
    }

    fun setUniform(uniformName: String, value: Int){
        GLES30.glUniform1i(uniforms[uniformName]!!, value)
    }

    fun setUniform4fv(uniformName: String, value: FloatArray){
        GLES30.glUniform4fv(uniforms[uniformName]!!, 1, value, 0)
    }

    fun bindAttribLocation(attribName: String, location: Int){
        GLES30.glBindAttribLocation(programId, location, attribName)
    }

    fun createVertexShader(shaderCode: String) {
        vertexShaderId = createShader(shaderCode, GLES30.GL_VERTEX_SHADER)
    }

    fun createFragmentShader(shaderCode: String) {
        fragmentShaderId = createShader(shaderCode, GLES30.GL_FRAGMENT_SHADER)
    }

    private fun createShader(shaderCode: String, shaderType: Int): Int {
        return GLES30.glCreateShader(shaderType).also { shaderId ->
            if(shaderId == 0) {
                throw Exception("Error creating shader. Id: $shaderId")
            }

            GLES30.glShaderSource(shaderId, shaderCode)
            GLES30.glCompileShader(shaderId)

            val compileStatus : IntArray = IntArray(1)
            GLES30.glGetShaderiv(shaderId, GLES30.GL_COMPILE_STATUS, compileStatus, 0)
            if(compileStatus[0] == 0){
                throw Exception("Error compiling shader code: ${GLES30.glGetProgramInfoLog(shaderId)}")
            }

            GLES30.glAttachShader(programId, shaderId)
        }
    }

    fun link() {
        GLES30.glLinkProgram(programId)

        val linkStatus : IntArray = IntArray(1)
        GLES30.glGetProgramiv(programId, GLES30.GL_LINK_STATUS, linkStatus, 0)
        if(linkStatus[0] == 0){
            throw Exception("Error linking shader code: ${GLES30.glGetProgramInfoLog(programId)}")
        }

        if (vertexShaderId != 0) {
            GLES30.glDetachShader(programId, vertexShaderId)
        }

        if (fragmentShaderId != 0) {
            GLES30.glDetachShader(programId, fragmentShaderId)
        }

        GLES30.glValidateProgram(programId)

        val validateStatus : IntArray = IntArray(1)
        GLES30.glGetProgramiv(programId, GLES30.GL_VALIDATE_STATUS, validateStatus, 0)
        if(validateStatus[0] == 0){
            throw Exception("Error validating shader code: ${GLES30.glGetProgramInfoLog(programId)}")
        }
    }

    fun bind() {
        GLES30.glUseProgram(programId)
    }

    fun unbind() {
        GLES30.glUseProgram(0)
    }

    fun cleanup() {
        unbind()
        if (programId != 0) {
            GLES30.glDeleteProgram(programId)
        }
    }
}