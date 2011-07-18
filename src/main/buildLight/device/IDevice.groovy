package buildLight.device

import buildLight.constants.LightColor

interface IDevice {

    boolean open()

    boolean isOpened()

    void close()

    def turnOn(LightColor color, int intensity);

    def turnOff(LightColor color);

    def flashOn(LightColor color, int intensity);

    def flashOff(LightColor color);

}
