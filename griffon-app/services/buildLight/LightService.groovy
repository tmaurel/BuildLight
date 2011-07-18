package buildLight

import buildLight.BuildConstants.LightColor
import buildLight.device.IDevice
import buildLight.BuildConstants.LightDevice
import buildLight.device.DeviceFactory

class LightService {

    private IDevice device

    def initDevice(LightDevice lightDevice) {
        closeDevice()
        this.device = DeviceFactory.newDevice lightDevice
        return this.device?.open()
    }

    def closeDevice() {
        if(isOpened()) {
            this.device?.close()
        }
    }

    def isOpened() {
        return this.device?.isOpened()
    }

    def turnLightOn(LightColor color, int intensity) {
        log.info("Turning on color {} on device {} with intensity {}", [color, this.device, intensity].toArray())
        if(isOpened()) {
            this.device?.turnOn color, intensity
        }
    }

    def turnLightOff(LightColor color) {
        log.info("Turning off color {} on device {}", [color, this.device].toArray())
        if(isOpened()) {
            this.device?.turnOff color
        }
    }

    def flashLightOn(LightColor color, int intensity) {
        log.info("Turning flash on for color {} on device {} with intensity {}", [color, this.device, intensity].toArray())
        if(isOpened()) {
            this.device?.flashOn color, intensity
        }
    }

    def flashOffAction(LightColor color) {
        log.info("Turning flash off for color {} on device {}", [color, this.device].toArray())
        if(isOpened()) {
            this.device?.flashOff color
        }
    }



}