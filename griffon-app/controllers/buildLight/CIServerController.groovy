package buildLight

import buildLight.server.ICIServer.CIServerNotFound
import javax.swing.JOptionPane
import java.awt.Window

class CIServerController {

    def model
    def view

    def cIServerService

    def test = {
        initServer()

        try {
            def lastBuildStatus = cIServerService.lastBuildStatus
            serverFound(lastBuildStatus)
        }

        catch(CIServerNotFound e) {
            serverNotFound()
        }
    }

    def initServer() {
        cIServerService.initServer(model.serverType, model.projectUrl)
        if(model.needAuth) {
            cIServerService.setCredentials(model.userName, model.password)
        }
    }

    def serverFound(status) {
        edt {
            view.waitBox.hide()
        }
        doLater {
            JOptionPane.showMessageDialog(Window.windows.find{it.focused},
                app.i18n.getMessage('buildLight.settings.server.found.with.status', [app.i18n.getMessage("buildLight.current.status.$status")]), app.i18n.getMessage('buildLight.settings.server.found'),
                JOptionPane.INFORMATION_MESSAGE)
        }
    }

    def serverNotFound() {
        edt {
            view.waitBox.hide()
        }
        doLater {
            JOptionPane.showMessageDialog(Window.windows.find{it.focused},
                app.i18n.getMessage('buildLight.settings.server.not.found'), app.i18n.getMessage('buildLight.settings.server.not.found'),
                JOptionPane.ERROR_MESSAGE)
        }
    }
}
