package hu.iit.me.untitledwestern

import android.app.Activity
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.MotionEvent

class MainActivity : Activity() {
    private lateinit var gLView: GLSurfaceView
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        gLView = MyGLSurfaceView(this)
        setContentView(gLView)
    }

    override fun onPause() {
        super.onPause()
        gLView.onPause()
    }

    override fun onResume() {
        super.onResume()
        gLView.onResume()
    }


    class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {
        private val renderer: MyGLRenderer

        init {
            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2)

            renderer = MyGLRenderer(context)

            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(renderer)
        }

        // Touch event
        private val TOUCH_SCALE_FACTOR: Float = 180.0f / 32000f
        private var previousX: Float = 0f
        private var previousY: Float = 0f

        override fun onTouchEvent(e: MotionEvent): Boolean {
            // MotionEvent reports input details from the touch screen
            // and other input controls. In this case, you are only
            // interested in events where the touch position changed.

            val x: Float = e.x
            val y: Float = e.y

            if(e.action == MotionEvent.ACTION_MOVE ) {
                if (x < width / 2) {
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
/*
            when (e.actionMasked) {
                MotionEvent.ACTION_MOVE -> {
                    if (x < width / 2) {
                        var dx: Float = x - previousX
                        var dy: Float = y - previousY

                        renderer.mPlayerObject.position.x += dx * TOUCH_SCALE_FACTOR
                        renderer.mPlayerObject.position.y -= dy * TOUCH_SCALE_FACTOR
                        renderer.mPlayerObject.currSprite = 1
                    }
                }
                MotionEvent.ACTION_DOWN -> {
                    if (x < width / 2) {
                        renderer.mPistolObject.currSprite = 1
                    }
                }
            }

 */

            previousX = x
            previousY = y
            return true
        }

    }
}