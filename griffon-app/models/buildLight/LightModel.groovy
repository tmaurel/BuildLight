package buildLight

import buildLight.constants.LightDevice
import groovy.beans.Bindable

class LightModel {

    def settingsService

    @Bindable LightDevice device

    @Bindable int intensity

    @Bindable Boolean keepLightOnWhenBuildIsUnstable

    @Bindable Boolean keepLightOnWhenBuildFailed

    @Bindable Boolean keepLightOnWhenBuildPassed

    void mvcGroupInit(Map args) {
        def lightSettings = settingsService.config.light
        this.device = LightDevice.valueOf lightSettings.device
        this.intensity = lightSettings.intensity
        this.keepLightOnWhenBuildIsUnstable = lightSettings.keepLightOnWhenBuildIsUnstable
        this.keepLightOnWhenBuildFailed = lightSettings.keepLightOnWhenBuildFailed
        this.keepLightOnWhenBuildPassed = lightSettings.keepLightOnWhenBuildPassed
    }

    void mvcGroupDestroy() {
        def lightSettings = settingsService.config.light
        lightSettings.device = this.device.name()
        lightSettings.intensity = this.intensity
        lightSettings.keepLightOnWhenBuildIsUnstable = this.keepLightOnWhenBuildIsUnstable
        lightSettings.keepLightOnWhenBuildFailed = this.keepLightOnWhenBuildFailed
        lightSettings.keepLightOnWhenBuildPassed = this.keepLightOnWhenBuildPassed
    }

}