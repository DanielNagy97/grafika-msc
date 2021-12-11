package hu.iit.me.untitledwestern

import android.view.MotionEvent

class TouchHandler {
    fun handleInput(e:MotionEvent, dummyGame: DummyGame, width:Int, height:Int){
        val x: Float = e.x
        val y: Float = e.y

        if(e.action == MotionEvent.ACTION_DOWN ) {
            if(x < width / 2){
                dummyGame.mPlayer.speedX = dummyGame.mPlayer.velocity
                if(x < width / 4){
                    dummyGame.mPlayer.xdir = -1
                }
                else{
                    dummyGame.mPlayer.xdir = 1
                }
                if (!dummyGame.mPlayer.jumping && !dummyGame.mPlayer.falling){
                    dummyGame.mPlayer.idle = false
                }
            }
            else{
                if(y < height / 2 && !dummyGame.mPlayer.jumping && !dummyGame.mPlayer.falling){
                    dummyGame.mPlayer.jumping = true
                }
                if(y > height / 2){
                    dummyGame.mPlayer.shooting = true
                    dummyGame.mPlayer.shootABullet()
                }
            }
        }

        if(e.action == MotionEvent.ACTION_UP) {
            if(x < width / 2){
                dummyGame.mPlayer.speedX = 0f
            }
            dummyGame.mPlayer.idle = true
        }
    }
}