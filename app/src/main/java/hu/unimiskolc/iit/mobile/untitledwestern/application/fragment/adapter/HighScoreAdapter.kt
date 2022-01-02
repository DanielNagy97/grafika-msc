package hu.unimiskolc.iit.mobile.untitledwestern.application.fragment.adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.unimiskolc.iit.mobile.untitledwestern.application.R
import hu.unimiskolc.iit.mobile.untitledwestern.application.fragment.adapter.holder.ScoreHolder
import hu.unimiskolc.iit.mobile.untitledwestern.core.domain.Game
import java.util.*
import kotlin.collections.ArrayList

class HighScoreAdapter(
    private val currentScoreColor: Int,
    private val currentGameId : Int = -1
): RecyclerView.Adapter<ScoreHolder>() {

    private var scores : List<Game> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_view_layout, parent,false)
        return ScoreHolder(itemView)
    }

    override fun onBindViewHolder(holder: ScoreHolder, position: Int) {
        val currentGame: Game = scores[position]
        holder.id.text = (position+1).toString()
        holder.duration.text = calcElapsedTime(currentGame.started, currentGame.ended!!)
        holder.score.text = currentGame.score.toString()
        holder.date.text = formatDate(currentGame.started)

        if(currentGame.id == currentGameId){
            holder.itemView.setBackgroundColor(currentScoreColor)
        }
    }

    override fun getItemCount(): Int {
        return scores.size
    }

    fun setScores(scores: List<Game>){
        this.scores = scores
        notifyDataSetChanged()
    }

    private fun formatDate(date: Date) : String {
        val pattern = "yyyy-MM-dd HH:mm:ss"
        return SimpleDateFormat(pattern).format(date)
    }

    private fun calcElapsedTime(started: Date, ended: Date) : String {
        val milliSeconds = ended.time - started.time
        val minutes = (milliSeconds / 1000 ) / 60
        val seconds = (milliSeconds / 1000 ) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}