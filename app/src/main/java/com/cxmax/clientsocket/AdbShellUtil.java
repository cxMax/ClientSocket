package com.cxmax.clientsocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2019-08-09.
 */
public class AdbShellUtil {

    public static void adbCommond(String com) {
        System.out.println("adbCommond...."+com);
        commond("sh", "./adb " + com);
    }

    public static void shellCommond(String[] com) {
        System.out.println("shell commond..."+ Arrays.toString(com));
        try {
            Process process = Runtime
                    .getRuntime()
                    .exec("./adb shell "); // adb
            // shell
            final BufferedWriter outputStream = new BufferedWriter(
                    new OutputStreamWriter(process.getOutputStream()));


            for (String s : com) {
                outputStream.write(s);
                outputStream.write("\n");
            }

            outputStream.flush();
            System.out.println("shell write finished...");
            readError(process.getErrorStream());
            adbCommond("forward tcp:8888 localabstract:puppet-ver1");
            readResult(process.getInputStream());


            while (true) {
                Thread.sleep(Integer.MAX_VALUE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readError(final InputStream errorStream) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                readResult(errorStream);
            }
        }.start();
    }


    ///////////////


    private static void commond(String c, String com) {
        System.out.println("---> " + c + com);
        try {
            Process process = Runtime
                    .getRuntime()
                    .exec(c); // adb
            final BufferedWriter outputStream = new BufferedWriter(
                    new OutputStreamWriter(process.getOutputStream()));


            outputStream.write(com);
            outputStream.write("\n");
            outputStream.write("exit\n");
            outputStream.flush();

            int i = process.waitFor();
            readResult(process.getInputStream());


            System.out.println("------END-------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void readResult(final InputStream stream) {

        System.out.println("read result.....");
        try {
            String line;
            final BufferedReader reader = new BufferedReader(
                    new InputStreamReader(stream));

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("-------END------");
        } catch (IOException e) {
            e.printStackTrace();
            try {
                stream.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
