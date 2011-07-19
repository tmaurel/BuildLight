package buildLight

import java.awt.Window
import javax.swing.JOptionPane

testServerAction = action(
        id: 'testServerAction',
        name: app.i18n.getMessage('buildLight.settings.server.test'),
        closure: {
            def waitbox = view.waitBox
            waitbox.pack()
            doOutside {
                controller.test(
                    { status ->
                        println "TITITITITT : " + status
                        edt {
                            waitBox.hide()
                        }
                        controller.serverFound(status)
                    }, {
                        edt {
                            waitBox.hide()
                        }
                        controller.serverNotFound()
                    }
                )
            }
            waitbox.show()
        },
        mnemonic: 'T',
        accelerator: shortcut('T'),
)
