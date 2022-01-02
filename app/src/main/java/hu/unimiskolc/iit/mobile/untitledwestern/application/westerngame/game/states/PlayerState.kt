package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.game.states

data class PlayerState (
    var shooting: Boolean = false,
    var inHole: Boolean = false,
    var isInjured: Boolean = false
)