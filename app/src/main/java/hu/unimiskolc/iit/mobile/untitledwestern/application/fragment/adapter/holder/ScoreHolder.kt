package hu.unimiskolc.iit.mobile.untitledwestern.application.fragment.adapter.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.unimiskolc.iit.mobile.untitledwestern.application.R

class ScoreHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val id: TextView = itemView.findViewById(R.id.score_id)
    val duration: TextView = itemView.findViewById(R.id.score_duration)
    val score: TextView = itemView.findViewById(R.id.score_value)
    val date: TextView = itemView.findViewById(R.id.score_date)
}