package hu.unimiskolc.iit.mobile.untitledwestern.core.domain

import java.util.*

data class Game(val id: Int, val started: Date, var ended: Date?, var score: Int)
