package hu.unimiskolc.iit.mobile.untitledwestern.application.westerngame.engine

class Timer {
    private var lastLoopTime: Double = 0.0

    fun init() {
        lastLoopTime = getTime()
    }

    private fun getTime(): Double {
        return System.nanoTime() / 1000_000_000.0
    }

    fun getElapsedTime(): Float {
        val time: Double = getTime()
        val elapsedTime: Float = (time - lastLoopTime).toFloat()
        lastLoopTime = time
        return elapsedTime
    }
}