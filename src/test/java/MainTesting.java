import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * created by elio on 18/09/2022
 */
public class MainTesting {
    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public static Calendar calendar = Calendar.getInstance();

    public static void main(String[] args) throws ParseException {
        Date date = sdf.parse("20/11/2022");
        calendar.add(Calendar.DATE, 5);
        Date date1 = calendar.getTime();
        System.out.println(date.getTime());
        System.out.println(date1.getTime());
        System.out.println(calendar.get(Calendar.YEAR));
        System.out.println((date1.getTime() - date.getTime()) / (1000*60*60*24));
    }
}
