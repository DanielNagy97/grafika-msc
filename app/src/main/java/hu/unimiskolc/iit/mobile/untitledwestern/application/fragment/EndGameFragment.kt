package hu.unimiskolc.iit.mobile.untitledwestern.application.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hu.unimiskolc.iit.mobile.untitledwestern.application.databinding.EndGameFragmentBinding
import org.koin.android.ext.android.inject
import hu.unimiskolc.iit.mobile.untitledwestern.application.R

class EndGameFragment: Fragment() {

    companion object {
        fun newInstance() = EndGameFragment()
    }

    private lateinit var binding : EndGameFragmentBinding
    private val viewModel: EndGameViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.end_game_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EndGameFragmentBinding.bind(view)
        binding?.vm = viewModel
        binding?.lifecycleOwner = this

        //viewModel = ViewModelProvider(this).get(ResultViewModel::class.java)
        val score = arguments?.getInt("score")
        viewModel.setScore(score ?: 0)
    }
}