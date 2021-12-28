package hu.unimiskolc.iit.mobile.untitledwestern.core.domain

import java.util.*

data class Game(val id: Int, val started: Date, val ended: Date?, val score: Int)
