package buildLight

previewAction = action(
        id: 'previewAction',
        name: app.getMessage('buildLight.settings.light.preview'),
        closure: {
            def waitbox = view.waitBox
            waitbox.pack()
            doOutside {
                controller.preview()
            }
            waitbox.show()
        },
        mnemonic: 'P',
        accelerator: shortcut('P'),
)
