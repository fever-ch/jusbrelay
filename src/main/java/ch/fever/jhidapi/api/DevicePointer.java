/*
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
package ch.fever.jhidapi.api;

import ch.fever.jhidapi.common.Buffer;
import ch.fever.jhidapi.common.FeatureReport;
import ch.fever.jhidapi.jna.HidApiNative;
import ch.fever.jhidapi.jna.HidDevice;

public class DevicePointer {
    protected DevicePointer(HidApiNative hidApiNative, HidDevice pointer) {
        this.pointer = pointer;
        this.hidApiNative = hidApiNative;
    }

    public HidDevice getPointer() {
        return pointer;
    }


    synchronized public void close() {
        hidApiNative.hid_close(pointer);
    }

    final private HidDevice pointer;
    final private HidApiNative hidApiNative;


    synchronized public void sendFeatureReport(FeatureReport featureReport) {
        if (featureReport == null)
            throw new NullPointerException();
        int i = hidApiNative.hid_send_feature_report(pointer, featureReport, featureReport.size());
        if (i < 0)
            throw new RuntimeException("hid_send_feature_report returned " + i);
    }

    synchronized public void getFeatureReport(FeatureReport featureReport) {
        if (featureReport == null)
            throw new NullPointerException();
        int i = hidApiNative.hid_get_feature_report(pointer, featureReport, featureReport.size());
        if (i < 0)
            throw new HidException("hid_get_feature_report returned " + i);
    }

    synchronized public void write(Buffer data) {
        if (data == null)
            throw new NullPointerException();
        int i = hidApiNative.hid_write(pointer, data, data.size());
        if (i < 0)
            throw new RuntimeException();
    }


}
