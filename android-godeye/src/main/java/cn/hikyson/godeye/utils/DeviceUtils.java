/*
 * Copyright (C) 2016 MarkZhai (http://zhaiyifan.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.hikyson.godeye.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

public class DeviceUtils {

    private static int sCoreNum = 0;

    private DeviceUtils() {
        throw new InstantiationError("Must not instantiate this class");
    }

    public static int getCpuNumCores() {
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                return Pattern.matches("cpu[0-9]", pathname.getName());
            }
        }

        if (sCoreNum == 0) {
            try {
                File dir = new File("/sys/devices/system/cpu/");
                File[] files = dir.listFiles(new CpuFilter());
                sCoreNum = files.length;
            } catch (Exception e) {
                sCoreNum = 1;
            }
        }
        return sCoreNum;
    }
}