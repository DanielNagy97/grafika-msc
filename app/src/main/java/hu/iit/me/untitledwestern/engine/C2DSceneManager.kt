package hu.iit.me.untitledwestern.engine

import hu.iit.me.untitledwestern.MyGLRenderer

class C2DSceneManager {
    var mScenes: ArrayList<C2DScene>

    init{
        mScenes = ArrayList()
    }

    fun registerScene(scene: C2DScene){
        if (scene != null){
            mScenes.add(scene)
        }
    }

    fun render(renderer: MyGLRenderer) {
        for(scene in mScenes){
            scene.render(renderer)
        }
    }

    fun getSceneById(Id: Int): C2DScene?{
        if (Id < mScenes.size) {
            return mScenes.get(Id)
        }
        return null
    }
}