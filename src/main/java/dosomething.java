import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

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
        for (int i = 20; i >= 0; i--) {
            Thread.sleep(2000);
            scroll();
            click(923, 727);
            robot.delay(2800);
            click(918, 665);
            robot.delay(2800);
            click(869,877);
        }
    }

    private static void click(int x, int y) {
        move(x, y);
        robot.delay(GLOBAL_DELAY);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    private static void scroll() {
        robot.delay(GLOBAL_DELAY);
        robot.mouseWheel(100);
    }
    private static void move(int x, int y) {
        robot.delay(GLOBAL_DELAY);
        robot.mouseMove(x, y);
    }

}
