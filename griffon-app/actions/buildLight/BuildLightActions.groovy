package buildLight

def toggleConf = { view, enabled ->
    view.tabs.setEnabledAt(1, enabled)
    view.tabs.setEnabledAt(2, enabled)
    view.disableRange.enabled = enabled
    view.disableFrom.enabled = enabled
    view.disableUntil.enabled = enabled
}

startAction = action(
        id: 'startAction',
        name: app.i18n.getMessage('buildLight.start'),
        closure: {
            view.tabs.selectedIndex = 0
            toggleConf(view, false)
            controller.start()
        }
)

stopAction = action(
        id: 'stopAction',
        name: app.i18n.getMessage('buildLight.stop'),
        closure: {
            toggleConf(view, true)
            controller.stop()
        }
)
