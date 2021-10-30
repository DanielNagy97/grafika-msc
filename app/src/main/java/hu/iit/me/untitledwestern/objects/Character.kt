package hu.iit.me.untitledwestern.objects

import hu.iit.me.untitledwestern.MyGLRenderer
import hu.iit.me.untitledwestern.engine.GameObject
import hu.iit.me.untitledwestern.engine.math.Vector2D

class Character {
    lateinit var body : GameObject
    lateinit var gun: GameObject
    lateinit var gunOffset: Vector2D

    constructor(body: GameObject, gun: GameObject) {
        this.body = body
        this.gun = gun
        this.gunOffset = Vector2D(0f,0f)
    }

    fun draw(renderer: MyGLRenderer) {
        body.draw(renderer)
        gun.draw(renderer)
    }
}