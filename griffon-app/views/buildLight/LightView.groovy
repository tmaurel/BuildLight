package buildLight

import buildLight.constants.LightDevice
import buildLight.views.JCenteredDialog
import java.awt.Color
import java.awt.GridBagLayout
import javax.swing.BorderFactory
import javax.swing.JOptionPane
import javax.swing.JSlider

widget(
        new JCenteredDialog(this.app.views.BuildLight.mainFrame),
        contentPane: optionPane(
                messageType: JOptionPane.PLAIN_MESSAGE,
                message: app.getMessage('buildLight.settings.light.previewing'),
                options: [],
                border: BorderFactory.createLineBorder(Color.lightGray)
        ),
        id: 'waitBox',
        undecorated: true,
        modal: true,
        layout: new GridBagLayout()
)

panel(
        id: 'root',
        constraints: 'grow'
) {
    migLayout(
            layoutConstraints: 'fillx',
            colConstraints: '[]40[]'
    )

    label(
            text: app.getMessage('buildLight.settings.light.device')
    )

    comboBox(
            id: 'device',
            constraints: 'wrap, grow',
            items: LightDevice.values(),
            selectedItem: bind('device', source: model, mutual: true)
    )


    label(
            text: app.getMessage('buildLight.settings.light.intensity')
    )

    slider(
            id: 'intensity',
            minimum: 0,
            maximum: 100,
            minorTickSpacing: 5,
            majorTickSpacing: 25,
            snapToTicks: true,
            paintTicks: true,
            paintTrack: true,
            paintLabels: true,
            orientation: JSlider.HORIZONTAL,
            constraints: 'wrap, grow',
            value: bind('intensity', source: model, mutual: true)
    )

    label(
            text: app.getMessage('buildLight.settings.light.keepLightWhenIsUnstable')
    )

    checkBox(
            id: 'keepLightOnWhenBuildIsUnstable',
            constraints: 'wrap, grow',
            selected: bind('keepLightOnWhenBuildIsUnstable', source: model, mutual: true)
    )

    label(
            text: app.getMessage('buildLight.settings.light.keepLightWhenFailed')
    )

    checkBox(
            id: 'keepLightOnWhenBuildFailed',
            constraints: 'wrap, grow',
            selected: bind('keepLightOnWhenBuildFailed', source: model, mutual: true),
    )

    label(
            text: app.getMessage('buildLight.settings.light.keepLightWhenSucceeded')
    )

    checkBox(
            id: 'keepLightOnWhenBuildPassed',
            constraints: 'wrap, grow',
            selected: bind('keepLightOnWhenBuildPassed', source: model, mutual: true)
    )

    button(
            previewAction,
            constraints: 'span 2, center, wrap'
    )


}
