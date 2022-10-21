package com.elio;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * created by elio on 12/09/2022
 */
public class StartApp {
    private static final Robot robot;
    public static final Properties prop = new Properties();
    private static final int GLOBAL_DELAY = 50;
    private static final int TEST_DELAY = 5000;
    public static final DateFormat sdf = SimpleDateFormat.getDateInstance(DateFormat.DATE_FIELD);
    public static final Calendar calendar;
    public static final Scanner sc = new Scanner(System.in);
    public static final HashMap<Integer, String> promptMap = new HashMap<>();
    public static final String CONFIG_NAME = "config.properties";
    // 60天上限
    public static final ArrayList<Integer> date = new ArrayList<>();

    static {
        promptMap.put(1, "因工作关系是否必须进入上飞院（是");
        promptMap.put(2, "来访人员是否有48小时（是");
        promptMap.put(3, "访客人员来访当日抗原检测阴性记录是否上传疫测达并阅读上飞院进院安全告知（是");
        promptMap.put(4, "14天内是否去过或途经中高风险地区（否");
        promptMap.put(5, "随行人信息（删除按钮");
        promptMap.put(6, "我承诺所填信息的真实性");
        promptMap.put(7, "来访开始时间（位置");
        promptMap.put(8, "来访开始时间（确认按钮");
        promptMap.put(9, "《立即提交》按钮");
        promptMap.put(10, "《浏览器关闭按钮》");
        try {
            robot = new Robot();
            loadProp();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        calendar = Calendar.getInstance();
    }

    public static void main(String[] args) throws InterruptedException {
        String choice = "";
        do {
            System.out.println("[1] 开始");
            System.out.println("[2] 测试");
            System.out.println("[3] 修改信息");
            System.out.println("[4] 修改坐标");
            System.out.println("[5] 离开");
            do {
                System.out.print("> ");
                choice = sc.nextLine();
                switch (choice) {
                    case "1":
                        commence(false, false);
                        break;
                    case "2":
                        System.out.println("-----------------------------Testing-----------------------------");
                        commence(true, false);
                        break;
                    case "3":
                        System.out.println("-----------------------------Edit Profile-----------------------------");
                        editProfile();
                        break;
                    case "4":
                        System.out.println("-----------------------------Edit Offset-----------------------------");
                        commence(false, true);
                        break;
                    case "5":
                        System.out.println("Exiting...");
                        return;
                    default:
                        choice = null;
                }
            } while (choice == null);
            System.out.println("##################################################");
        } while (true);
    }

    private static void editProfile() {
        String s;
        sayPrompt("输入姓名(" + prop.getProperty("name") + ")");
        s = sc.nextLine();
        editConfig("name", s);

        sayPrompt("输入身份证(" + prop.getProperty("id") + ")");
        s = sc.nextLine();
        editConfig("id", s);

        sayPrompt("输入电话(" + prop.getProperty("tele") + ")");
        s = sc.nextLine();
        editConfig("tele", s);

        sayPrompt("输入健康码图片路径(" + prop.getProperty("covid-img") + ")");
        s = sc.nextLine();
        editConfig("covid-img", s);

        sayPrompt("输入行程卡图片路径(" + prop.getProperty("routine-img") + ")");
        s = sc.nextLine();
        editConfig("routine-img", s);

        sayPrompt("输入浏览器执行文件路径(默认)(" + prop.getProperty("browser-exec") + ")");
        s = sc.nextLine();
        editConfig("browser-exec", s);
        System.out.println("##########################");
        System.out.println("#  姓名：" + prop.getProperty("name"));
        System.out.println("#  身份证：" + prop.getProperty("id"));
        System.out.println("#  电话：" + prop.getProperty("tele"));
        System.out.println("#  健康码图片路径：" + prop.getProperty("covid-img"));
        System.out.println("#  行程卡图片路径：" + prop.getProperty("routine-img"));
        System.out.println("#  浏览器执行文件路径：" + prop.getProperty("browser-exec"));
        System.out.println("##########################");
    }

    private static void commence(boolean test, boolean editOffset) throws InterruptedException {
        String confirm = "";
        if (!editOffset) {
            sayPrompt("输入要登记的日期（例如：8,9,10,22-26)");
            while (!parseDateInput(sc.nextLine())) {
                sayPrompt("请重新输入");
            }
            System.out.println("即将登记的日期" + date);
        } else {
            date.add(calendar.get(Calendar.DAY_OF_MONTH));
        }
        sayPrompt("开始(y or n)？");
        if (sc.nextLine().equals("y")) {
            for (int d : date) {
                if (!editOffset) {
                    click(10, test, false);
                    calendar.set(Calendar.DATE, d);
                    changeTime();
                    Thread.sleep(1000);
                }
                openBrowser();
                robot.delay(3000);
                click(1, test, editOffset);
                click(2, test, editOffset);
                click(3, test, editOffset);
                nextInput("俞晓波");
                nextInput("13818139567");
                nextInput("项目实施");
                nextInput("上飞院2号楼");
                click(4, test, editOffset);
                nextInput("南京国睿");
                nextInput(prop.getProperty("name"));
                nextInput(prop.getProperty("tele"));
                nextInput(prop.getProperty("id"));
                nextInput("无");
                longPressKey(KeyEvent.VK_PAGE_DOWN, 200);

                fillImg(prop.getProperty("covid-img"));
                fillImg(prop.getProperty("routine-img"));
                longPressKey(KeyEvent.VK_PAGE_DOWN, 200);


                click(5, test, editOffset);

                click(6, test, editOffset);
                click(7, test, editOffset);
                robot.delay(300);
                click(8, test, editOffset);
                robot.delay(500);
                click(9, test, editOffset);
                robot.delay(500);
                click(10, test, editOffset);
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

    private static void click(int line, boolean test, boolean editOffset) {
        testUnit(line, test, editOffset);
        move(line);
        if (line >= 9 && (test || editOffset)) {
            return;
        }
        if (line == 5) {
            robot.delay(500);
        }
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
        System.out.println("正在登记：" + date);
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

    private static boolean parseDateInput(String input) {
        date.clear();
        boolean result = false;
        if (input != null && !input.equals("")) {
            input = input.trim();
        } else {
            return false;
        }
        String[] partialDate = input.split(",");
        try {
            for (String partial : partialDate) {
                if (partial.contains("-")) {
                    String[] bits = partial.split("-");
                    for (int i = Integer.parseInt(bits[0]); i <= Integer.parseInt(bits[1]); i++) {
                        if (!StartApp.date.contains(i)) {
                            if (i > 31 || i < 1) {
                                return false;
                            }
                            StartApp.date.add(i);
                        }
                    }
                } else {
                    int val = Integer.parseInt(partial);
                    if (!StartApp.date.contains(val)) {
                        if (val > 31 || val < 1) {
                            return false;
                        }
                        StartApp.date.add(val);
                    }
                }
            }
            Collections.sort(StartApp.date);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void testUnit(int i, boolean test, boolean editOffset) {
        String output = promptMap.get(i);
        if (test) {
            System.out.println("点击：" + output);
            robot.delay(TEST_DELAY);
        }
        if (editOffset) {
            System.out.println("点击：" + output);
            sayPrompt("移动鼠标，输入p确认坐标");
            while (!sc.nextLine().equals("p")) {
                sayPrompt("移动鼠标，输入p确认坐标");
            }
            Point p = MouseInfo.getPointerInfo().getLocation();
            String offset = (int) p.getX() + "," + (int) p.getY();
            editConfig(String.valueOf(i), offset);
            System.out.println(offset);
        }
    }

    private static void loadProp() {
        try (InputStream is = StartApp.class.getClassLoader().getResourceAsStream(CONFIG_NAME)) {
            assert is != null;
            prop.load(new InputStreamReader(is, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void editConfig(String key, String value) {
        if (value == null || value.equals("")) {
            return;
        }
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(StartApp.class.getClassLoader().getResource(CONFIG_NAME).getPath().substring(1)))) {
            if (key.equals("covid-img") || key.equals("routine-img") || key.equals("browser-exec")) {
                value = value.replaceAll("\\\\+", "/")
                        .replaceAll("/+", "\\\\");
            }
            prop.setProperty(key, value);
            prop.store(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sayPrompt(String prompt) {
        System.out.println("---" + prompt + "   ");
        System.out.print("> ");
    }
}
