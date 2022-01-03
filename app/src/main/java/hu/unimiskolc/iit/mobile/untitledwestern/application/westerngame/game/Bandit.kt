package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game

import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.GameObject
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.states.MovementState

class Bandit (
    body: GameObject,
    pistol: GameObject,
    velocity: Float,
    lives: Int
): Gunslinger(body, pistol, velocity, lives) {

    private fun turnToPlayer(player: Player){
        if(body.position.x < player.body.position.x){
            movement.x.direction = 1
        }
        else{
            movement.x.direction = -1
        }
    }

    fun shootPlayer(player: Player){
        turnToPlayer(player)

        if(player.body.position.y == body.position.y){
            shootABullet()
        }
    }

    fun updateLives(viewPortMin: Float){
        if(lives <= 0){
            lives = 1
            body.position.x = viewPortMin -100f
            state.isInjured = false
            if(state.inHole){
                state.inHole = false
            }
        }
    }
}