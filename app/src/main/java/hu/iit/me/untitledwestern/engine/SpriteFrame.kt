package hu.iit.me.untitledwestern.engine

class SpriteFrame {
    var mFrame: Texture2D
    var name: String

    constructor(frameTexture: Texture2D, name: String){
        this.mFrame = frameTexture
        this.name = name
    }
}