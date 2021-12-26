package hu.unimiskolc.iit.mobile.untitledwestern.application.game

import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.GameObject
import hu.unimiskolc.iit.mobile.untitledwestern.application.game.movement.Movement
import hu.unimiskolc.iit.mobile.untitledwestern.application.game.movement.Movement2D
import hu.unimiskolc.iit.mobile.untitledwestern.application.game.states.MovementState
import hu.unimiskolc.iit.mobile.untitledwestern.application.game.states.PlayerState

open class Character (
    var body: GameObject,
    var pistol: GameObject,
    var velocity: Float,
    var lives: Int
    ){
    var state: PlayerState = PlayerState()
    var movementState: MovementState = MovementState.FALLING
    var movement = Movement2D(Movement(0f, 1), Movement(0f, -1))

    private var onPlatform : GameObject? = null
    val bullets: ArrayList<Bullet> = ArrayList()

    private fun calcPosition(ground: Float, platforms: List<GameObject>, holes: List<GameObject>, barrels: List<GameObject>, gameCameraOffset: Float, dt:Float): Float {
        // Position handling
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

        checkHoles(holes)

        checkPlatforms(platforms)

        var newOffset = checkBarrels(barrels, gameCameraOffset, dt)

        fallFromPlatform()

        return newOffset
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

    private fun checkPlatforms(platforms: List<GameObject>) {
        val eps = 11f
        //mPlayerObject.getBoundingBox().mEnabled = true
        for(plat in platforms){
            if (body.getBoundingBox().checkOverlapping(plat.getBoundingBox())) {
                landOnPlatform(plat, eps)
            }
        }
    }

    private fun checkHoles(holes: List<GameObject>){
        for (hole in holes) {
            if (!state.inHole && (body.getBoundingBox().minpoint.x > hole.getBoundingBox().minpoint.x && body.getBoundingBox().maxpoint.x < hole.getBoundingBox().maxpoint.x) && body.getBoundingBox().minpoint.y < hole.getBoundingBox().maxpoint.y){
                state.inHole = true
                movementState = MovementState.FALLING
                lives--
            }
        }
    }

    private fun checkBarrels(barrels: List<GameObject>, gameCameraOffset: Float, dt: Float): Float{
        var newOffset = gameCameraOffset
        for (barrel in barrels){
            if (barrel.getBoundingBox().checkOverlapping(body.getBoundingBox())){
                if(!landOnPlatform(barrel, 11f) && onPlatform == null){
                    body.position.x = barrel.position.x-(body.getBoundingBox().maxpoint.x-body.getBoundingBox().minpoint.x)
                    newOffset += movement.x.direction * (movement.x.speed) * dt
                }
            }
        }
        return newOffset
    }

    private fun calcPistolPosition() {
        pistol.position.y = body.getBoundingBox().minpoint.y + (body.getBoundingBox().maxpoint.y-body.getBoundingBox().minpoint.y)/3

        if(movement.x.direction == 1){
            pistol.position.x = body.getBoundingBox().maxpoint.x
        }
        else{
            pistol.position.x = body.getBoundingBox().minpoint.x - (pistol.getBoundingBox().maxpoint.x-pistol.getBoundingBox().minpoint.x)
        }
    }

    fun updatePosition(ground: Float, platforms: List<GameObject>, holes: List<GameObject>, barrels: List<GameObject>, gameCameraOffset: Float, dt: Float): Float {
        val newOffset: Float = calcPosition(ground, platforms, holes, barrels, gameCameraOffset, dt)
        calcPistolPosition()
        return newOffset
    }

    fun updateAnimations(){
        // Player Animation
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

        // Pistol Animation
        if (state.shooting) {
            pistol.currSprite = 1
            if (pistol.mSprites[1].miActualFrame == pistol.mSprites[1].mvFrames.size-1) {
                state.shooting = false
            }
        } else{
            // TODO: Make a function that plays animation only once and a resetter!!
            pistol.mSprites[1].miActualFrame = 0
            pistol.currSprite = 0
        }
        pistol.mSprites[pistol.currSprite].toFlip = movement.x.direction != 1
    }

    fun shootABullet(){
        for (i in 0 until bullets.size){
            if(!bullets[i].isFired){
                bullets[i].isFired = true
                bullets[i].visible = true
                bullets[i].position.x = pistol.getBoundingBox().maxpoint.x - 19f
                bullets[i].position.y = pistol.getBoundingBox().maxpoint.y - 17f
                bullets[i].xDir = movement.x.direction
                bullets[i].mSprites[bullets[i].currSprite].toFlip = movement.x.direction != 1
                break
            }
        }
    }
}