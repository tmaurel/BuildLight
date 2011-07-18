package buildLight

testServerAction = action(
    id: 'testServerAction',
    name: app.i18n.getMessage('buildLight.settings.server.test'),
    closure: {
        def waitbox = view.waitBox
        waitbox.pack()
        doOutside {
            controller.test()
        }
        waitbox.show()
    },
    mnemonic: 'T',
    accelerator: shortcut('T'),
)
