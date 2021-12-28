package hu.unimiskolc.iit.mobile.untitledwestern.application.fragment

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import hu.unimiskolc.iit.mobile.untitledwestern.application.MyGLSurfaceView

class MainGameFragment: Fragment() {
    private lateinit var myGLSurfaceView: GLSurfaceView

    companion object {
        fun newInstance() = MainGameFragment()
    }

    private lateinit var viewModel: MainGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myGLSurfaceView = MyGLSurfaceView(requireContext())
        return myGLSurfaceView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= ViewModelProvider(this).get(MainGameViewModel::class.java)
    }
}