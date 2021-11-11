package hu.iit.me.untitledwestern

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

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)

        touchHandler = TouchHandler()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        touchHandler.handleInput(e, renderer.dummygame, width, height)
        return true
    }
}