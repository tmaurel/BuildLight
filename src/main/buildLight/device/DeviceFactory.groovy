package buildLight.device

import buildLight.constants.LightDevice
import buildLight.device.delcom.DelcomV2Device

class DeviceFactory {

    public static IDevice newDevice(LightDevice device) {
        switch(device) {
            case LightDevice.DELCOM_V2:
                return new DelcomV2Device()
            break;
        }
    }

}
