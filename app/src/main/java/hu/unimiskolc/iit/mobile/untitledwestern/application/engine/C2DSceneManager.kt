package hu.unimiskolc.iit.mobile.untitledwestern.application.engine

import hu.unimiskolc.iit.mobile.untitledwestern.application.MyGLRenderer

class C2DSceneManager {
    var mScenes: ArrayList<C2DScene> = ArrayList()
    var currentScene: Int = 0

    fun registerScene(scene: C2DScene){
        mScenes.add(scene)
    }

    fun getCurrentScene(): C2DScene {
        return mScenes[currentScene]
    }

    fun render(renderer: MyGLRenderer) {
        mScenes[currentScene].render(renderer)
    }
}