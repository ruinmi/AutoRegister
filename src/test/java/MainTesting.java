import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * created by elio on 18/09/2022
 */
public class MainTesting {
    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public static Calendar calendar;

    public static void main(String[] args) throws IOException, InterruptedException {
        calendar = Calendar.getInstance();

        for (int i = 0; i < 10; i++) {
            calendar.add(Calendar.DATE, 1);
            changeTime();
            System.out.println("ok");
        }
    }

    public static void changeTime() throws IOException, InterruptedException {
        String date = sdf.format(calendar.getTime());
        String time = "08:50:30";

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

    }
    /**
     * 读取RunTime.exec运行子进程的输入流 和 异常流
     * @param inputStreams 输入流
     */
    public static void readStreamInfo(InputStream... inputStreams){
        ExecutorService executorService = Executors.newFixedThreadPool(inputStreams.length);
        for (InputStream in : inputStreams) {
            executorService.execute(new MyThread (in));
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
        public MyThread(InputStream in){
            this.in = in;
        }
        @Override
        public void run() {
            try{
                BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                String line = null;
                while((line = br.readLine())!=null){
                    System.out.println(" inputStream: " + line);
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
