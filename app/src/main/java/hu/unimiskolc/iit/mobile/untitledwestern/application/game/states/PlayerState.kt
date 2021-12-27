package hu.unimiskolc.iit.mobile.untitledwestern.application.game.states

data class PlayerState (
    var shooting: Boolean = false,
    var inHole: Boolean = false,
    var isInjured: Boolean = false
)