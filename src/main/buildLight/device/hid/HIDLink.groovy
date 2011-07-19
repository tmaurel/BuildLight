package buildLight.device.hid;

public interface HIDLink {

    public void SetVendorID(short VendorID)

    public void SetProductID(short ProductID)

    public boolean getHIDHandle()

    public boolean isOpened()

    public void CloseHIDDevice()

    public byte SetFeatureReport(byte[] buffer, short buffersize)

    public byte GetFeatureReport(byte[] buffer)

}
