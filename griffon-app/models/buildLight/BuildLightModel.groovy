package buildLight

import groovy.beans.Bindable

import buildLight.constants.BuildStatus

class BuildLightModel {

    @Bindable
    BuildStatus currentStatus = BuildStatus.UNKNOWN

}