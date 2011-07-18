package buildLight

class BuildConstants {

    public enum ServerType {
        HUDSON
    }

    public enum LightDevice {
        DELCOM_V2
    }

    public enum LightColor {
        RED, GREEN, YELLOW
    }

    public enum LightCommand {
        GETINFOS,
        FLASHON,
        FLASHOFF,
        TURNON,
        TURNOFF,
        CHANGEPOWER
    }

    public enum BuildStatus {
        SUCCESS,
        FAILURE,
        BUILDING,
        UNKNOWN
    }

}