package hu.iit.me.untitledwestern

import android.content.Context
import hu.iit.me.untitledwestern.engine.*
import hu.iit.me.untitledwestern.engine.math.Vector2D
import hu.iit.me.untitledwestern.engine.util.TextUtil
import org.json.JSONObject
import org.json.JSONTokener

class DummyGame(private var context: Context, var renderer: MyGLRenderer, private val scale: Float) {

    var sceneManager: C2DSceneManager = C2DSceneManager()
    private var scene: C2DScene = C2DScene()

    private lateinit var mPlayerObject: GameObject
    private lateinit var mPistolObject: GameObject

    lateinit var platforms: List<BoundingBox2D>
    private var layers: ArrayList<C2DGraphicsLayer> = ArrayList()

    var idle = true
    var shooting = false
    var jumping = false
    var falling = true
    private var onPlatform : BoundingBox2D? = null

    var velocity = 1.5f

    var speedX = 0f
    private var speedY: Float = 0f
    var xdir = 1f
    private var ydir = -1f

    private var ground: Float = -75f
    private var horizon: Float = -21f

    fun init(){
        val sceneModel = JSONTokener(TextUtil.readFile(context, "sceneJson/scene01.json")).nextValue() as JSONObject

        mPlayerObject = makeGameObject(sceneModel.getJSONObject("player"))
        mPistolObject = makeGameObject(sceneModel.getJSONObject("player").getJSONObject("pistol"))

        horizon = sceneModel.getDouble("horizon").toString().toFloat()
        ground = sceneModel.getDouble("ground").toString().toFloat()

        val layerModels = sceneModel.getJSONArray("layers")
        for (i in 0 until layerModels.length()){
            layers.add(makeLayer(layerModels.getJSONObject(i)))
        }

        layers.last().addGameObject(mPlayerObject)
        layers.last().addGameObject(mPistolObject)

        for(layer in layers){
            scene.registerLayer(layer)
        }
        sceneManager.registerScene(scene)


        platforms = listOf(BoundingBox2D(Vector2D(-60f, -50f), Vector2D(0f, -40f)),
            BoundingBox2D(Vector2D(0f, -10f), Vector2D(60f, 0f)),
            BoundingBox2D(Vector2D(-180f, -10f), Vector2D(-120f, 0f)))

        for(plat in platforms){
            plat.mEnabled = true
        }
    }

    private fun makeLayer(jsonObject: JSONObject): C2DGraphicsLayer{
        var newLayer: C2DGraphicsLayer

        val name = jsonObject.getString("name")
        val cameraSpeed = jsonObject.getDouble("cameraSpeed").toString().toFloat()
        newLayer = C2DGraphicsLayer(name, 0, cameraSpeed)

        val layerGameObjects = jsonObject.getJSONArray("gameObjects")
        for (i in 0 until layerGameObjects.length()) {
            val newObj = makeGameObject(layerGameObjects.getJSONObject(i))
            newLayer.addGameObject(newObj)
        }
        newLayer.setCamera(CCamera2D(0f, 0f, 11))

        return newLayer
    }

    private fun makeGameObject(jsonObject: JSONObject): GameObject{
        var newGameObject: GameObject
        val pPosition = jsonObject.getJSONArray("position")
        val pSprites = jsonObject.getJSONArray("sprites")

        var posY = if(pPosition[1].toString() == "horizon"){
            horizon
        } else{
            pPosition[1].toString().toFloat()
        }

        newGameObject = GameObject(context, pPosition[0].toString().toFloat(), posY, scale)
        for (i in 0 until pSprites.length()) {
            val spriteName = pSprites.getJSONObject(i).getString("fileName")
            val numberOfFrames = pSprites.getJSONObject(i).getInt("numberOfFrames")
            val Fps = pSprites.getJSONObject(i).getInt("Fps")
            newGameObject.addSprite(spriteName, numberOfFrames, Fps)
        }
        return newGameObject
    }

    fun updateCameras(){
        for (i in 0 until layers.size-1){
            layers[i].mCamera!!.moveLeft(xdir * speedX * layers[i].cameraSpeed)
        }
        layers.last().mCamera!!.mPosition = Vector2D(mPlayerObject.position.x + 80f, 0f)
    }

    fun updatePositions(){
        calcPlayerPosition()
        calcPistolPosition()
    }

    private fun calcPlayerPosition() {
        // Position handling
        mPlayerObject.position.x += xdir * speedX
        mPlayerObject.position.y += ydir * speedY

        if (falling) {
            if (speedY == 0f) {
                speedY = 0.5f
            }

            //max speed
            if (speedY < 5f) {
                speedY *= 1.2f
            } else {
                speedY = 5f
            }

            //checking ground
            if (mPlayerObject.position.y < ground) {
                falling = false
                speedY = 0f
                mPlayerObject.position.y = ground
                if (speedX > 0) {
                    idle = false
                }
            }
        } else if (jumping) {
            if (speedY <= 0f) {
                ydir = 1f
                speedY = 5f
            } else if (speedY >= 0.3f && ydir == 1f) {
                speedY *= 0.9f
            } else if (speedY < 0.3f) {
                //falling begins
                jumping = false
                falling = true
                ydir = -1f
            }
        }

        checkPlatforms()

        // Falling from the platform
        if(onPlatform != null){
            if((mPlayerObject.getBoundingBox().maxpoint.x <= onPlatform!!.minpoint.x || mPlayerObject.getBoundingBox().minpoint.x >= onPlatform!!.maxpoint.x) && !jumping){
                onPlatform = null
                falling = true
            }
        }
    }

    private fun checkPlatforms() {
        val eps = 5f
        //mPlayerObject.getBoundingBox().mEnabled = true
        for(plat in platforms){
            if (mPlayerObject.getBoundingBox().checkOverlapping(plat)) {
                if (falling && mPlayerObject.getBoundingBox().minpoint.y > plat.maxpoint.y - eps) {
                    onPlatform = plat
                    mPlayerObject.position.y = plat.maxpoint.y
                    falling = false
                    speedY = 0f
                    if (speedX > 0) {
                        idle = false
                    }
                }
            }
        }
    }

    private fun calcPistolPosition() {
        mPistolObject.position.y = mPlayerObject.getBoundingBox().minpoint.y + (mPlayerObject.getBoundingBox().maxpoint.y-mPlayerObject.getBoundingBox().minpoint.y)/3

        if(xdir == 1f){
            mPistolObject.position.x = mPlayerObject.getBoundingBox().maxpoint.x
        }
        else{
            mPistolObject.position.x = mPlayerObject.getBoundingBox().minpoint.x - (mPistolObject.getBoundingBox().maxpoint.x-mPistolObject.getBoundingBox().minpoint.x)
        }
    }

    fun updateAnimations(){
        // Player Animation
        if (falling) {
            mPlayerObject.currSprite = 3
        } else if (jumping) {
            mPlayerObject.currSprite = 2
        } else if (idle) {
            mPlayerObject.currSprite = 0
        } else if (!idle) {
            mPlayerObject.currSprite = 1
        }

        mPlayerObject.mSprites[mPlayerObject.currSprite].toFlip = xdir != 1f

        // Pistol Animation
        if (shooting) {
            mPistolObject.currSprite = 1
            if (mPistolObject.mSprites[1].miActualFrame == mPistolObject.mSprites[1].mvFrames.size-1) {
                shooting = false
            }
        } else{
            // TODO: Make a function that plays animation only once and a resetter!!
            mPistolObject.mSprites[1].miActualFrame = 0
            mPistolObject.currSprite = 0
        }
        mPistolObject.mSprites[mPistolObject.currSprite].toFlip = xdir != 1f
    }

    fun cleanup() {
        renderer.cleanup()
        mPlayerObject.cleanup()
        mPistolObject.cleanup()

    }
}