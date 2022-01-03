package hu.unimiskolc.iit.mobile.untitledwestern.application.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.unimiskolc.iit.mobile.untitledwestern.application.databinding.EndGameFragmentBinding
import org.koin.android.ext.android.inject
import hu.unimiskolc.iit.mobile.untitledwestern.application.R
import hu.unimiskolc.iit.mobile.untitledwestern.application.fragment.adapter.HighScoreAdapter

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
        binding.vm = viewModel
        binding.lifecycleOwner = this

        binding.highScores.backToMenuButton.setOnClickListener {
            findNavController().navigate(R.id.startGameFragment)
        }

        val score = arguments?.getInt("score")
        viewModel.setScore(score ?: 0)

        val gameId = arguments?.getInt("gameId")

        if(gameId == null){
            view.findViewById<TextView>(R.id.result_text).visibility = View.INVISIBLE
        }

        val recyclerView: RecyclerView = binding.highScores.myListView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.hasFixedSize()
        val adapter = HighScoreAdapter(R.color.currentScore, gameId ?: -1)
        recyclerView.adapter = adapter

        viewModel.fetchHighScores()
        viewModel.getHighScores().observe(viewLifecycleOwner, {
                hiScores -> adapter.setScores(hiScores)
                if(gameId != null){
                    val gameListPosition = hiScores!!.mapIndexedNotNull { index, score ->  if (score.id == gameId) index else null}
                    if(gameListPosition.isNotEmpty()){
                        (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(gameListPosition[0], 0)
                    }
                }
        })
    }
}