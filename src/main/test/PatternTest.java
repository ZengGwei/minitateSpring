import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/18
 */
public class PatternTest {

    public static void main(String[] args) {
        String line = " <p>今天天气真好！！￥{user}带着阿猫阿狗出去春游！！！</p>";
        Matcher m = PatternTest.matcher(line);
        while (m.find()) {
            for (int i = 1; i <= m.groupCount(); i++) {

                //要把￥{}中间的这个字符串给取出来
                String paramName = m.group(i);
                line = line.replaceAll("￥\\{" + paramName + "\\}", "Tom");
                System.out.println(line);


            }
        }

    }

    private static Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("￥\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }
}


