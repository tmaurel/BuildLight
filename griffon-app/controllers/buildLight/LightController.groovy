package buildLight


import java.awt.event.ActionListener
import java.awt.Window
import javax.swing.JOptionPane
import buildLight.constants.BuildStatus
import buildLight.constants.LightColor


class LightController {

    def model
    def view

    def lightService

    def preview = {

        def opened = lightService.initDevice model.device

        if(opened) {

            startTimer(1000, false, {
                updateLight(BuildStatus.SUCCESS)
            })

            startTimer(4000, false, {
                updateLight(BuildStatus.BUILDING)
            })

            startTimer(7000, false, {
                updateLight(BuildStatus.FAILURE)
            })

            startTimer(10000, false, {
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

    def updateLight(BuildStatus status) {
        switch(status) {
            case BuildStatus.BUILDING:
                shutdownLights()
                if(model.flashOnBuild) {
                    lightService.turnFlashOn LightColor.YELLOW, model.intensity
                }
                else {
                    lightService.turnLightOn LightColor.YELLOW, model.intensity
                }
            break;
            case BuildStatus.SUCCESS:
                shutdownLights()
                lightService.turnLightOn LightColor.GREEN, model.intensity
            break;
            case BuildStatus.FAILURE:
                shutdownLights()
                lightService.turnLightOn LightColor.RED, model.intensity
            break;
        }
    }

    def shutdownLights() {
        lightService.turnLightOff LightColor.GREEN
        lightService.turnLightOff LightColor.YELLOW
        lightService.turnFlashOff LightColor.YELLOW
        lightService.turnLightOff LightColor.RED
    }

    def deviceNotFound() {
        JOptionPane.showMessageDialog(Window.windows.find{it.focused},
            app.i18n.getMessage('buildLight.settings.light.not.found'), app.i18n.getMessage('buildLight.settings.light.not.found'),
            JOptionPane.ERROR_MESSAGE)
    }

    def startTimer(delay, repeat, closure) {
        javax.swing.Timer timer = new javax.swing.Timer(delay, [
               actionPerformed: { e ->
                   closure()
               }
        ] as ActionListener)
        timer.repeats = repeat
        timer.start()
    }

    void mvcGroupDestroy() {
        lightService.closeDevice()
    }

}
