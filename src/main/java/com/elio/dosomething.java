package com.elio;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import static com.elio.StartApp.getClipboardString;
import static com.elio.StartApp.pressCombinedKey;

/**
 * created by elio on 25/09/2022
 */
public class dosomething {
    private static final Robot robot;
    private static final int GLOBAL_DELAY = 50;
    static {
        try {
            robot = new Robot();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000);
        pressCombinedKey(KeyEvent.VK_CONTROL, KeyEvent.VK_C);
        String clipboardString = getClipboardString();
        System.out.println(clipboardString);
    }
    public static void pressCombinedKey(int mainKey, int subKey) {
        robot.delay(GLOBAL_DELAY);
        robot.keyPress(mainKey);
        robot.delay(GLOBAL_DELAY);

        robot.keyPress(subKey);
        robot.delay(GLOBAL_DELAY);

        robot.keyRelease(subKey);
        robot.delay(GLOBAL_DELAY);

        robot.keyRelease(mainKey);
    }


}
