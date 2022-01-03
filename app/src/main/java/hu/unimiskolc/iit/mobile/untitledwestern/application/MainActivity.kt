package hu.unimiskolc.iit.mobile.untitledwestern.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.unimiskolc.iit.mobile.untitledwestern.application.fragment.EndGameFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
            this.finish()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}

