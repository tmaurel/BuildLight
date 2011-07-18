package buildLight

import buildLight.constants.LightColor
import buildLight.constants.LightDevice
import buildLight.device.DeviceFactory
import buildLight.device.IDevice
import buildLight.constants.BuildStatus

class LightService {

    private IDevice device

    private boolean flashOnBuild

    private int intensity

    def initDevice(LightDevice lightDevice, boolean flashOnBuild, int intensity) {
        closeDevice()
        this.device = DeviceFactory.newDevice lightDevice
        this.flashOnBuild = flashOnBuild
        this.intensity = intensity
        return this.device?.open()
    }

    def closeDevice() {
        if(isOpened()) {
            this.device?.close()
        }
        this.flashOnBuild = false
        this.intensity = 0
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

    def turnFlashOn(LightColor color, int intensity) {
        log.info("Turning flash on for color {} on device {} with intensity {}", [color, this.device, intensity].toArray())
        if(isOpened()) {
            this.device?.flashOn color, intensity
        }
    }

    def turnFlashOff(LightColor color) {
        log.info("Turning flash off for color {} on device {}", [color, this.device].toArray())
        if(isOpened()) {
            this.device?.flashOff color
        }
    }


    def updateLight(BuildStatus status) {
        switch(status) {
            case BuildStatus.BUILDING:
                shutdownLights()
                if(this.flashOnBuild) {
                    turnFlashOn LightColor.YELLOW, this.intensity
                }
                else {
                    turnLightOn LightColor.YELLOW, this.intensity
                }
            break;
            case BuildStatus.SUCCESS:
                shutdownLights()
                turnLightOn LightColor.GREEN, this.intensity
            break;
            case BuildStatus.FAILURE:
                shutdownLights()
                turnLightOn LightColor.RED, this.intensity
            break;
        }
    }

    def shutdownLights() {
        turnLightOff LightColor.GREEN
        turnLightOff LightColor.YELLOW
        turnFlashOff LightColor.YELLOW
        turnLightOff LightColor.RED
    }


}