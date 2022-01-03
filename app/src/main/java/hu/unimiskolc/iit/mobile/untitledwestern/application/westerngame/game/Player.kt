package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game

import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.GameObject
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.states.MovementState
import kotlin.math.floor

class Player(
    body: GameObject,
    pistol: GameObject,
    velocity: Float,
    lives: Int
): Gunslinger(body, pistol, velocity, lives) {
    private var invincibleTime: Float = 0f

    fun updateCameraOffset(barrels: List<GameObject>, holes: List<GameObject>, originalGameCameraOffset: Float, gameCameraBaseOffset: Float, dt: Float) : Float{
        var gameCameraOffset = originalGameCameraOffset
        if(!state.isInjured){
            checkHoles(holes)
            gameCameraOffset = checkBarrels(barrels, gameCameraOffset, dt)
        }

        if(gameCameraOffset > gameCameraBaseOffset){
            gameCameraOffset -=  movement.x.speed * 0.5f * dt
        }
        return gameCameraOffset
    }

    fun checkIfPushedFromViewport(viewportMinX: Float, viewPortHalfHeight: Float){
        if(body.getBoundingBox().maxpoint.x < viewportMinX && !state.isInjured){
            movementState = MovementState.FALLING
            body.position.y = viewPortHalfHeight
            lives--
            state.isInjured = true
        }
    }

    fun checkCollectibles(collectibles: List<Collectible>): Int{
        var score = 0
        for(coin in collectibles){
            if(coin.visible){
                if (body.getBoundingBox().checkOverlapping(coin.getBoundingBox())) {
                    score += coin.value
                    coin.position.x -= 1000f
                }
            }
        }
        return score
    }

    fun updateInvincible(dt: Float){
        if(state.isInjured){
            invincibleTime += dt
            val isVisible = floor(invincibleTime*10) % 2 != 0f
            body.visible = isVisible
            pistol.visible = isVisible
            //2 seconds
            if(invincibleTime > 2){
                invincibleTime = 0f
                state.isInjured = false
                body.visible = true
                pistol.visible = true
            }
        }
    }
}