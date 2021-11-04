package hu.iit.me.untitledwestern.engine

import hu.iit.me.untitledwestern.engine.math.Vector2D

class SpriteFrame {
    var mFrame: Texture2D
    var name: String

    lateinit var mBBoxOriginal: BoundingBox2D
    lateinit var mBBoxTransformed: BoundingBox2D

    constructor(frameTexture: Texture2D, name: String){
        this.mFrame = frameTexture
        this.name = name
    }

    fun addBoundingBox(minpoint: Vector2D, maxpoint: Vector2D){
        mBBoxOriginal = BoundingBox2D(minpoint, maxpoint)
        mBBoxTransformed = BoundingBox2D(minpoint, maxpoint)
    }
}