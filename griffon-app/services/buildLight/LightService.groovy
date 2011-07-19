package buildLight

import buildLight.constants.LightColor
import buildLight.constants.LightDevice
import buildLight.device.DeviceFactory
import buildLight.device.IDevice

class LightService {

    private IDevice device

    boolean initDevice(LightDevice lightDevice) {
        closeDevice()
        this.device = DeviceFactory.newDevice lightDevice
        this.device?.open()
    }

    def closeDevice() {
        log.info("Closing device {}", [this.device].toArray())
        if (isOpened()) {
            this.device?.close()
        }
    }

    boolean isOpened() {
        this.device?.isOpened()
    }

    def turnLightOn(LightColor color, int intensity) {
        log.info("Turning on color {} on device {} with intensity {}", [color, this.device, intensity].toArray())
        if (isOpened()) {
            this.device?.turnOn color, intensity
        }
    }

    def turnLightOff(LightColor color) {
        log.info("Turning off color {} on device {}", [color, this.device].toArray())
        if (isOpened()) {
            this.device?.turnOff color
        }
    }

    def flashLightOn(LightColor color, int intensity) {
        log.info("Turning flash on for color {} on device {} with intensity {}", [color, this.device, intensity].toArray())
        if (isOpened()) {
            this.device?.flashOn color, intensity
        }
    }

    def flashLightOff(LightColor color) {
        log.info("Turning flash off for color {} on device {}", [color, this.device].toArray())
        if (isOpened()) {
            this.device?.flashOff color
        }
    }


}