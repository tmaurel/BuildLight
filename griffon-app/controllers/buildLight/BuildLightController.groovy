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
        edt {
            view.startButon.action = actions.stopAction
        }
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
        disableFrom.seconds = 0

        def (untilHours, untilMinutes) = model.disableUntil.split(":")*.toInteger()
        def disableUntil = new Date()
        disableUntil.hours = untilHours
        disableUntil.minutes = untilMinutes
        disableUntil.seconds = 0

        if(disableUntil.before(disableFrom)) {
            disableUntil += 1
        }

        if(model.disableRange) {
            log.info("Light should be disabled during this time frame (From {}, until {})", [disableFrom, disableUntil].toArray())
        }

        def disabled = false

        if (ok) {
            currentTimer = TimerFactory.createTimer(ciServerController.frequencyInMilliseconds, 0, true, {
                execOutside {

                    def currentDate = new Date()

                    if(model.disableRange && currentDate.after(disableFrom) && currentDate.before(disableUntil)) {
                        if(!disabled) {
                            lightController.shutdownLights()
                        }
                        disabled = true
                        log.info("Light disabled during this time frame (From {}, until {})", [disableFrom, disableUntil].toArray())
                    }
                    else {
                        if(disabled) {
                            disableFrom += 1
                            disableUntil += 1
                            log.info("Light re-enabled. Next disabled time frame : (From {}, until {})", [disableFrom, disableUntil].toArray())
                            disabled = false
                        }
                    }

                    ciServerController.updateStatus(!disabled, model.currentStatus, {
                        ciServerController.serverNotFound()
                        stop()
                    })

                }
            })
        }
    }

    def stop() {
        edt {
            view.startButon.action = actions.startAction
        }
        def lightController = app.controllers.Light
        lightController.closeDevice()
        model.currentStatus = BuildStatus.UNKNOWN
        currentTimer?.stop()
    }

    def setCurrentStatus(status) {
        model.currentStatus = status
    }
}
