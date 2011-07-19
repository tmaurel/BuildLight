package buildLight

import buildLight.constants.BuildStatus
import groovy.beans.Bindable

class BuildLightModel {

    def settingsService

    @Bindable
    BuildStatus currentStatus = BuildStatus.UNKNOWN

    @Bindable
    boolean disableRange

    @Bindable
    String disableFrom

    @Bindable
    String disableUntil

    void mvcGroupInit(Map args) {

        def generalSettings = settingsService.config.general
        this.disableRange = generalSettings.disableRange
        this.disableFrom = generalSettings.disableFrom
        this.disableUntil = generalSettings.disableUntil
    }

    void mvcGroupDestroy() {
        def generalSettings = settingsService.config.general
        generalSettings.disableRange = this.disableRange
        generalSettings.disableFrom = this.disableFrom
        generalSettings.disableUntil = this.disableUntil
    }

}