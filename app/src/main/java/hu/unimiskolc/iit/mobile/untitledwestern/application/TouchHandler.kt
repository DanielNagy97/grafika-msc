package hu.unimiskolc.iit.mobile.untitledwestern.application

import android.view.MotionEvent
import hu.unimiskolc.iit.mobile.untitledwestern.application.game.states.MovementState

class TouchHandler {
    fun handleInput(e:MotionEvent, dummyGame: DummyGame, width:Int, height:Int){
        val x: Float = e.x
        val y: Float = e.y

        if(e.action == MotionEvent.ACTION_DOWN ) {
            if(x < width / 2){
                dummyGame.mPlayer.movement.x.speed = dummyGame.mPlayer.velocity
                if(x < width / 4){
                    dummyGame.mPlayer.movement.x.direction = -1
                }
                else{
                    dummyGame.mPlayer.movement.x.direction = 1
                }
                if (dummyGame.mPlayer.movementState == MovementState.IDLE){
                    dummyGame.mPlayer.movementState = MovementState.WALKING
                }
            }
            else{
                if(y < height / 2 && (dummyGame.mPlayer.movementState == MovementState.IDLE || dummyGame.mPlayer.movementState == MovementState.WALKING)){
                    dummyGame.mPlayer.movementState = MovementState.JUMPING
                }
                if(y > height / 2){
                    dummyGame.mPlayer.shootABullet()
                }
            }
        }

        if(e.action == MotionEvent.ACTION_UP) {
            if(x < width / 2){
                dummyGame.mPlayer.movement.x.speed = 0f
            }
            if(dummyGame.mPlayer.movementState == MovementState.WALKING && !dummyGame.mPlayer.state.shooting)
            dummyGame.mPlayer.movementState = MovementState.IDLE
        }
    }
}