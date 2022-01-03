package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game

import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.BoundingBox2D
import hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine.GameObject

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

    fun updatePosition(ground: Float, viewPortHalfHeight: Float, dt: Float) {
        calcPosition(ground, viewPortHalfHeight, dt)
        calcPistolPosition()
    }

    private fun updatePistolAnimation(){
        if (state.shooting) {
            pistol.currSprite = 1
            if (pistol.mSprites[1].miActualFrame == pistol.mSprites[1].mvFrames.size-1) {
                state.shooting = false
            }
        } else{
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

    fun updateBullet(dt: Float, viewPort: BoundingBox2D, opponent: Character): Int{
        var score = 0
        for (bullet in bullets){
            bullet.updatePosition(dt, viewPort, movement.x.speed)
            if(!opponent.state.isInjured){
                if(bullet.checkOpponent(opponent)){
                    score += 100
                }
            }
        }
        return score
    }
}