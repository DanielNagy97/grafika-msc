package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine

import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.Renderer

class C2DSceneManager {
    var mScenes: ArrayList<C2DScene> = ArrayList()
    private var currentScene: Int = 0

    fun registerScene(scene: C2DScene) {
        mScenes.add(scene)
    }

    fun getCurrentScene(): C2DScene {
        return mScenes[currentScene]
    }

    fun render(renderer: Renderer) {
        if (mScenes.size != 0) {
            mScenes[currentScene].render(renderer)
        }
    }

    fun cleanup() {
        for (scene in mScenes) {
            scene.cleanup()
        }
        mScenes.clear()
    }
}