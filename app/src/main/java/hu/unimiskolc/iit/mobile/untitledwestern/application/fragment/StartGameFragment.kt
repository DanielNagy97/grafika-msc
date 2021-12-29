package hu.unimiskolc.iit.mobile.untitledwestern.application.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import hu.unimiskolc.iit.mobile.untitledwestern.application.R
import hu.unimiskolc.iit.mobile.untitledwestern.application.databinding.StartGameFragmentBinding

class StartGameFragment : Fragment(R.layout.start_game_fragment){

    private var binding: StartGameFragmentBinding? = null

    companion object {
        fun newInstance() = StartGameFragment()
    }

    private lateinit var viewModel: StartGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.start_game_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = StartGameFragmentBinding.bind(view)
        viewModel = ViewModelProvider(this).get(StartGameViewModel::class.java)

        binding?.startGameButton?.setOnClickListener {
            findNavController().navigate(R.id.mainGameFragment)
        }

        binding?.hiScoreButton?.setOnClickListener {
            findNavController().navigate(R.id.highScoreFragment)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}