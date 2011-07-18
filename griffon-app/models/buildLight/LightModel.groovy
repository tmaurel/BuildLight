package buildLight

import groovy.beans.Bindable
import buildLight.constants.LightDevice

class LightModel {

    def settingsService

    @Bindable LightDevice device

    @Bindable int intensity

    @Bindable Boolean flashOnBuild

    @Bindable Boolean keepLightOnWhenBuildFailed

    @Bindable Boolean keepLightOnWhenBuildPassed

    void mvcGroupInit(Map args) {
        def lightSettings = settingsService.config.light
        this.device = LightDevice.valueOf lightSettings.device
        this.intensity = lightSettings.intensity
        this.flashOnBuild = lightSettings.flashOnBuild
        this.keepLightOnWhenBuildFailed = lightSettings.keepLightOnWhenBuildFailed
        this.keepLightOnWhenBuildPassed = lightSettings.keepLightOnWhenBuildPassed
    }

    void mvcGroupDestroy() {
        def lightSettings = settingsService.config.light
        lightSettings.device = this.device.name()
        lightSettings.intensity = this.intensity
        lightSettings.flashOnBuild = this.flashOnBuild
        lightSettings.keepLightOnWhenBuildFailed = this.keepLightOnWhenBuildFailed
        lightSettings.keepLightOnWhenBuildPassed = this.keepLightOnWhenBuildPassed
    }

}