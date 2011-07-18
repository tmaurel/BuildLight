package buildLight.device.delcom

import buildLight.BuildConstants.LightColor
import buildLight.BuildConstants.LightCommand
import buildLight.device.IDevice
import buildLight.device.hid.HIDLink
import buildLight.device.hid.HIDLinkFactory

class DelcomV2Device implements IDevice {

    private static short VID = (short) 0x0FC5
    private static short PID = (short) 0xB080

    private HIDLink hidLink = HIDLinkFactory.getInstance()

    boolean open() {
        hidLink?.SetVendorID(VID)
        hidLink?.SetProductID(PID)

        boolean opened = hidLink?.isOpened()

        if (!opened) {
           opened = hidLink?.getHIDHandle()
        }

        if(opened) {
            power(LightColor.GREEN, 0)
            power(LightColor.RED, 0)
            power(LightColor.YELLOW, 0)

            flashOff(LightColor.GREEN)
            turnOff(LightColor.GREEN)

            flashOff(LightColor.RED)
            turnOff(LightColor.RED)

            flashOff(LightColor.YELLOW)
            turnOff(LightColor.YELLOW)
        }
        opened
    }

    void sendPacket(DelcomV2Packet packet) {
        if (hidLink?.isOpened()) {
            hidLink?.SetFeatureReport(packet.toByteArray(), (short) 8)
        }
    }

    void close() {
        if (hidLink?.isOpened()) {
            turnOff(LightColor.GREEN)
            flashOff(LightColor.GREEN)

            turnOff(LightColor.RED)
            flashOff(LightColor.RED)

            turnOff(LightColor.YELLOW)
            flashOff(LightColor.YELLOW)

            hidLink?.CloseHIDDevice()
        }
    }

    boolean isOpened() {
        return hidLink?.isOpened()
    }

    def turnOn(LightColor color, int intensity) {
        sendPacket(new DelcomV2Packet(command: LightCommand.TURNON, color: color))
        power color, intensity
    }

    def turnOff(LightColor color) {
        sendPacket(new DelcomV2Packet(command: LightCommand.TURNOFF, color: color))
    }

    def flashOn(LightColor color, int intensity) {
        turnOn color, intensity
        sendPacket(new DelcomV2Packet(command: LightCommand.FLASHON, color: color))
    }

    def flashOff(LightColor color) {
        sendPacket(new DelcomV2Packet(command: LightCommand.FLASHOFF, color: color))
        turnOff color
    }

    private power(LightColor color, int intensity) {
        sendPacket(new DelcomV2Packet(command: LightCommand.CHANGEPOWER, color: color, power: intensity))
    }

    public String toString() {
        "DelcomV2"
    }

}
