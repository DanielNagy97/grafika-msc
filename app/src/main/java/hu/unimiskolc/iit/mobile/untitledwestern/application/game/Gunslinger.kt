package hu.unimiskolc.iit.mobile.untitledwestern.application.game

import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.BoundingBox2D
import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.GameObject

open class Gunslinger (
    body: GameObject,
    var pistol: GameObject,
    velocity: Float,
    lives: Int
): Character(body, velocity, lives) {
    val bullets: ArrayList<Bullet> = ArrayList()

    private fun calcPistolPosition() {
        pistol.position.y = body.getBoundingBox().minpoint.y + (body.getBoundingBox().maxpoint.y-body.getBoundingBox().minpoint.y)/3

        if(movement.x.direction == 1){
            pistol.position.x = body.getBoundingBox().maxpoint.x
        }
        else{
            pistol.position.x = body.getBoundingBox().minpoint.x - (pistol.getBoundingBox().maxpoint.x-pistol.getBoundingBox().minpoint.x)
        }
    }

    fun updatePosition(ground: Float, dt: Float) {
        calcPosition(ground, dt)
        calcPistolPosition()
    }

    private fun updatePistolAnimation(){
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

    fun updateAnimations(){
        updateAnimation()
        updatePistolAnimation()
    }

    fun shootABullet(){
        for (i in 0 until bullets.size){
            if(!bullets[i].isFired){
                state.shooting = true
                bullets[i].isFired = true
                bullets[i].visible = true
                bullets[i].position.x = pistol.getBoundingBox().maxpoint.x - 19f
                bullets[i].position.y = pistol.getBoundingBox().maxpoint.y - 17f
                bullets[i].movement.direction = movement.x.direction
                bullets[i].mSprites[bullets[i].currSprite].toFlip = movement.x.direction != 1
                break
            }
        }
    }

    fun updateBullet(dt: Float, viewPort: BoundingBox2D, opponent: Character){
        for (bullet in bullets){
            bullet.updatePosition(dt, viewPort)
            if(!opponent.state.isInjured){
                bullet.checkOpponent(opponent)
            }
        }
    }
}