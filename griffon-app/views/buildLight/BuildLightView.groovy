package buildLight

import buildLight.constants.BuildStatus
import java.awt.Color
import javax.swing.JFrame
import buildLight.timer.TimeArray

def disableFrom = comboBox(
    id: 'disableFrom',
    items: TimeArray.getArray(),
    selectedItem: bind('disableFrom', source: model, mutual: true)
)

def disableUntil = comboBox(
    id: 'disableUntil',
    items: TimeArray.getArray(),
    selectedItem: bind('disableUntil', source: model, mutual: true)
)

def toggleDisableFields = { enabled ->
    disableFrom.enabled = enabled
    disableUntil.enabled = enabled
    enabled
}

def mainFrame = application(title: 'BuildLight',
        id: 'mainFrame',
        minimumSize: [400, 300],
        defaultCloseOperation: JFrame.EXIT_ON_CLOSE,
        pack: true,
        //location:[50,50],
        locationByPlatform: true,
        iconImage: imageIcon('/griffon-icon-48x48.png').image,
        iconImages: [imageIcon('/griffon-icon-48x48.png').image,
                imageIcon('/griffon-icon-32x32.png').image,
                imageIcon('/griffon-icon-16x16.png').image]) {
    // add content here
    migLayout(layoutConstraints: 'fill')
    tabbedPane(
            id: 'tabs',
            constraints: 'grow, center'
    ) {

        panel(title: app.i18n.getMessage('buildLight.settings.general')) {
            migLayout(layoutConstraints: 'fill')

            label(
                    text: app.i18n.getMessage('buildLight.settings.general.label'),
                    constraints: 'growy, wrap, align center'
            )

            panel(
                    constraints: 'align center'
            ) {
                migLayout(
                    layoutConstraints: 'fill',
                    colConstraints: '[]40[]'
                )

                label(
                        text: app.i18n.getMessage('buildLight.current.status'),
                        constraints: 'grow'
                )

                label(
                        id: 'statusLabel',
                        text: bind('currentStatus', source: model, converter: {
                            def color
                            switch (it) {
                                case BuildStatus.BUILDING:
                                    color = Integer.toHexString(Color.yellow.RGB)
                                    break
                                case BuildStatus.FAILURE:
                                    color = Integer.toHexString(Color.red.RGB)
                                    break
                                case BuildStatus.SUCCESS:
                                    color = Integer.toHexString(Color.green.RGB)
                                    break
                                case BuildStatus.UNKNOWN:
                                default:
                                    color = Integer.toHexString(Color.gray.RGB)
                                    break
                            }
                            "<html><font color=\"#${color.substring(2, color.length())}\">${app.i18n.getMessage("buildLight.current.status.$it")}</font></html>"
                        }),
                        constraints: 'grow, wrap'
                )

                vbox(
                    constraints: 'growy, wrap'
                )

                checkBox(
                        id: 'disableRange',
                        label: app.i18n.getMessage('buildLight.settings.general.disable.range'),
                        constraints: 'wrap, grow, span 2',
                        selected: bind('disableRange', source: model, mutual: true,
                                converter: toggleDisableFields,
                                reverseConverter: toggleDisableFields
                        )
                )

                label(
                        text: app.i18n.getMessage('buildLight.settings.general.disable.from')
                )

                widget(
                    disableFrom,
                    constraints: 'wrap, grow',
                )

                label(
                    text: app.i18n.getMessage('buildLight.settings.general.disable.until')
                )

                widget(
                    disableUntil,
                    constraints: 'wrap, grow',
                )
            }
        }

        panel(title: app.i18n.getMessage('buildLight.settings.server')) {
            borderLayout()
            widget(id: 'serverConfigContainer', buildMVCGroup('CIServer').view.root)
        }

        panel(title: app.i18n.getMessage('buildLight.settings.light')) {
            borderLayout()
            widget(id: 'lightConfigContainer', buildMVCGroup('Light').view.root)
        }

    }

    toolBar(minimumSize: [0, 40], floatable: false, constraints: 'south') {
        migLayout(layoutConstraints: 'fill')
        button(
                startAction,
                id: 'startButon',
                constraints: 'grow, east, gap 0 5 0 5'
        )
    }

}

