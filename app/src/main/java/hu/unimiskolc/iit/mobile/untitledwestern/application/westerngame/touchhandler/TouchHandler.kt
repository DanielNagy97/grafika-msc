package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.touchhandler

import android.view.MotionEvent
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.DummyGame
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.states.GameState
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.states.MovementState

class TouchHandler {
    fun handleInput(e:MotionEvent, dummyGame: DummyGame, width:Int, height:Int){
        val x: Float = e.x
        val y: Float = e.y

        if(e.action == MotionEvent.ACTION_DOWN ) {

            if(dummyGame.gameState == GameState.NOT_STARTED){
                dummyGame.gameState = GameState.STARTED
                dummyGame.mPlayer.movementState = MovementState.WALKING
                dummyGame.mPlayer.movement.x.speed = dummyGame.mPlayer.velocity
            }
            else if(dummyGame.gameState == GameState.STARTED){
                if(y < height / 2 && (dummyGame.mPlayer.movementState == MovementState.IDLE || dummyGame.mPlayer.movementState == MovementState.WALKING)){
                    dummyGame.mPlayer.movementState = MovementState.JUMPING
                }
                if(y > height / 2){
                    dummyGame.mPlayer.shootABullet()
                }
            }
        }
    }
}