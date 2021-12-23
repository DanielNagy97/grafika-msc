package hu.unimiskolc.iit.mobile.untitledwestern.application.game

import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.GameObject

class Player(
    body: GameObject,
    pistol: GameObject,
    velocity: Float,
    lives: Int
): Character(body, pistol, velocity, lives) {

    fun checkCollectibles(collectibles: List<Collectible>): Int{
        var score = 0
        for(coin in collectibles){
            if (body.getBoundingBox().checkOverlapping(coin.getBoundingBox())) {
                score += coin.value
                coin.position.x += 1000f
            }
        }
        return score
    }


}