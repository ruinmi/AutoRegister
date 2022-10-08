import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * created by elio on 12/09/2022
 */
public class StartApp {
    private static final Robot robot;
    private static final Properties prop;
    private static final int GLOBAL_DELAY = 50;
    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public static final Calendar calendar;
    // 60天上限
    public static final ArrayList<Integer> date = new ArrayList<>();
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
        String propDate = prop.getProperty("date");
        if (propDate != null && !propDate.equals("")) {
            propDate = propDate.trim();
        } else {
            throw new RuntimeException("Can not get the value from 'date' in properties");
        }
        String[] partialDate = propDate.split(",");
        try {
            for (String partial : partialDate) {
                if (partial.contains("-")) {
                    String[] bits = partial.split("-");
                    for (int i = Integer.parseInt(bits[0]); i <= Integer.parseInt(bits[1]); i++) {
                        if (!date.contains(i)) {
                            date.add(i);
                        }
                    }
                } else {
                    int val = Integer.parseInt(partial);
                    if (!date.contains(val)) {
                        date.add(val);
                    }
                }
            }
            Collections.sort(date);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while parsing date");
        }
        calendar = Calendar.getInstance();
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Are you sure that the date" + date + " is correct and START(y or n)?");
        String s;
        if ((s = sc.nextLine()).equals("y")) {
            for (int d : date){
                calendar.set(Calendar.DATE, d);
                changeTime();
                Thread.sleep(1000);
                openBrowser();
                robot.delay(3000);
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
                robot.delay(300);
                click(8);
                robot.delay(500);
                if (prop.getProperty("test").equals("true")) {
                    continue;
                }
                click(9);
                robot.delay(500);
                click(10);
            }
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

    public static void openBrowser() {
        String url = "https://qy.do1.com.cn/open/vp/module/form.html?agentCode=form&corp_id=wx75f8bc632d52198a#/open/add?id=formbd2303bfe7344c3ebaac7c22341c8b54";
        try {
            ProcessBuilder proc = new ProcessBuilder(
                    prop.getProperty("browser-exec"),
                    url);
            proc.start();
        } catch (Exception e) {
            System.out.println("Error executing program.");
        }
    }

    public static void changeTime() {
        String date = sdf.format(calendar.getTime());
        String time = "08:50:30";
        try {
            Runtime run = Runtime.getRuntime();
            String timeCommand = "cmd.exe /c time" + " " + time;
            String dateCommand = "cmd.exe /c date" + " " + date;
            Process p1 = run.exec(timeCommand);
            readStreamInfo(p1.getInputStream(), p1.getErrorStream());
            p1.waitFor();
            p1.destroy();
            Process p2 = run.exec(dateCommand);
            readStreamInfo(p2.getInputStream(), p2.getErrorStream());
            p2.waitFor();
            p2.destroy();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取RunTime.exec运行子进程的输入流 和 异常流
     *
     * @param inputStreams 输入流
     */
    public static void readStreamInfo(InputStream... inputStreams) {
        ExecutorService executorService = Executors.newFixedThreadPool(inputStreams.length);
        for (InputStream in : inputStreams) {
            executorService.execute(new MyThread(in));
        }
        executorService.shutdown();
    }

    /**
     * @author lirong
     * @desc
     * @date 2019/06/13 21:25
     */
    static class MyThread implements Runnable {
        private InputStream in;

        public MyThread(InputStream in) {
            this.in = in;
        }

        @Override
        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "GBK"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(" inputStream: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
