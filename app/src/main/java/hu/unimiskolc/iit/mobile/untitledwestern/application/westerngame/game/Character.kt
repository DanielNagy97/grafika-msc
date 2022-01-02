package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game

import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.GameObject
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.movement.Movement
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.movement.Movement2D
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.states.MovementState
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.states.PlayerState

open class Character (
    var body: GameObject,
    var velocity: Float,
    var lives: Int
    ){
    var state: PlayerState = PlayerState()
    var movementState: MovementState = MovementState.FALLING
    var movement = Movement2D(Movement(0f, 1), Movement(0f, -1))

    private var onPlatform : GameObject? = null

    protected fun calcPosition(ground: Float, dt:Float){
        body.position.x += movement.x.direction * (movement.x.speed * dt)
        body.position.y += movement.y.direction * (movement.y.speed * dt)

        if (movementState == MovementState.FALLING) {
            when {
                movement.y.speed == 0f -> {
                    movement.y.speed = velocity / 3f
                }
                movement.y.speed < velocity * 4f -> {
                    movement.y.speed *= 1.2f * (1 + dt)
                }
                else -> {
                    movement.y.speed = velocity * 4f
                }
            }

            if(state.inHole){
                if (body.getBoundingBox().maxpoint.y < ground){
                    body.position.y = 282f
                    state.inHole = false
                }
            }
            else{
                if (body.position.y < ground) {
                    movement.y.speed = 0f
                    body.position.y = ground
                    checkIfWalking()
                }
            }
        }
        if (movementState == MovementState.JUMPING) {
            if (movement.y.speed <= 0f) {
                movement.y.direction = 1
                movement.y.speed = velocity * 4f
            } else if (movement.y.speed >= velocity/5f && movement.y.direction == 1) {
                movement.y.speed *= 0.9f * (1 - dt)
            } else if (movement.y.speed < velocity/5f) {
                movementState = MovementState.FALLING
                movement.y.direction = -1
            }
        }
    }

    private fun fallFromPlatform(){
        if(onPlatform != null){
            if((body.getBoundingBox().maxpoint.x <= onPlatform!!.getBoundingBox().minpoint.x
                        || body.getBoundingBox().minpoint.x >= onPlatform!!.getBoundingBox().maxpoint.x) && movementState != MovementState.JUMPING){
                onPlatform = null
                movementState = MovementState.FALLING
            }
        }
    }

    private fun landOnPlatform(platform: GameObject, eps: Float): Boolean{
        return if (movementState == MovementState.FALLING && body.getBoundingBox().minpoint.y > platform.getBoundingBox().maxpoint.y - eps) {
            onPlatform = platform
            body.position.y = platform.getBoundingBox().maxpoint.y
            movement.y.speed = 0f
            checkIfWalking()
            true
        } else{
            false
        }
    }

    private fun checkIfWalking(){
        movementState = if (movement.x.speed > 0) {
            MovementState.WALKING
        } else{
            MovementState.IDLE
        }
    }

    fun checkPlatforms(platforms: List<GameObject>) {
        val eps = 11f
        for(plat in platforms){
            //if(plat.visible){
                if (body.getBoundingBox().checkOverlapping(plat.getBoundingBox())) {
                    landOnPlatform(plat, eps)
                }
            //}
        }
        fallFromPlatform()
    }

    fun checkHoles(holes: List<GameObject>){
        for (hole in holes) {
            if(hole.visible){
                if (!state.inHole && (body.getBoundingBox().minpoint.x > hole.getBoundingBox().minpoint.x && body.getBoundingBox().maxpoint.x < hole.getBoundingBox().maxpoint.x) && body.getBoundingBox().minpoint.y < hole.getBoundingBox().maxpoint.y){
                    state.inHole = true
                    movementState = MovementState.FALLING
                }
                else if(state.inHole && body.getBoundingBox().maxpoint.y < hole.getBoundingBox().maxpoint.y){
                    lives--
                    state.isInjured = true
                }
            }
        }
    }

    fun checkBarrels(barrels: List<GameObject>, gameCameraOffset: Float, dt: Float): Float{
        var newOffset = gameCameraOffset
        for (barrel in barrels){
            if(barrel.visible){
                if (barrel.getBoundingBox().checkOverlapping(body.getBoundingBox())){
                    if(!landOnPlatform(barrel, 11f) && onPlatform == null){
                        body.position.x = barrel.position.x-(body.getBoundingBox().maxpoint.x-body.getBoundingBox().minpoint.x)
                        newOffset += movement.x.direction * (movement.x.speed) * dt
                    }
                }
            }
        }
        return newOffset
    }

    protected fun updateAnimation(){
        when (movementState) {
            MovementState.FALLING -> {
                body.currSprite = 3
            }
            MovementState.JUMPING -> {
                body.currSprite = 2
            }
            MovementState.WALKING -> {
                body.currSprite = 1
            }
            MovementState.IDLE -> {
                body.currSprite = 0
            }
        }
        body.mSprites[body.currSprite].toFlip = movement.x.direction != 1
    }
}