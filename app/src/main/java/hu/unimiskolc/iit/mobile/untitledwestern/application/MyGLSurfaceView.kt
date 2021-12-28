package hu.unimiskolc.iit.mobile.untitledwestern.application

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import hu.unimiskolc.iit.mobile.untitledwestern.application.fragment.MainGameFragment

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

    override fun onTouchEvent(e: MotionEvent): Boolean {
        if(renderer.dummygame.mPlayer.lives<4){
            endGame()
        }
        touchHandler.handleInput(e, renderer.dummygame, width, height)
        return true
    }

    private fun endGame(){
        // Switching to manual render mode
        renderMode = 0
        findFragment<MainGameFragment>().findNavController().navigate(R.id.highScoreFragment)
    }
}