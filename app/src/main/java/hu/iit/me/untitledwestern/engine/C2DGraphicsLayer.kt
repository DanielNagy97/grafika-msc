package hu.iit.me.untitledwestern.engine

import hu.iit.me.untitledwestern.MyGLRenderer

class C2DGraphicsLayer {
    var mObjectList: ArrayList<GameObject>
    var mTextures: ArrayList<Texture2D>

    var mVisible: Boolean

    var mName: String
    var mID: Int

    var mCamera: CCamera2D? = null

    constructor(name: String, id: Int){
        this.mName = name
        this.mID = id
        mVisible = true

        mTextures = ArrayList<Texture2D>()
        mObjectList = ArrayList<GameObject>()
        mCamera = null
    }

    fun addGameObject(gameObject: GameObject){
        if (gameObject != null){
            mObjectList.add(gameObject)
        }
    }

    fun addTexture(texture: Texture2D){
        if (texture != null) {
            mTextures.add(texture)
        }
    }

    fun getTexture(index: Int): Texture2D?{
        if (index > -1 && index < mTextures.size){
            return mTextures.get(index)
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
        if(camera != null){
            mCamera = camera
        }
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