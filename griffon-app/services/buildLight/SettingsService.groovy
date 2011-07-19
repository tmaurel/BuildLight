package buildLight

class SettingsService {

    static DEFAULT_CONFIG_PATH = "DefaultConfig"

    static USER_CONFIG_PATH = "buildLight.config"

    def config

    public SettingsService() {
        this.readSettings()
    }

    def readSettings() {

        def configToRead
        def userConfigFile = new File(USER_CONFIG_PATH)

        if (!userConfigFile.exists()) {
            log.info "Reading default config"
            configToRead = this.class.classLoader.loadClass(DEFAULT_CONFIG_PATH)
        }
        else {
            log.info "Reading user config"
            configToRead = userConfigFile.toURL()
        }

        this.config = new ConfigSlurper().parse(configToRead);
    }

    def writeSettings() {
        log.info "Saving user config"
        new File(USER_CONFIG_PATH).withWriter { writer ->
            this.config.writeTo(writer)
        }
    }
}