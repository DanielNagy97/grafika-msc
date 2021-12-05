package hu.iit.me.untitledwestern.game

import hu.iit.me.untitledwestern.MyGLRenderer
import hu.iit.me.untitledwestern.engine.BoundingBox2D
import hu.iit.me.untitledwestern.engine.GameObject
import hu.iit.me.untitledwestern.engine.math.Vector2D

class Character (
    var body: GameObject,
    var pistol: GameObject,
    var velocity: Float
    ){
    var idle = true
    var shooting = false
    var jumping = false
    var falling = true
    private var onPlatform : GameObject? = null

    var speedX = 0f
    private var speedY: Float = 0f
    var xdir = 1f
    private var ydir = -1f

    private fun calcPlayerPosition(ground: Float, platforms: List<GameObject>, dt:Float) {
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

            //checking ground
            if (body.position.y < ground) {
                falling = false
                speedY = 0f
                body.position.y = ground
                if (speedX > 0) {
                    idle = false
                }
            }
        } else if (jumping) {
            if (speedY <= 0f) {
                ydir = 1f
                speedY = velocity * 4f
            } else if (speedY >= velocity/5f && ydir == 1f) {
                speedY *= 0.9f * (1 - dt)
            } else if (speedY < velocity/5f) {
                //falling begins
                jumping = false
                falling = true
                ydir = -1f
            }
        }

        checkPlatforms(platforms)

        // Falling from the platform
        if(onPlatform != null){
            if((body.getBoundingBox().maxpoint.x <= onPlatform!!.getBoundingBox().minpoint.x
                        || body.getBoundingBox().minpoint.x >= onPlatform!!.getBoundingBox().maxpoint.x) && !jumping){
                onPlatform = null
                falling = true
            }
        }
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

    private fun calcPistolPosition() {
        pistol.position.y = body.getBoundingBox().minpoint.y + (body.getBoundingBox().maxpoint.y-body.getBoundingBox().minpoint.y)/3

        if(xdir == 1f){
            pistol.position.x = body.getBoundingBox().maxpoint.x
        }
        else{
            pistol.position.x = body.getBoundingBox().minpoint.x - (pistol.getBoundingBox().maxpoint.x-pistol.getBoundingBox().minpoint.x)
        }
    }

    fun checkCoins(coins: List<GameObject>){
        for(coin in coins){
            if (body.getBoundingBox().checkOverlapping(coin.getBoundingBox())) {
                coin.position.x += 1000f
            }
        }
    }

    fun updatePosition(ground: Float, platforms: List<GameObject>, coins: List<GameObject>, dt: Float) {
        calcPlayerPosition(ground, platforms, dt)
        calcPistolPosition()
        checkCoins(coins)
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

        body.mSprites[body.currSprite].toFlip = xdir != 1f

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
        pistol.mSprites[pistol.currSprite].toFlip = xdir != 1f
    }
}