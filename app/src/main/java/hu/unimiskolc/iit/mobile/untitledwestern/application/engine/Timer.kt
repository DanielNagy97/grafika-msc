package hu.unimiskolc.iit.mobile.untitledwestern.application.engine

class Timer {
    private var lastLoopTime: Double = 0.0

    fun init() {
        lastLoopTime = getTime()
    }

    fun getTime(): Double {
        return System.nanoTime() / 1000_000_000.0
    }

    fun getElapsedTime(): Float {
        var time: Double = getTime()
        var elapsedTime: Float = (time - lastLoopTime).toFloat()
        lastLoopTime = time
        return elapsedTime
    }
}