package hu.unimiskolc.iit.mobile.untitledwestern.application.engine

import hu.unimiskolc.iit.mobile.untitledwestern.application.MyGLRenderer

class C2DSceneManager {
    var mScenes: ArrayList<C2DScene> = ArrayList()

    fun registerScene(scene: C2DScene){
        mScenes.add(scene)
    }

    fun render(renderer: MyGLRenderer) {
        for(scene in mScenes){
            scene.render(renderer)
        }
    }

    fun getSceneById(Id: Int): C2DScene?{
        if (Id < mScenes.size) {
            return mScenes[Id]
        }
        return null
    }
}