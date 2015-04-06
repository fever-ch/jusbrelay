package ch.fever.domotic4j.usbrelay.data;

import com.sun.jna.Structure;
import com.sun.jna.WString;

import java.util.Arrays;
import java.util.List;

public class HidDeviceInfoStructure extends Structure implements Structure.ByReference {

  public String path;

  public short vendorId;
  public short productId;
  public WString serialNumber;

  public short releaseNumber;
  public WString manufacturerString;

  public WString productString;
  public short usagePage;

  public short usage;
  public int interfaceNumber;


  public HidDeviceInfoStructure next;



  @Override
  protected List getFieldOrder() {
    return Arrays.asList(
      "path",
      "vendorId",
      "productId",
      "serialNumber",
      "releaseNumber",
      "manufacturerString",
      "productString",
      "usagePage",
      "usage",
      "interfaceNumber",
      "next"
    );
  }

  /**
   * @return A string representation of the attached device
   */
  public String show() {
    HidDeviceInfoStructure u = this;
    String str = "HidDevice\n";
    str += "\tpath:" + u.path + ">\n";
    str += "\tvendorId: " + Integer.toHexString(u.vendorId) + "\n";
    str += "\tproductId: " + Integer.toHexString(u.productId) + "\n";
    str += "\tserialNumber: " + u.serialNumber + ">\n";
    str += "\treleaseNumber: " + u.releaseNumber + "\n";
    str += "\tmanufacturerString: " + u.manufacturerString + ">\n";
    str += "\tproductString: " + u.productString + ">\n";
    str += "\tusagePage: " + u.usagePage + "\n";
    str += "\tusage: " + u.usage + "\n";
    str += "\tinterfaceNumber: " + u.interfaceNumber + "\n";
    return str;
  }
}

