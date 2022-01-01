package hu.unimiskolc.iit.mobile.untitledwestern.application

import android.app.Activity
import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import androidx.core.os.bundleOf
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import hu.unimiskolc.iit.mobile.untitledwestern.application.fragment.MainGameFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyGLSurfaceView(context: Context, private val mainGameFragment: MainGameFragment) : GLSurfaceView(context) {
    private val renderer: MyGLRenderer
    private val touchHandler: TouchHandler

    init {
        // Create an OpenGL ES 3.0 context
        setEGLContextClientVersion(3)

        val isBBEnabled = mainGameFragment.arguments?.getBoolean("boundingBoxCheck")

        renderer = MyGLRenderer(context, this, isBBEnabled!!)

        // Hiding the navigationbar
        systemUiVisibility = (SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)

        touchHandler = TouchHandler()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        if(!renderer.dummygame.gameEnded && renderer.dummygame.mPlayer != null){
            touchHandler.handleInput(e, renderer.dummygame, width, height)
        }
        return true
    }

    fun endGame(){
        // Switching to manual render mode
        renderMode = 0

        (context as Activity).runOnUiThread() {
            val bundle = bundleOf("score" to renderer.dummygame.score, "gameId" to mainGameFragment.viewModel.getGame().id)

            mainGameFragment.viewModel.endGame(renderer.dummygame.score)
            mainGameFragment.findNavController().navigate(R.id.endGameFragment, bundle)
        }
    }
}