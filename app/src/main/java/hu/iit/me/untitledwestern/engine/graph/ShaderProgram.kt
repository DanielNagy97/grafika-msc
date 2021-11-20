package hu.iit.me.untitledwestern.engine.graph

import android.opengl.GLES32
import java.lang.Exception

class ShaderProgram {
    val programId: Int = GLES32.glCreateProgram()
    private var uniforms: HashMap<String, Int>
    private var vertexShaderId: Int = 0
    private var fragmentShaderId: Int = 0

    init {
        if (programId == 0) {
            throw Exception("Could not create shader program!")
        }
        uniforms = HashMap()
    }

    fun createUniform(uniformName: String) {
        val uniformLocation: Int = GLES32.glGetUniformLocation(programId, uniformName)
        if(uniformLocation < 0) {
            throw Exception("Could not find uniform: $uniformName")
        }
        uniforms[uniformName] = uniformLocation
    }

    fun setUniform(uniformName: String, value: FloatArray){
        GLES32.glUniformMatrix4fv(uniforms[uniformName]!!, 1, false, value, 0)
    }

    fun setUniform(uniformName: String, value: Int){
        GLES32.glUniform1i(uniforms[uniformName]!!, value)
    }

    fun setUniform4fv(uniformName: String, value: FloatArray){
        GLES32.glUniform4fv(uniforms[uniformName]!!, 1, value, 0)
    }

    fun bindAttribLocation(attribName: String, location: Int){
        GLES32.glBindAttribLocation(programId, location, attribName)
    }

    fun createVertexShader(shaderCode: String) {
        vertexShaderId = createShader(shaderCode, GLES32.GL_VERTEX_SHADER)
    }

    fun createFragmentShader(shaderCode: String) {
        fragmentShaderId = createShader(shaderCode, GLES32.GL_FRAGMENT_SHADER)
    }

    private fun createShader(shaderCode: String, shaderType: Int): Int {
        return GLES32.glCreateShader(shaderType).also { shaderId ->
            if(shaderId == 0) {
                throw Exception("Error creating shader. Id: $shaderId")
            }

            GLES32.glShaderSource(shaderId, shaderCode)
            GLES32.glCompileShader(shaderId)

            val compileStatus = IntArray(1)
            GLES32.glGetShaderiv(shaderId, GLES32.GL_COMPILE_STATUS, compileStatus, 0)
            if(compileStatus[0] == 0){
                throw Exception("Error compiling shader code: ${GLES32.glGetProgramInfoLog(shaderId)}")
            }

            GLES32.glAttachShader(programId, shaderId)
        }
    }

    fun link() {
        GLES32.glLinkProgram(programId)

        val linkStatus = IntArray(1)
        GLES32.glGetProgramiv(programId, GLES32.GL_LINK_STATUS, linkStatus, 0)
        if(linkStatus[0] == 0){
            throw Exception("Error linking shader code: ${GLES32.glGetProgramInfoLog(programId)}")
        }

        if (vertexShaderId != 0) {
            GLES32.glDetachShader(programId, vertexShaderId)
        }

        if (fragmentShaderId != 0) {
            GLES32.glDetachShader(programId, fragmentShaderId)
        }

        GLES32.glValidateProgram(programId)

        val validateStatus = IntArray(1)
        GLES32.glGetProgramiv(programId, GLES32.GL_VALIDATE_STATUS, validateStatus, 0)
        if(validateStatus[0] == 0){
            throw Exception("Error validating shader code: ${GLES32.glGetProgramInfoLog(programId)}")
        }
    }

    fun bind() {
        GLES32.glUseProgram(programId)
    }

    fun unbind() {
        GLES32.glUseProgram(0)
    }

    fun cleanup() {
        unbind()
        if (programId != 0) {
            GLES32.glDeleteProgram(programId)
        }
    }
}