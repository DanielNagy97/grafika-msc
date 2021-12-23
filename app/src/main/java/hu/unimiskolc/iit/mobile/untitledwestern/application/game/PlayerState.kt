package hu.unimiskolc.iit.mobile.untitledwestern.application.game

data class PlayerState (
    var idle: Boolean = true,
    var shooting: Boolean = false,
    var jumping: Boolean = false,
    var falling: Boolean = true,
    var inHole: Boolean = false
)