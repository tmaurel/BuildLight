package buildLight

import buildLight.timer.TimerFactory
import buildLight.constants.BuildStatus
import buildLight.timer.TimeArray

class BuildLightController {
    // these will be injected by Griffon
    def model
    def view
    def actions

    def settingsService

    def currentTimer

    void mvcGroupDestroy() {
        destroyMVCGroup('CIServer')
        destroyMVCGroup('Light')
        settingsService.writeSettings()
    }

    /*
    def action = { evt = null ->
    }
    */

    def start() {

        boolean ok = true

        view.tabs.selectedIndex = 0
        view.tabs.setEnabledAt(1, false)
        view.tabs.setEnabledAt(2, false)
        view.startButon.action = actions.stopAction

        def lightController = app.controllers.Light
        def ciServerController = app.controllers.CIServer

        ciServerController.initServer( null, {
           ciServerController.serverNotFound()
           ok = false
           doLater {
               stop()
           }
        })

        lightController.initDevice( null, {
            lightController.deviceNotFound()
            ok = false
            doLater {
                stop()
            }
        })

        def (fromHours, fromMinutes) = model.disableFrom.split(":")*.toInteger()
        def disableFrom = new Date()
        disableFrom.hours = fromHours
        disableFrom.minutes = fromMinutes

        def (untilHours, untilMinutes) = model.disableUntil.split(":")*.toInteger()
        def disableUntil = new Date()
        disableUntil.hours = untilHours
        disableUntil.minutes = untilMinutes

        if(disableUntil.before(disableFrom)) {
            disableUntil += 1
        }

        def disabled = false

        if (ok) {
            currentTimer = TimerFactory.createTimer(ciServerController.frequencyInMilliseconds, 0, true, {
                execOutside {

                    def currentDate = new Date()

                    if(model.disableRange && currentDate.after(disableFrom) && currentDate.before(disableUntil)) {
                        disabled = true
                        log.info("Light disabled during this time range (From {}, until {})", [disableFrom, disableUntil].toArray())
                    }
                    else {
                        if(disabled) {
                            disableFrom += 1
                            disableUntil += 1
                            disabled = false
                        }

                        ciServerController.updateLight(model.currentStatus, {
                            ciServerController.serverNotFound()
                            stop()
                        })
                    }
                }
            })
        }
    }

    def stop() {
        def lightController = app.controllers.Light
        edt {
            view.tabs.setEnabledAt(1, true)
            view.tabs.setEnabledAt(2, true)
            view.startButon.action = actions.startAction
        }
        lightController.closeDevice()
        model.currentStatus = BuildStatus.UNKNOWN
        currentTimer?.stop()
    }

    def setCurrentStatus(status) {
        model.currentStatus = status
    }
}
