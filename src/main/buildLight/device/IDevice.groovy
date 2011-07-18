package buildLight.device

import buildLight.BuildConstants.LightColor

interface IDevice {

    boolean open()

    boolean isOpened()

    void close()

    def turnOn(LightColor color, int intensity);

    def turnOff(LightColor color);

    def flashOn(LightColor color, int intensity);

    def flashOff(LightColor color);

}
