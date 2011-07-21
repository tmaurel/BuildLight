ciServer {
    serverType = "HUDSON"
    projectUrl = ""
    needAuth = false
    userName = ""
    password = ""
    frequency = 5
}

light {
    device = "DELCOM_V2"
    intensity = 50
    keepLightOnWhenBuildIsUnstable = true
    keepLightOnWhenBuildFailed = true
    keepLightOnWhenBuildPassed = true
}

general {
    disableRange = false
    disableFrom = "20:00"
    disableUntil = "07:00"
}