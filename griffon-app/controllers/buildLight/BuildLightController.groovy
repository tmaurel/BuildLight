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
            toggleConf(false)
            view.startButon.action = actions.stopAction
        }
        def lightController = app.controllers.Light
        def ciServerController = app.controllers.CIServer

        ciServerController.initServer( null, {
           stop()
           ciServerController.serverNotFound()
           ok = false
        })

        lightController.initDevice( null, {
            stop()
            lightController.deviceNotFound()
            ok = false
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
                        stop()
                        ciServerController.serverNotFound()
                    })

                }
            })
        }
    }

    def toggleConf(enabled) {
        view.tabs.setEnabledAt(1, enabled)
        view.tabs.setEnabledAt(2, enabled)
        view.disableRange.enabled = enabled
        view.disableFrom.enabled = enabled
        view.disableUntil.enabled = enabled
    }


    def stop() {
        edt {
            toggleConf(true)
            view.startButon.action = actions.startAction
        }
        execOutside {
            def lightController = app.controllers.Light
            lightController.closeDevice()
            model.currentStatus = BuildStatus.UNKNOWN
            currentTimer?.stop()
        }
    }

    def setCurrentStatus(status) {
        model.currentStatus = status
    }
}
