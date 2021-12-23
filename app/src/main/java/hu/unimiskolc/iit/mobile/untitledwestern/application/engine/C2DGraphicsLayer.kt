package hu.unimiskolc.iit.mobile.untitledwestern.application.engine

import hu.unimiskolc.iit.mobile.untitledwestern.application.MyGLRenderer
import java.util.Random

class C2DGraphicsLayer(var cameraSpeed: Float) {
    var mObjectList: ArrayList<GameObject> = ArrayList()
    private var mTextures: ArrayList<Texture2D> = ArrayList()

    private var mVisible: Boolean = true

    var mCamera: CCamera2D? = null

    init {
        mCamera = null
    }

    fun addGameObject(gameObject: GameObject){
        mObjectList.add(gameObject)
    }

    fun addGameObjects(gameObjects: List<GameObject>) {
        for (gameObject in gameObjects) {
            addGameObject(gameObject)
        }
    }

    fun addTexture(texture: Texture2D){
        mTextures.add(texture)
    }

    fun render(renderer: MyGLRenderer) {
        if (!mVisible) {
            return
        }

        if(mCamera != null){
             mCamera!!.setViewMatrix(renderer.viewMatrix)
        }

        for(texture in mTextures){
            texture.draw(renderer)
        }

        for(gameObject in mObjectList){
            if(gameObject.repeatingInterval > 0.0f){
                if(mCamera!!.viewPort.minpoint.x > gameObject.getBoundingBox().maxpoint.x){
                    gameObject.position.x = rand(mCamera!!.viewPort.maxpoint.x, mCamera!!.viewPort.maxpoint.x + gameObject.repeatingInterval*10)
                    gameObject.position.y = rand(gameObject.minRepeatY, gameObject.maxRepeatY)
                }
            }

            if (!gameObject.visible || !mCamera!!.viewPort.checkOverlapping(gameObject.getBoundingBox())){
                continue
            }
            gameObject.draw(renderer)
        }
    }

    fun setCamera(camera: CCamera2D){
        mCamera = camera
    }

    fun clear() {
        //TODO: Make clear functions!
    }

    fun rand(from: Float, to: Float) : Float{
        val random = Random()
        return from + random.nextFloat() * (to - from)
    }

}