package hu.iit.me.untitledwestern.engine.graph

import android.opengl.GLES30
import hu.iit.me.untitledwestern.engine.Texture2D

class Mesh {
    var vaoId: Int? = null
    lateinit var vboIdList: List<Int>
    var vertexCount: Int? = null
    lateinit var texture: Texture2D

    constructor(positions: FloatArray, textCoords: FloatArray, texture:Texture2D, numOfVertices: Int){
        this.texture = texture

        vboIdList = ArrayList()
        
    }
}