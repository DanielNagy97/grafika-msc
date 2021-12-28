package hu.unimiskolc.iit.mobile.untitledwestern.application.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import hu.unimiskolc.iit.mobile.untitledwestern.application.R
import hu.unimiskolc.iit.mobile.untitledwestern.application.databinding.HighScoreFragmentBinding
import hu.unimiskolc.iit.mobile.untitledwestern.application.databinding.StartGameFragmentBinding

class HighScoreFragment: Fragment(R.layout.high_score_fragment) {

    private var binding: HighScoreFragmentBinding? = null

    companion object {
        fun newInstance() = HighScoreFragment()
    }

    private lateinit var viewModel: HighScoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.high_score_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HighScoreFragmentBinding.bind(view)
        viewModel = ViewModelProvider(this).get(HighScoreViewModel::class.java)

        binding?.backToMenuButton?.setOnClickListener {
            findNavController().navigate(R.id.startGameFragment)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}