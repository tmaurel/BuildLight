package buildLight

import buildLight.constants.BuildStatus
import buildLight.constants.LightColor
import java.awt.Window
import java.awt.event.ActionListener
import javax.swing.JOptionPane
import buildLight.timer.TimerFactory

class LightController {

    def model
    def view

    def lightService

    def initDevice = { successCallback, failureCallback ->
        def opened = lightService.initDevice(model.device)
        if(!opened && failureCallback) {
            failureCallback()
        } else if(successCallback) {
            successCallback()
        }
    }

    def closeDevice = {
        lightService.closeDevice()
    }

    def preview = {
        initDevice(null, null)
        if (lightService.isOpened()) {

            TimerFactory.createTimer(1000, false, {
                updateLight(BuildStatus.UNKNOWN, BuildStatus.SUCCESS)
            })

            TimerFactory.createTimer(4000, false, {
                updateLight(BuildStatus.SUCCESS, BuildStatus.BUILDING)
            })

            TimerFactory.createTimer(7000, false, {
                updateLight(BuildStatus.BUILDING, BuildStatus.FAILURE)
            })

            TimerFactory.createTimer(10000, false, {
                lightService.closeDevice()
                doLater {
                    view.waitBox.hide()
                }
            })
        }
        else {
            edt {
                view.waitBox.hide()
            }
            doLater {
                deviceNotFound()
            }
        }
    }

    def updateLight(BuildStatus previousStatus, BuildStatus newStatus) {
        switch (newStatus) {
            case BuildStatus.BUILDING:
                if(!previousStatus.equals(newStatus)) {
                    shutdownLights()
                    if (model.flashOnBuild) {
                        lightService.flashLightOn LightColor.YELLOW, model.intensity
                    }
                    else {
                        lightService.turnLightOn LightColor.YELLOW, model.intensity
                    }
                }
                break
            case BuildStatus.SUCCESS:
                if(!previousStatus.equals(newStatus)) {
                    shutdownLights()
                    lightService.turnLightOn LightColor.GREEN, model.intensity
                }
                else if(!model.keepLightOnWhenBuildPassed) {
                    shutdownLights()
                }
                break
            case BuildStatus.FAILURE:
                 if(!previousStatus.equals(newStatus)) {
                    shutdownLights()
                    lightService.turnLightOn LightColor.RED, model.intensity
                }
                else if(!model.keepLightOnWhenBuildFailed) {
                    shutdownLights()
                }
                break
            case BuildStatus.UNKNOWN:
            default:
                shutdownLights()
                break
        }
    }

    def shutdownLights() {
        lightService.turnLightOff LightColor.GREEN
        lightService.turnLightOff LightColor.YELLOW
        lightService.flashLightOff LightColor.YELLOW
        lightService.turnLightOff LightColor.RED
    }

    def deviceNotFound() {
        JOptionPane.showMessageDialog(Window.windows.find {it.focused},
                app.i18n.getMessage('buildLight.settings.light.not.found'), app.i18n.getMessage('buildLight.settings.light.not.found'),
                JOptionPane.ERROR_MESSAGE)
    }

    void mvcGroupDestroy() {
        closeDevice()
    }

}
