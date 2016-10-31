/*
 * Copyright 2014 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package com.wanpucs.openapi.httpclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 类HttpDownload.java的实现描述：TODO 类实现描述
 * 
 * @author mint 2014年4月2日 上午9:07:23
 */
public class HttpDownload {

    public static void downloadFile(String path, String url) {

        try {
            // 如果文件存在则删除
            File file = new File(path);
            if (file.isFile() && file.exists()) {
                file.delete();
            }
            URL remote = new URL(url);
            URLConnection urlCon = remote.openConnection();
            InputStream is = urlCon.getInputStream();
            FileOutputStream fos = new FileOutputStream(path);

            byte[] buffer = new byte[1000];
            int bytesRead = is.read(buffer);
            while (bytesRead > 0) {
                fos.write(buffer, 0, bytesRead);
                bytesRead = is.read(buffer);
            }
            is.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
