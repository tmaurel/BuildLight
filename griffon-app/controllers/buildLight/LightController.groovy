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
                updateLight(BuildStatus.UNKNOWN, BuildStatus.UNSTABLE, false)
            })

            TimerFactory.createTimer(4000, false, {
                updateLight(BuildStatus.UNSTABLE, BuildStatus.SUCCESS, false)
            })

            TimerFactory.createTimer(7000, false, {
                updateLight(BuildStatus.SUCCESS, BuildStatus.BUILDING, false)
            })

            TimerFactory.createTimer(10000, false, {
                updateLight(BuildStatus.BUILDING, BuildStatus.FAILURE, false)
            })

            TimerFactory.createTimer(13000, false, {
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

    def updateLight(BuildStatus previousStatus, BuildStatus newStatus, boolean forceUpdate) {
        switch (newStatus) {
            case BuildStatus.BUILDING:
                if(!previousStatus.equals(newStatus) || forceUpdate) {
                    shutdownLights()
                    lightService.flashLightOn LightColor.YELLOW, model.intensity
                }
                break
            case BuildStatus.SUCCESS:
                if(!previousStatus.equals(newStatus)  || forceUpdate) {
                    shutdownLights()
                    lightService.turnLightOn LightColor.GREEN, model.intensity
                }
                else if(!model.keepLightOnWhenBuildPassed) {
                    shutdownLights()
                }
                break
            case BuildStatus.FAILURE:
                 if(!previousStatus.equals(newStatus)  || forceUpdate) {
                    shutdownLights()
                    lightService.turnLightOn LightColor.RED, model.intensity
                }
                else if(!model.keepLightOnWhenBuildFailed) {
                    shutdownLights()
                }
                break
            case BuildStatus.UNSTABLE:
                 if(!previousStatus.equals(newStatus)  || forceUpdate) {
                    shutdownLights()
                    lightService.turnLightOn LightColor.YELLOW, model.intensity
                }
                else if(!model.keepLightOnWhenBuildIsUnstable) {
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
