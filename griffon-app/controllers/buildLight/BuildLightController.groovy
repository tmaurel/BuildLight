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

        def format = TimeArray.getFormat()

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

        if (ok) {
            currentTimer = TimerFactory.createTimer(ciServerController.frequencyInMilliseconds, 0, true, {
                execOutside {

                    def currentDate = new Date()
                    def disableFrom = format.parse(model.disableFrom)
                    def disableUntil = format.parse(model.disableUntil)
                    if(disableUntil.before(disableFrom)) {
                        disableUntil = disableUntil + 1
                    }

                    if(model.disableRange && currentDate.after(disableFrom) && currentDate.before(disableUntil)) {
                        log.info("Light disabled during this time range (From {}, until {}", [disableFrom, disableUntil].toArray())
                    }
                    else {
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
