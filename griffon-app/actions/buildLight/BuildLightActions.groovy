package buildLight


startAction = action(
        id: 'startAction',
        name: app.getMessage('buildLight.start'),
        closure: {
            view.tabs.selectedIndex = 0
            controller.start()
        }
)

stopAction = action(
        id: 'stopAction',
        name: app.getMessage('buildLight.stop'),
        closure: {
            controller.stop()
        }
)
