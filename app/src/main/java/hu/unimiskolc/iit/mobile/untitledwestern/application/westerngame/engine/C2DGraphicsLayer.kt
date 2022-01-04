package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine

import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.Renderer
import java.util.Random

class C2DGraphicsLayer(var cameraSpeed: Float) {
    var mObjectList: ArrayList<GameObject> = ArrayList()

    private var mVisible: Boolean = true

    var mCamera: CCamera2D? = null
    var color = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)

    init {
        mCamera = null
    }

    fun addGameObject(gameObject: GameObject) {
        mObjectList.add(gameObject)
    }

    fun addGameObjects(gameObjects: List<GameObject>) {
        for (gameObject in gameObjects) {
            addGameObject(gameObject)
        }
    }

    fun addListOfGameObjectLists(gameObjectsList: List<List<GameObject>>){
        for (gameObjects in gameObjectsList){
            addGameObjects(gameObjects)
        }
    }

    fun render(renderer: Renderer) {
        if (!mVisible) {
            return
        }

        if (mCamera != null) {
            mCamera!!.setViewMatrix(renderer.viewMatrix)
        }

        for (gameObject in mObjectList) {
            gameObject.color = color
            if (gameObject.repeatingInterval > 0.0f) {
                if (mCamera!!.viewPort.minpoint.x > gameObject.getBoundingBox().maxpoint.x) {
                    gameObject.position.x = rand(
                        mCamera!!.viewPort.maxpoint.x,
                        mCamera!!.viewPort.maxpoint.x + gameObject.repeatingInterval * 10
                    )
                    gameObject.position.y = rand(gameObject.minRepeatY, gameObject.maxRepeatY)
                }
            }

            if (!gameObject.visible || !mCamera!!.viewPort.checkOverlapping(gameObject.getBoundingBox())) {
                continue
            }
            gameObject.draw(renderer)
        }
    }

    fun setCamera(camera: CCamera2D) {
        mCamera = camera
    }

    fun cleanup() {
        for (gameObject in mObjectList) {
            gameObject.cleanup()
        }
        mObjectList.clear()
    }

    private fun rand(from: Float, to: Float): Float {
        val random = Random()
        return from + random.nextFloat() * (to - from)
    }

}