package hu.unimiskolc.iit.mobile.untitledwestern.application

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {
    private val renderer: MyGLRenderer
    private val touchHandler: TouchHandler

    init {
        // Create an OpenGL ES 3.0 context
        setEGLContextClientVersion(3)

        renderer = MyGLRenderer(context)

        // Hiding the navigationbar
        systemUiVisibility = (SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)

        touchHandler = TouchHandler()
    }

    //@SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent): Boolean {
        //systemUiVisibility = (SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        touchHandler.handleInput(e, renderer.dummygame, width, height)
        return true
    }
}