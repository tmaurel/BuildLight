package buildLight


startAction = action(
        id: 'startAction',
        name: app.i18n.getMessage('buildLight.start'),
        closure: {
            view.tabs.selectedIndex = 0
            controller.start()
        }
)

stopAction = action(
        id: 'stopAction',
        name: app.i18n.getMessage('buildLight.stop'),
        closure: {
            controller.stop()
        }
)
