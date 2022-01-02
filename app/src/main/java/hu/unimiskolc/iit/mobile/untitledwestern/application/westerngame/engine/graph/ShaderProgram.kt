package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.graph

import android.opengl.GLES32.glCreateProgram
import android.opengl.GLES32.glGetUniformLocation
import android.opengl.GLES32.glUniformMatrix4fv
import android.opengl.GLES32.glUniform1i
import android.opengl.GLES32.glUniform4fv
import android.opengl.GLES32.glBindAttribLocation
import android.opengl.GLES32.glCreateShader
import android.opengl.GLES32.glShaderSource
import android.opengl.GLES32.glCompileShader
import android.opengl.GLES32.glGetShaderiv
import android.opengl.GLES32.glGetProgramInfoLog
import android.opengl.GLES32.glAttachShader
import android.opengl.GLES32.glLinkProgram
import android.opengl.GLES32.glGetProgramiv
import android.opengl.GLES32.glDetachShader
import android.opengl.GLES32.glValidateProgram
import android.opengl.GLES32.glUseProgram
import android.opengl.GLES32.glDeleteProgram
import android.opengl.GLES32.GL_VERTEX_SHADER
import android.opengl.GLES32.GL_FRAGMENT_SHADER
import android.opengl.GLES32.GL_COMPILE_STATUS
import android.opengl.GLES32.GL_LINK_STATUS
import android.opengl.GLES32.GL_VALIDATE_STATUS
import java.lang.Exception

class ShaderProgram {
    val programId: Int = glCreateProgram()
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
        val uniformLocation: Int = glGetUniformLocation(programId, uniformName)
        if(uniformLocation < 0) {
            throw Exception("Could not find uniform: $uniformName")
        }
        uniforms[uniformName] = uniformLocation
    }

    fun setUniform(uniformName: String, value: FloatArray){
        glUniformMatrix4fv(uniforms[uniformName]!!, 1, false, value, 0)
    }

    fun setUniform(uniformName: String, value: Int){
        glUniform1i(uniforms[uniformName]!!, value)
    }

    fun setUniform4fv(uniformName: String, value: FloatArray){
        glUniform4fv(uniforms[uniformName]!!, 1, value, 0)
    }

    fun bindAttribLocation(attribName: String, location: Int){
        glBindAttribLocation(programId, location, attribName)
    }

    fun createVertexShader(shaderCode: String) {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER)
    }

    fun createFragmentShader(shaderCode: String) {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER)
    }

    private fun createShader(shaderCode: String, shaderType: Int): Int {
        return glCreateShader(shaderType).also { shaderId ->
            if(shaderId == 0) {
                throw Exception("Error creating shader. Id: $shaderId")
            }

            glShaderSource(shaderId, shaderCode)
            glCompileShader(shaderId)

            val compileStatus = IntArray(1)
            glGetShaderiv(shaderId, GL_COMPILE_STATUS, compileStatus, 0)
            if(compileStatus[0] == 0){
                throw Exception("Error compiling shader code: ${glGetProgramInfoLog(shaderId)}")
            }

            glAttachShader(programId, shaderId)
        }
    }

    fun link() {
        glLinkProgram(programId)

        val linkStatus = IntArray(1)
        glGetProgramiv(programId, GL_LINK_STATUS, linkStatus, 0)
        if(linkStatus[0] == 0){
            throw Exception("Error linking shader code: ${glGetProgramInfoLog(programId)}")
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId)
        }

        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId)
        }

        glValidateProgram(programId)

        val validateStatus = IntArray(1)
        glGetProgramiv(programId, GL_VALIDATE_STATUS, validateStatus, 0)
        if(validateStatus[0] == 0){
            throw Exception("Error validating shader code: ${glGetProgramInfoLog(programId)}")
        }
    }

    fun bind() {
        glUseProgram(programId)
    }

    fun unbind() {
        glUseProgram(0)
    }

    fun cleanup() {
        unbind()
        if (programId != 0) {
            glDeleteProgram(programId)
        }
    }
}