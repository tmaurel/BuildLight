package buildLight.device.delcom

import buildLight.constants.LightColor
import buildLight.constants.LightCommand

class DelcomV2Packet {

    LightColor color
    LightCommand command
    Integer power

    public byte[] toByteArray() {
        byte[] bytes = new byte[16]
        bytes[0] = this.getByteForMajorCommand()
        bytes[1] = this.getByteForMinorCommand()
        bytes[2] = this.getByteForDataLSB()
        bytes[3] = this.getByteForDataMSB()
        bytes
    }

    private byte getByteForMajorCommand() {
        byte majorByte
        switch (this.command) {
            case LightCommand.GETINFOS:
                majorByte = (byte) 104
                break

            default:
                majorByte = (byte) 101
                break
        }
        majorByte
    }

    private byte getByteForMinorCommand() {
        byte minorByte
        switch (this.command) {
            case LightCommand.FLASHOFF:
            case LightCommand.FLASHON:
                minorByte = (byte) 20
                break

            case LightCommand.CHANGEPOWER:
                minorByte = (byte) 34
                break

            case LightCommand.TURNON:
            case LightCommand.TURNOFF:
                minorByte = (byte) 12
                break

            case LightCommand.GETINFOS:
            default:
                minorByte = (byte) 0
                break
        }
        minorByte
    }

    private byte getByteForDataLSB() {
        byte lsb
        switch (this.command) {
            case LightCommand.FLASHOFF:
            case LightCommand.TURNON:
                lsb = this.getLedColorByteForPowerOn()
                break

            case LightCommand.FLASHON:
            case LightCommand.TURNOFF:
                lsb = (byte) 0
                break

            case LightCommand.CHANGEPOWER:
                lsb = this.getLedColorByteForPowerChange()
                break

            case LightCommand.GETINFOS:
            default:
                lsb = (byte) 0
                break
        }
        lsb
    }

    private byte getByteForDataMSB() {
        byte msb
        switch (this.command) {
            case LightCommand.FLASHOFF:
            case LightCommand.TURNON:
                msb = (byte) 0
                break

            case LightCommand.FLASHON:
            case LightCommand.TURNOFF:
                msb = this.getLedColorByteForPowerOn();
                break;

            case LightCommand.CHANGEPOWER:
                msb = (byte) this.power;
                break;

            case LightCommand.GETINFOS:
            default:
                msb = (byte) 0;
                break;
        }
        msb
    }

    private byte getLedColorByteForPowerChange() {
        byte colorByte
        switch (this.color) {
            case LightColor.GREEN:
                colorByte = (byte) 0
                break

            case LightColor.RED:
                colorByte = (byte) 1
                break

            case LightColor.YELLOW:
            default:
                colorByte = (byte) 2
                break
        }
        colorByte
    }

    private byte getLedColorByteForPowerOn() {
        byte colorByte
        switch (this.color) {
            case LightColor.GREEN:
                colorByte = (byte) 1
                break

            case LightColor.RED:
                colorByte = (byte) 2
                break

            case LightColor.YELLOW:
            default:
                colorByte = (byte) 4
                break
        }
        colorByte
    }


}
