/**
 * Copyright (C) 2015 Raphael P. Barazzutti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.fever.usbrelay.jna;

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

