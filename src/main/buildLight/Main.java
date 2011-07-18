package buildLight;

import buildLight.device.delcom.DelcomV2Packet;
import buildLight.device.hid.HIDLink;
import buildLight.device.hid.w32.HIDW32;

public class Main {

    private static short VID = (short) 0x0FC5;
    private static short PID = (short) 0xB080;

    private static HIDLink HID = new HIDW32();
    private static DelcomV2Packet PACKET = new DelcomV2Packet();

    public static void main(String[] args) {
        if (Main.init()) {
            if (args.length > 1) {
                ledPower(BuildConstants.LightColor.GREEN, Integer.parseInt(args[1]));
                if (args.length == 3 && args[2].equals("flash")) {
                    flashOn(BuildConstants.LightColor.GREEN);
                }
            }
            HID.CloseHIDDevice();
        }
    }

    public static void ledPower(BuildConstants.LightColor led, int power) {
        PACKET.setColor(led);
        PACKET.setCommand(BuildConstants.LightCommand.CHANGEPOWER);
        PACKET.setPower(power);
        HID.SetFeatureReport(PACKET.toByteArray(), (short) 8);
    }

    public static void turnOn(BuildConstants.LightColor led) {
        PACKET.setColor(led);
        PACKET.setCommand(BuildConstants.LightCommand.TURNON);
        HID.SetFeatureReport(PACKET.toByteArray(), (short) 8);
    }

    public static void turnOff(BuildConstants.LightColor led) {
        PACKET.setColor(led);
        PACKET.setCommand(BuildConstants.LightCommand.TURNOFF);
        HID.SetFeatureReport(PACKET.toByteArray(), (short) 8);
    }

    public static void flashOn(BuildConstants.LightColor led) {
        PACKET.setColor(led);
        PACKET.setCommand(BuildConstants.LightCommand.FLASHON);
        HID.SetFeatureReport(PACKET.toByteArray(), (short) 8);
    }

    public static void flashOff(BuildConstants.LightColor led) {
        PACKET.setColor(led);
        PACKET.setCommand(BuildConstants.LightCommand.FLASHOFF);
        HID.SetFeatureReport(PACKET.toByteArray(), (short) 8);
    }

    public static boolean init() {
        HID.SetVendorID(VID);
        HID.SetProductID(PID);

        boolean opened = HID.isOpened();

        if (!opened) {
            opened = HID.getHIDHandle();
        }

        if (opened) {
            ledPower(BuildConstants.LightColor.GREEN, 0);
            ledPower(BuildConstants.LightColor.RED, 0);
            ledPower(BuildConstants.LightColor.YELLOW, 0);

            flashOff(BuildConstants.LightColor.GREEN);
            turnOn(BuildConstants.LightColor.GREEN);

            flashOff(BuildConstants.LightColor.RED);
            turnOn(BuildConstants.LightColor.RED);

            flashOff(BuildConstants.LightColor.YELLOW);
            turnOn(BuildConstants.LightColor.YELLOW);

            return true;
        }

        return false;
    }

}
