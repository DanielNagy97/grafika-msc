package hu.iit.me.untitledwestern

import android.view.MotionEvent

class TouchHandler {
    fun handleInput(e:MotionEvent, dummyGame: DummyGame, width:Int, height:Int){
        val x: Float = e.x
        val y: Float = e.y

        if(e.action == MotionEvent.ACTION_DOWN ) {
            if(x < width / 2){
                dummyGame.speedX = dummyGame.velocity
                if(x < width / 4){
                    dummyGame.xdir = -1f
                }
                else{
                    dummyGame.xdir = 1f
                }
                if (!dummyGame.jumping && !dummyGame.falling){
                    dummyGame.idle = false
                }
            }
            else{
                if(y < height / 2 && !dummyGame.jumping && !dummyGame.falling){
                    dummyGame.jumping = true
                }
                if(y > height / 2){
                    dummyGame.shooting = true
                }
            }
        }

        if(e.action == MotionEvent.ACTION_UP) {
            if(x < width / 2){
                dummyGame.speedX = 0f
            }
            dummyGame.idle = true
        }
    }
}