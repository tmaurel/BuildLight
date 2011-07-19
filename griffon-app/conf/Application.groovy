application {
    title = 'BuildLight'
    startupGroups = ['BuildLight']

    // Should Griffon exit when no Griffon created frames are showing?
    autoShutdown = true

    // If you want some non-standard application class, apply it here
    //frameClass = 'javax.swing.JFrame'
}
mvcGroups {
    // MVC Group for "Light"
    'Light' {
        model = 'buildLight.LightModel'
        actions = 'buildLight.LightActions'
        controller = 'buildLight.LightController'
        view = 'buildLight.LightView'
    }

    // MVC Group for "CIServer"
    'CIServer' {
        model = 'buildLight.CIServerModel'
        actions = 'buildLight.CIServerActions'
        controller = 'buildLight.CIServerController'
        view = 'buildLight.CIServerView'
    }

    // MVC Group for "BuildLight"
    'BuildLight' {
        model = 'buildLight.BuildLightModel'
        actions = 'buildLight.BuildLightActions'
        controller = 'buildLight.BuildLightController'
        view = 'buildLight.BuildLightView'
    }

}
