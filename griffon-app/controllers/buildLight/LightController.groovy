package buildLight


import java.awt.event.ActionListener
import java.awt.Window
import javax.swing.JOptionPane
//import buildLight.constants.BuildStatus
import buildLight.constants.LightColor


class LightController {

    def model
    def view

    def lightService

    def preview = {

        def opened = lightService.initDevice model.device

        if(opened) {

/*
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
            })*/
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
