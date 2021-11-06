package hu.iit.me.untitledwestern

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.MotionEvent

class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {
    private val renderer: MyGLRenderer

    init {
        // Create an OpenGL ES 3.0 context
        setEGLContextClientVersion(3)

        renderer = MyGLRenderer(context)

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
    }

    // Touch event
    private val TOUCH_SCALE_FACTOR: Float = 180.0f / 1200f
    private var previousX: Float = 0f
    private var previousY: Float = 0f

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val x: Float = e.x
        val y: Float = e.y

        if(e.action == MotionEvent.ACTION_DOWN ) {
            if(x < width / 2){
                renderer.velocityX = 1.5f
                if(x < width / 4){
                    renderer.xdir = -1f
                }
                else{
                    renderer.xdir = 1f
                }
                if (!renderer.jumping && !renderer.falling){
                    renderer.walking = true
                }
            }
            else{
                if(y > height / 2 && !renderer.shooting){
                    renderer.shooting = true
                }
                else{
                    if(!renderer.jumping && !renderer.falling){
                        renderer.jumping = true
                        renderer.velocityY = 10f
                        renderer.ydir = 1f
                    }
                }
            }
        }

        if(e.action == MotionEvent.ACTION_UP) {
            if(x < width / 2){
                renderer.velocityX = 0f

            }
            renderer.walking = false
        }

        return true
    }


    /*
    override fun onTouchEvent(e: MotionEvent): Boolean {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        val x: Float = e.x
        val y: Float = e.y

        if(e.action == MotionEvent.ACTION_MOVE ) {
            if (x < (width / 2)) {
                var dx: Float = x - previousX
                var dy: Float = y - previousY

                renderer.mPlayerObject.position.x += dx * TOUCH_SCALE_FACTOR
                renderer.mPlayerObject.position.y -= dy * TOUCH_SCALE_FACTOR
                renderer.mPlayerObject.currSprite = 1
            }
        }

        if(e.actionMasked == MotionEvent.ACTION_POINTER_DOWN && e.pointerCount == 2){
            if (x < width / 2) {
                renderer.mPistolObject.currSprite = 1
            }
        }
        else if(e.action == MotionEvent.ACTION_DOWN) {
            if (x > width / 2) {
                renderer.mPistolObject.currSprite = 1
            }
        }

        previousX = x
        previousY = y
        return true
    }
     */

}