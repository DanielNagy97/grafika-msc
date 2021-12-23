package hu.unimiskolc.iit.mobile.untitledwestern.application.game

import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.GameObject

open class Character (
    var body: GameObject,
    var pistol: GameObject,
    var velocity: Float,
    var lives: Int
    ){
    var idle = true
    var shooting = false
    var jumping = false
    var falling = true
    private var onPlatform : GameObject? = null
    var inHole = false

    var speedX = 0f
    private var speedY: Float = 0f
    var xdir = 1
    private var ydir = -1

    val bullets: ArrayList<Bullet> = ArrayList()

    private fun calcPosition(ground: Float, platforms: List<GameObject>, holes: List<GameObject>, barrels: List<GameObject>, gameCameraOffset: Float, dt:Float): Float {
        // Position handling
        body.position.x += xdir * (speedX * dt)
        body.position.y += ydir * (speedY * dt)

        if (falling) {
            if (speedY == 0f) {
                speedY = velocity / 3f
            }

            //max speed
            if (speedY < velocity * 4f) {
                speedY *= 1.2f * (1 + dt)
            } else {
                speedY = velocity * 4f
            }

            if(!inHole){
                //checking ground
                if (body.position.y < ground) {
                    falling = false
                    speedY = 0f
                    body.position.y = ground
                    if (speedX > 0) {
                        idle = false
                    }
                }
            }
            if(inHole){
                if (body.getBoundingBox().maxpoint.y < ground){
                    body.position.y = 282f
                    inHole = false
                }
            }

        } else if (jumping) {
            if (speedY <= 0f) {
                ydir = 1
                speedY = velocity * 4f
            } else if (speedY >= velocity/5f && ydir == 1) {
                speedY *= 0.9f * (1 - dt)
            } else if (speedY < velocity/5f) {
                //falling begins
                jumping = false
                falling = true
                ydir = -1
            }
        }

        checkHoles(holes)

        checkPlatforms(platforms)

        var newOffset = checkBarrels(barrels, gameCameraOffset, dt)

        // Falling from the platform
        if(onPlatform != null){
            if((body.getBoundingBox().maxpoint.x <= onPlatform!!.getBoundingBox().minpoint.x
                        || body.getBoundingBox().minpoint.x >= onPlatform!!.getBoundingBox().maxpoint.x) && !jumping){
                onPlatform = null
                falling = true
            }
        }

        return newOffset
    }

    private fun checkPlatforms(platforms: List<GameObject>) {
        val eps = 11f
        //mPlayerObject.getBoundingBox().mEnabled = true
        for(plat in platforms){
            if (body.getBoundingBox().checkOverlapping(plat.getBoundingBox())) {
                if (falling && body.getBoundingBox().minpoint.y > plat.getBoundingBox().maxpoint.y - eps) {
                    onPlatform = plat
                    body.position.y = plat.getBoundingBox().maxpoint.y
                    falling = false
                    speedY = 0f
                    if (speedX > 0) {
                        idle = false
                    }
                }
            }
        }
    }

    private fun checkHoles(holes: List<GameObject>){
        for (hole in holes) {
            if (!inHole && (body.getBoundingBox().minpoint.x > hole.getBoundingBox().minpoint.x && body.getBoundingBox().maxpoint.x < hole.getBoundingBox().maxpoint.x) && body.getBoundingBox().minpoint.y < hole.getBoundingBox().maxpoint.y){
                inHole = true
                falling = true
                lives--
            }
        }
    }

    private fun checkBarrels(barrels: List<GameObject>, gameCameraOffset: Float, dt: Float): Float{
        var newOffset = gameCameraOffset
        for (barrel in barrels){
            if (barrel.getBoundingBox().checkOverlapping(body.getBoundingBox())){
                if (falling && body.getBoundingBox().minpoint.y > barrel.getBoundingBox().maxpoint.y - 11f) {
                    onPlatform = barrel
                    body.position.y = barrel.getBoundingBox().maxpoint.y
                    falling = false
                    speedY = 0f
                    if (speedX > 0) {
                        idle = false
                    }
                }
                else if(onPlatform == null){
                    body.position.x = barrel.position.x-(body.getBoundingBox().maxpoint.x-body.getBoundingBox().minpoint.x)
                    newOffset += xdir * (speedX) * dt
                }

            }
        }
        return newOffset
    }

    private fun calcPistolPosition() {
        pistol.position.y = body.getBoundingBox().minpoint.y + (body.getBoundingBox().maxpoint.y-body.getBoundingBox().minpoint.y)/3

        if(xdir == 1){
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
        if (falling) {
            body.currSprite = 3
        } else if (jumping) {
            body.currSprite = 2
        } else if (idle) {
            body.currSprite = 0
        } else if (!idle) {
            body.currSprite = 1
        }

        body.mSprites[body.currSprite].toFlip = xdir != 1

        // Pistol Animation
        if (shooting) {
            pistol.currSprite = 1
            if (pistol.mSprites[1].miActualFrame == pistol.mSprites[1].mvFrames.size-1) {
                shooting = false
            }
        } else{
            // TODO: Make a function that plays animation only once and a resetter!!
            pistol.mSprites[1].miActualFrame = 0
            pistol.currSprite = 0
        }
        pistol.mSprites[pistol.currSprite].toFlip = xdir != 1
    }

    fun shootABullet(){
        for (i in 0 until bullets.size){
            if(!bullets[i].isFired){
                bullets[i].isFired = true
                bullets[i].visible = true
                bullets[i].position.x = pistol.getBoundingBox().maxpoint.x - 19f
                bullets[i].position.y = pistol.getBoundingBox().maxpoint.y - 17f
                bullets[i].xDir = xdir
                bullets[i].mSprites[bullets[i].currSprite].toFlip = xdir != 1
                break
            }
        }
    }
}