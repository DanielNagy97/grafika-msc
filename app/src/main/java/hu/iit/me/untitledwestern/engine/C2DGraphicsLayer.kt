package hu.iit.me.untitledwestern.engine

import hu.iit.me.untitledwestern.MyGLRenderer

class C2DGraphicsLayer(var mName: String, var mID: Int, var cameraSpeed: Float) {
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

    fun addTexture(texture: Texture2D){
        mTextures.add(texture)
    }

    fun getTexture(index: Int): Texture2D?{
        if (index > -1 && index < mTextures.size){
            return mTextures[index]
        }
        return null
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
            if (!gameObject.visible){
                continue
            }
            gameObject.draw(renderer)
        }
    }

    fun setCamera(camera: CCamera2D){
        mCamera = camera
    }

    fun getObjectByID(id: Int){

    }

    fun getObjectByName(name: String){

    }

    fun removeGameObjectByID(id: Int){

    }

    fun sortByZIndex(){

    }

    fun clear() {
        //TODO: Make clear functions!
    }


}