package buildLight

import buildLight.server.ICIServer.CIServerNotFound
import java.awt.Window
import javax.swing.JOptionPane

class CIServerController {

    def model
    def view

    def cIServerService

    def updateLight = true

    def initServer = { successCallback, failureCallback ->
        cIServerService.initServer(model.serverType, model.projectUrl)
        if (model.needAuth) {
            cIServerService.setCredentials(model.userName, model.password)
        }

        try {
            def lastBuildStatus = cIServerService.lastBuildStatus

            if(successCallback) {
                println successCallback
                successCallback(lastBuildStatus)
            }
        }

        catch (CIServerNotFound e) {
            if(failureCallback) {
                failureCallback()
            }
        }
    }

    def getFrequencyInMilliseconds() {
        model.frequency.toInteger() * 1000
    }

    def test = { successCallback, failureCallback ->
        initServer(successCallback, failureCallback)
    }

    def updateStatus = { updateLight, currentStatus, failureCallback ->
        def lightController = app.controllers.Light
        def mainController = app.controllers.BuildLight
        try {
            def status = cIServerService.lastBuildStatus
            if(updateLight) {
                lightController.updateLight(currentStatus, status, updateLight != this.updateLight)
            }
            this.updateLight = updateLight
            mainController.setCurrentStatus(status)
        }
        catch (CIServerNotFound e) {
            failureCallback()
        }
    }

    def serverFound(status) {
        doLater {
            JOptionPane.showMessageDialog(Window.windows.find {it.focused},
                    app.i18n.getMessage('buildLight.settings.server.found.with.status', [app.i18n.getMessage("buildLight.current.status.$status")]), app.i18n.getMessage('buildLight.settings.server.found'),
                    JOptionPane.INFORMATION_MESSAGE)
        }
    }

    def serverNotFound() {
        doLater {
            JOptionPane.showMessageDialog(Window.windows.find {it.focused},
            app.i18n.getMessage('buildLight.settings.server.not.found'), app.i18n.getMessage('buildLight.settings.server.not.found'),
            JOptionPane.ERROR_MESSAGE)
        }
    }
}
