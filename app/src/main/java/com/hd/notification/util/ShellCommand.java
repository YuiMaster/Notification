package com.hd.notification.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class ShellCommand {
    public static synchronized String run(String[] cmd, String workdirectory) throws IOException {
        String result = "";
        try {
            ProcessBuilder builder = new ProcessBuilder(cmd);
            InputStream in = null;
            //设置一个路径
            if (workdirectory != null) {
                builder.directory(new File(workdirectory));
                builder.redirectErrorStream(true);
                Process process = builder.start();
                in = process.getInputStream();
                byte[] re = new byte[1024];
                while (in.read(re) != -1) {
                    result = result + new String(re,"UTF-8");
                }
            }
            if (in != null) {
                in.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
