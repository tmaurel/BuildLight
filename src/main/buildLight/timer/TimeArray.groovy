package buildLight.timer

import java.text.DateFormat
import java.text.SimpleDateFormat


class TimeArray {

    def static final list = new ArrayList<String>()

    static {
        (0..23).each {
             def hour = (it > 9) ? it.toString() : "0" + it.toString()
             ["00", "15", "30", "45"].each { minutes ->
                 list.add "$hour:$minutes".toString()
             }
        }
    }


    def static getArray() {
        list.toArray()
    }
}
