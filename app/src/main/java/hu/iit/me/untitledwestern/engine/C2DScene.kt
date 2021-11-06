package hu.iit.me.untitledwestern.engine

import hu.iit.me.untitledwestern.MyGLRenderer

class C2DScene {
    var mLayers: ArrayList<C2DGraphicsLayer>
    var mName: String = ""
    var mVisible: Boolean

    init{
        mLayers = ArrayList<C2DGraphicsLayer>()
        mName = "Sample Scene"
        mVisible = true
    }

    fun registerLayer(layer: C2DGraphicsLayer){
        if(layer != null) {
            mLayers.add(layer)
        }
    }

    fun render(renderer: MyGLRenderer) {
        if (!mVisible || mLayers.size == 0){
            return
        }

        for(layer in mLayers){
            layer.render(renderer)
        }
    }

    fun sortByZIndex(){
        for(layer in mLayers){
            layer.sortByZIndex()
        }
    }

}