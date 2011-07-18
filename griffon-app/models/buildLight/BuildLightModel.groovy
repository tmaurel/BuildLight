package buildLight

import groovy.beans.Bindable
import buildLight.BuildConstants.BuildStatus

class BuildLightModel {

    @Bindable
    BuildStatus currentStatus = BuildStatus.UNKNOWN

}