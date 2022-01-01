package hu.unimiskolc.iit.mobile.untitledwestern.application.fragment

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.lifecycle.LiveData
import hu.unimiskolc.iit.mobile.untitledwestern.application.R
import hu.unimiskolc.iit.mobile.untitledwestern.core.domain.Game
import java.util.*

class ScoreAdapter(context: Context, val resource: Int, objects: List<Game>) : ArrayAdapter<Game>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val id = getItem(position)?.id
        val ended = getItem(position)?.ended
        val score = getItem(position)?.score
        val started = getItem(position)?.started

        //val newGame = Game(id!!, started!!, ended!!, score!!)

        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var newView: View =  inflater.inflate(resource, parent, false)

        val idView = newView.findViewById<TextView>(R.id.score_id)
        val endedView = newView.findViewById<TextView>(R.id.score_duration)
        val scoreView = newView.findViewById<TextView>(R.id.score_value)
        val startedView = newView.findViewById<TextView>(R.id.score_date)

        idView.text = (position+1).toString()
        endedView.text = calcElapsedTime(started!!, ended!!)
        scoreView.text = score.toString()
        startedView.text = formatDate(started!!)

        return newView
    }

    private fun formatDate(date: Date) : String {
        val pattern = "yyyy-MM-dd"
        return SimpleDateFormat(pattern).format(date)
    }

    private fun calcElapsedTime(started: Date, ended: Date) : String {
        val milliSeconds = ended.time - started.time
        val minutes = (milliSeconds / 1000 ) / 60
        val seconds = (milliSeconds / 1000 ) % 60
        return "$minutes:$seconds"
    }

}