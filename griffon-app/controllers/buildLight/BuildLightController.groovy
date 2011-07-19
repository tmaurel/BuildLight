package buildLight

import buildLight.server.ICIServer.CIServerNotFound
import buildLight.timer.TimerFactory
import buildLight.constants.BuildStatus

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
           doLater {
               ok = false
               stop()
           }
        })

        lightController.initDevice( null, {
            lightController.deviceNotFound()
            doLater {
                ok = false
                stop()
            }
        })

        if (ok) {
            currentTimer = TimerFactory.createTimer(ciServerController.frequencyInMilliseconds, 0, true, {
                execOutside {
                    ciServerController.updateLight(model.currentStatus, {
                        ciServerController.serverNotFound()
                        stop()
                    })
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
}
