package buildLight.timer

import java.awt.event.ActionListener

/**
 * Created by IntelliJ IDEA.
 * User: Thomas
 * Date: 19/07/11
 * Time: 00:58
 * To change this template use File | Settings | File Templates.
 */
class TimerFactory {

    public static javax.swing.Timer createTimer(int delay, boolean repeat, Closure closure) {
        createTimer(delay, delay, repeat, closure)
    }

    public static javax.swing.Timer createTimer(int delay, int initialDelay, boolean repeat, Closure closure) {
        javax.swing.Timer currentTimer = new javax.swing.Timer(delay, [
                actionPerformed: { e ->
                    closure()
                }
        ] as ActionListener)
        currentTimer.initialDelay = initialDelay
        currentTimer.repeats = repeat
        currentTimer.start()
        currentTimer
    }
}
