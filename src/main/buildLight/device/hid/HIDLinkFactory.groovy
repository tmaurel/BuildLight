package buildLight.device.hid

import org.apache.commons.lang.SystemUtils
import buildLight.device.hid.w32.HIDW32

class HIDLinkFactory {

    public static HIDLink getInstance() {
        HIDLink link = null

        if(SystemUtils.IS_OS_WINDOWS) {
            link = new HIDW32();
        }

        link
    }

}
