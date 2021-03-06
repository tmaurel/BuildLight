package buildLight

import buildLight.constants.ServerType
import groovy.beans.Bindable

class CIServerModel {

    def settingsService

    @Bindable ServerType serverType

    @Bindable String projectUrl

    @Bindable Boolean needAuth

    @Bindable String userName

    @Bindable String password

    @Bindable String frequency

    @Bindable Integer retries

    void mvcGroupInit(Map args) {
        def ciServerSettings = settingsService.config.ciServer
        this.serverType = ServerType.valueOf ciServerSettings.serverType
        this.projectUrl = ciServerSettings.projectUrl
        this.needAuth = ciServerSettings.needAuth
        this.userName = ciServerSettings.userName
        this.password = new String(ciServerSettings.password.decodeBase64())
        this.frequency = ciServerSettings.frequency
        this.retries = ciServerSettings.retries
    }

    void mvcGroupDestroy() {
        def ciServerSettings = settingsService.config.ciServer
        ciServerSettings.serverType = this.serverType.name()
        ciServerSettings.projectUrl = this.projectUrl
        ciServerSettings.needAuth = this.needAuth
        ciServerSettings.userName = this.userName
        ciServerSettings.password = this.password.bytes.encodeBase64().toString()
        ciServerSettings.frequency = this.frequency
        ciServerSettings.retries = this.retries
    }

}