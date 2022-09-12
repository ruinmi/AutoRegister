import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;

/**
 * created by elio on 12/09/2022
 */
public class StartApp {
    private static final Robot robot;
    private static final Properties prop;
    private static final int GLOBAL_DELAY = 50;

    static {
        try {
            InputStream is = StartApp.class.getClassLoader().getResourceAsStream("location.properties");
            prop = new Properties();
            assert is != null;
            prop.load(new InputStreamReader(is, StandardCharsets.UTF_8));
            robot = new Robot();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter any key to start[Except 'q' or 'quit']: ");
        String s;
        while (!(s = sc.nextLine()).equals("q") && !s.equals("quit")) {
            click(1);
            click(2);
            click(3);
            nextInput("俞晓波");
            nextInput("13818139567");
            nextInput("项目实施");
            nextInput("上飞院2号楼");
            click(4);
            nextInput("南京国睿");
            nextInput(prop.getProperty("name"));
            nextInput(prop.getProperty("tele"));
            nextInput(prop.getProperty("id"));
            nextInput("无");
            longPressKey(KeyEvent.VK_PAGE_DOWN, 200);

            fillImg(prop.getProperty("covid-img"));
            fillImg(prop.getProperty("routine-img"));
            longPressKey(KeyEvent.VK_PAGE_DOWN, 200);

            move(5);
            robot.delay(500);
            click(5);

            click(6);
            click(7);
            System.out.print("Enter any key to start[Except 'q' or 'quit']: ");
        }
    }

    private static void fillImg(String path) {
        longPressKey(KeyEvent.VK_TAB, 0);
        longPressKey(KeyEvent.VK_ENTER, 0);
        robot.delay(1000);
        input(path);
        longPressKey(KeyEvent.VK_ENTER, 0);
        robot.delay(2000);
    }

    private static void move(int line) {
        robot.delay(GLOBAL_DELAY);
        String[] axisString = prop.getProperty(String.valueOf(line)).split(",");
        robot.mouseMove(Integer.parseInt(axisString[0]), Integer.parseInt(axisString[1]));
    }

    private static void click(int line) {
        move(line);
        robot.delay(GLOBAL_DELAY);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    private static void input(String text) {
        robot.delay(GLOBAL_DELAY);
        setClipboardString(text);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(GLOBAL_DELAY);
        setClipboardString("");
    }

    private static void nextInput(String text) {
        robot.delay(GLOBAL_DELAY);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        input(text);
    }

    private static void longPressKey(int key, int time) {
        robot.delay(GLOBAL_DELAY);
        robot.keyPress(key);
        robot.delay(time);
        robot.keyRelease(key);
    }

    /**
     * 把文本设置到剪贴板（复制）
     */
    public static void setClipboardString(String text) {
        // 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 封装文本内容
        Transferable trans = new StringSelection(text);
        // 把文本内容设置到系统剪贴板
        clipboard.setContents(trans, null);
    }

    /**
     * 从剪贴板中获取文本（粘贴）
     */
    public static String getClipboardString() {
        // 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // 获取剪贴板中的内容
        Transferable trans = clipboard.getContents(null);

        if (trans != null) {
            // 判断剪贴板中的内容是否支持文本
            if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    // 获取剪贴板中的文本内容
                    String text = (String) trans.getTransferData(DataFlavor.stringFlavor);
                    return text;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
