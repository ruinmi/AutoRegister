package com.elio.util;

import com.elio.StartApp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.Scanner;

/**
 * created by elio on 14/10/2022
 */
public class GenStarter {
    public static void main(String[] args) throws IOException {
        StartApp.sayPrompt("输入导出bat文件所存放的文件夹");
        File batFile = new File(new Scanner(System.in).nextLine() + File.separator + "main.bat");
        if (!batFile.exists() && !batFile.createNewFile()) {
            System.out.println("创建文件失败！");
            return;
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(batFile.toPath())));
        String path = StartApp.class.getResource("").getPath();
        int classes = path.lastIndexOf("classes");

        String command = "@echo off\n" +
                "java -jar " + path.substring(1, classes) + "AuoRegister-1.0-SNAPSHOT-jar-with-dependencies.jar\n" +
                "pause";
        writer.write(command);
        writer.close();

    }
}
