package buildLight

import buildLight.constants.ServerType
import buildLight.views.JCenteredDialog
import javax.swing.JOptionPane
import javax.swing.BorderFactory
import java.awt.Color
import java.awt.GridBagLayout

def userName = textField(
    id: 'userName',
    text: bind('userName', source: model, mutual: true)
)

def password = passwordField(
    id: 'password',
    text: bind('password', source: model, mutual: true)
)

def toggleLoginFields = { enabled ->
    userName.enabled = enabled
    password.enabled = enabled
    enabled
}

widget(
    new JCenteredDialog(this.app.views.BuildLight.mainFrame),
    contentPane: optionPane(
        messageType: JOptionPane.PLAIN_MESSAGE,
        message: app.i18n.getMessage('buildLight.settings.server.testing'),
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
        layoutConstraints: 'fillx, debug',
        colConstraints: '[]40[]'
    )

    label(
        text: app.i18n.getMessage('buildLight.settings.server.type')
    )

    comboBox(
        id:'serverType',
        items: ServerType.values(),
        constraints: 'wrap, grow, span 2',
        selectedItem: bind('serverType', source: model, mutual: true)
    )

    label(
        text: app.i18n.getMessage('buildLight.settings.server.url')
    )

    textField(
        id: 'projectUrl',
        constraints: 'wrap, grow, span 2',
        text: bind('projectUrl', source: model, mutual: true)
    )

    label(
        text: app.i18n.getMessage('buildLight.settings.server.frequency')
    )

    widget(
        new buildLight.views.NumericTextField(),
        id: 'frequency',
        constraints: 'grow',
        columns: 5,
        text: bind('frequency', source: model, mutual: true)
    )

    label(
        text: app.i18n.getMessage('buildLight.settings.server.seconds'),
        constraints: 'wrap'
    )

    vbox()

    checkBox(
        id: 'needAuth',
        label: app.i18n.getMessage('buildLight.settings.server.useAuth'),
        constraints: 'wrap, grow, span 2',
        selected: bind('needAuth', source: model, mutual: true,
            converter: toggleLoginFields,
            reverseConverter: toggleLoginFields
        )
    )

    label(
        text: app.i18n.getMessage('buildLight.settings.server.userName')
    )

    widget(
        userName,
        constraints: 'wrap, grow, span 2'
    )

    label(
        text: app.i18n.getMessage('buildLight.settings.server.password')
    )

    widget(
        password,
        constraints: 'wrap, grow, span 2'
    )


    button(
        testServerAction,
        constraints: 'span 3, center, wrap'
    )

}
