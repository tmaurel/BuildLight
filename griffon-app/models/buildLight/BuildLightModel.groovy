package buildLight

import buildLight.constants.BuildStatus
import groovy.beans.Bindable

class BuildLightModel {

    @Bindable
    BuildStatus currentStatus = BuildStatus.UNKNOWN

}