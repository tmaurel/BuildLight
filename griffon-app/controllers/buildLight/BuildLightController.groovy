package buildLight

class BuildLightController {
    // these will be injected by Griffon
    def model
    def view
    def actions

    def settingsService
    def lightService
    def cIServerService


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
        view.tabs.selectedIndex = 0
        view.tabs.setEnabledAt(1, false)
        view.tabs.setEnabledAt(2, false)
        view.startButon.action = actions.stopAction

    }


    def stop() {
        view.tabs.setEnabledAt(1, true)
        view.tabs.setEnabledAt(2, true)
        view.startButon.action = actions.startAction
    }
}
