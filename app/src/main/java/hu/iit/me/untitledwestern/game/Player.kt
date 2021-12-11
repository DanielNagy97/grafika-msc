package hu.iit.me.untitledwestern.game

import hu.iit.me.untitledwestern.engine.GameObject
import hu.iit.me.untitledwestern.game.Character
import hu.iit.me.untitledwestern.game.Coin

class Player(
    body: GameObject,
    pistol: GameObject,
    velocity: Float
): Character(body, pistol, velocity) {

    fun checkCoins(coins: List<Coin>){
        for(coin in coins){
            if (body.getBoundingBox().checkOverlapping(coin.getBoundingBox())) {
                coin.position.x += 1000f
            }
        }
    }
}