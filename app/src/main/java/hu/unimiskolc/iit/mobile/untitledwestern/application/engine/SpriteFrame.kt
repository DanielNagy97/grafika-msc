package hu.unimiskolc.iit.mobile.untitledwestern.application.engine

import hu.unimiskolc.iit.mobile.untitledwestern.application.engine.math.Vector2D

class SpriteFrame(var mFrame: Texture2D, private var name: String) {
    lateinit var mBBoxOriginal: BoundingBox2D
    lateinit var mBBoxTransformed: BoundingBox2D

    fun addBoundingBox(minpoint: Vector2D, maxpoint: Vector2D){
        mBBoxOriginal = BoundingBox2D(minpoint, maxpoint)
        mBBoxTransformed = BoundingBox2D(minpoint, maxpoint)
    }
}