package hu.unimiskolc.iit.mobile.untitledwestern.application.fragment

import android.app.Activity
import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import hu.unimiskolc.iit.mobile.untitledwestern.application.R
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.DummyGame
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.Renderer
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.touchhandler.TouchHandler

class MyGLSurfaceView(context: Context, private val mainGameFragment: MainGameFragment) :
    GLSurfaceView(context) {
    private val renderer: hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.Renderer
    private val touchHandler: TouchHandler
    private val dummyGame: DummyGame

    init {
        // Create an OpenGL ES 3.0 context
        setEGLContextClientVersion(3)

        hideNavBar()

        val isBBEnabled = mainGameFragment.arguments?.getBoolean("boundingBoxCheck")
        dummyGame = DummyGame(context, 1f, "scenes/scene01.json", isBBEnabled!!)

        renderer = Renderer(context, this, dummyGame)

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)

        touchHandler = TouchHandler()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (dummyGame.sceneManager.mScenes.size != 0) {
            touchHandler.handleInput(e, dummyGame, width, height)
        }
        return true
    }

    fun endGame() {
        // Switching to manual render mode
        renderMode = 0
        renderer.cleanup()
        dummyGame.cleanup()

        (context as Activity).runOnUiThread() {
            val bundle = bundleOf(
                "score" to dummyGame.score,
                "gameId" to mainGameFragment.viewModel.getGame().id
            )

            mainGameFragment.viewModel.endGame(dummyGame.score)
            mainGameFragment.findNavController().navigate(R.id.endGameFragment, bundle)
        }
    }

    private fun hideNavBar() {
        systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE
                or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or SYSTEM_UI_FLAG_FULLSCREEN
                or SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}