package Utility; /**
 * Created with IntelliJ IDEA.
 * User: BYQ
 * Date: 2/27/14
 * Time: 9:47 PM
 * To change this template use File | Settings | File Templates.
 */


import Entity.News;
import Log.Logs;
import com.huaban.analysis.jieba.JiebaSegmenter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class tool {


    /**
     * get month timestamp
     * @param ts
     * @return
     */
    public static long getMonthStartTimestamp(long ts) {
        Date date = new Date();
        date.setTime(ts*1000);
        TimeZone timeZoneUTC = TimeZone.getTimeZone("GMT");
        Calendar cal = Calendar.getInstance(timeZoneUTC);
        cal.setTime(date);
        //int month = cal.get(Calendar.MONTH) + 1;
        //cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY,  0);
        cal.set(Calendar.MINUTE,       0);
        cal.set(Calendar.SECOND,       0);
        cal.set(Calendar.MILLISECOND,  0);

        return cal.getTimeInMillis()/1000 ;
    }



    /**
     * Given a timestamp, return the start timestamp of the week  UTC
     * @param ts    The give timestamp
     * @return      The start timestamp of the week
     */
    public static long getWeekStartTimestamp(long ts) {

        Date date = new Date();
        date.setTime(ts*1000);
        TimeZone timeZoneUTC = TimeZone.getTimeZone("GMT");
        Calendar cal = Calendar.getInstance(timeZoneUTC);
        cal.setTime(date);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
        if(day_of_week == 1)
            day_of_week = 6;
        else
            day_of_week -= 2;

        cal.set(Calendar.HOUR_OF_DAY,  0);
        cal.set(Calendar.MINUTE,       0);
        cal.set(Calendar.SECOND,       0);
        cal.set(Calendar.MILLISECOND,  0);

        return cal.getTimeInMillis()/1000 - 3600*24*day_of_week;
    }



    /**
     * Given a timestamp, return the start timestamp of the day  UTC
     * @param ts    The give timestamp
     * @return      The start timestamp of the day
     */
    public static long getDayStartTimestamp(long ts) {
        Date date = new Date();
        date.setTime(ts*1000);
        TimeZone timeZoneUTC = TimeZone.getTimeZone("GMT");
        Calendar cal = Calendar.getInstance(timeZoneUTC);
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY,  0);
        cal.set(Calendar.MINUTE,       0);
        cal.set(Calendar.SECOND,       0);
        cal.set(Calendar.MILLISECOND,  0);

        return cal.getTimeInMillis()/1000;
    }



    //ts = 1412431314, return 1412431200
    public static long getHourStartTimeStamp(long ts){
        long rem = ts % 3600;
        return ts- rem;
    }



    /**
     * Given a timestamp, return the start timestamp of the day  UTC
     * @param ts    The give timestamp
     * @return      The year number like: 2014
     */
    public static String getYear(long ts) {
        Date date = new Date();
        date.setTime(ts*1000);
        TimeZone timeZoneUTC = TimeZone.getTimeZone("GMT");
        Calendar cal = Calendar.getInstance(timeZoneUTC);
        cal.setTime(date);

        return String.valueOf(cal.get(Calendar.YEAR));
    }



    /**
     * Given a timestamp, return the start timestamp of the day  UTC
     * @param ts    The give timestamp
     * @return      The month number like: 07,08,09,10,11,12,01,02...
     */
    public static String getMonth(long ts) {
        Date date = new Date();
        date.setTime(ts*1000);
        TimeZone timeZoneUTC = TimeZone.getTimeZone("GMT");
        Calendar cal = Calendar.getInstance(timeZoneUTC);
        cal.setTime(date);

        int month = cal.get(Calendar.MONTH) + 1;
        if(month < 10)
            return "0"+String.valueOf(month);
        else
            return String.valueOf(month);
    }



    public static String getHour(long ts) {
        Date date = new Date();
        date.setTime(ts*1000);
        TimeZone timeZoneUTC = TimeZone.getTimeZone("GMT");
        Calendar cal = Calendar.getInstance(timeZoneUTC);
        cal.setTime(date);

        return String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
    }



    /**
     * get top n news
     * @param from
     * @param n
     * @return  top n news (Assending)
     */
    public static Map<String, Double> Top(
            Map<String, Double> from,  final int n) {

        ArrayList<Map.Entry<String, Double>> sortedMap = new ArrayList<Map.Entry<String,Double>>(from.entrySet());
        Collections.sort(sortedMap, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                if (o1.getValue() < o2.getValue())
                    return 1;
                else if(o1.getValue() > o2.getValue())
                    return -1;
                else
                    return 0;
            }
        });

        Map<String, Double> ret = new LinkedHashMap<String, Double>();

        if(sortedMap.size() > n){
            for (Map.Entry<String, Double> e : sortedMap) {
                String topuser = e.getKey();
                Double cnt = e.getValue();
                ret.put(topuser, cnt);
                if (ret.size() >= n)
                    break;
            }
        }
        else {
            for (Map.Entry<String, Double> e : sortedMap) {
                String topuser = e.getKey();
                Double cnt = e.getValue();
                ret.put(topuser, cnt);
            }
        }

        return ret;

    }



    //get top score News
    public static List<News> getAllTopNews(List<News> newsList,  int num){
        List<News> topNews = new ArrayList<News>();

        if(newsList.size() > 0){
            Map<String, News> nameToNews = new HashMap<String, News>();
            Map<String, Double> nameToScore = new HashMap<String, Double>();
            for(News n : newsList){
                nameToNews.put(n.getNewsTitle(), n);
                nameToScore.put(n.getNewsTitle(), n.getRankScore());
            }
            Map<String, Double> top = Top(nameToScore, num);
            for(String t : top.keySet()){
                if(nameToNews.containsKey(t))
                    topNews.add(nameToNews.get(t));
            }

        }
        return topNews;
    }



    /**
     * calculate similarity between two text, text should be tokenized
     * @param text1
     * @param text2
     * @return
     */
    public static double getSimilarity(List<String> text1, List<String> text2) {
        HashMap<String, Integer[]> wordMap = new HashMap<String, Integer[]>();
        //HashMap<String, Integer[]> wordMap2 = new HashMap<String, Integer>();

        for (String word : text1) {
            if (!wordMap.containsKey(word)) {
                wordMap.put(word, new Integer[2]);
                wordMap.get(word)[0] = new Integer(0);
                wordMap.get(word)[1] = new Integer(0);
            }
            wordMap.get(word)[0] += 1;
        }

        for (String word : text2) {
            if (!wordMap.containsKey(word)) {
                wordMap.put(word, new Integer[2]);
                wordMap.get(word)[0] = new Integer(0);
                wordMap.get(word)[1] = new Integer(0);
            }
            wordMap.get(word)[1] += 1;
        }

        double result = 0;
        int dotProduct = 0;
        int eNorm1 = 0;
        int eNorm2 = 0;
        int magnitude = 0;

        for (String word : wordMap.keySet()) {
            dotProduct += wordMap.get(word)[0] * wordMap.get(word)[1];
            eNorm1 += Math.pow(wordMap.get(word)[0], 2);
            eNorm2 += Math.pow(wordMap.get(word)[1], 2);
        }
        if (eNorm1 != 0 && eNorm2 != 0) {
            result = dotProduct / Math.sqrt(eNorm1 * eNorm2);
        }

        return result;

    }



    public static double StringSimilarity(String s1, String s2){
        List<String> slst1 = segmentText(s1);
        List<String> slst2 = segmentText(s2);

        return getSimilarity(slst1, slst2 );
    }



    //convert ts to readable time
    public static String convertTS(long ts) {
        Date date = new Date();
        date.setTime(ts*1000);
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(date);
    }



    //check whether the url contain http:
    public static boolean isValidURL(String url){
        String newURL = url.trim();
        Pattern p = Pattern.compile("^http:");
        Matcher m = p.matcher(newURL);
        return m.find();
    }



    /**
     * check time is timestamp, not readable time
     * @param ts
     * @return true:timestamp, false:readable time
     */
    public static boolean isValidTime(long ts){
        String time = String.valueOf(ts);
        Pattern p = Pattern.compile("^1");
        Matcher m = p.matcher(time);
        return m.find();
    }



    /**
     * check title does not contain "<a..."
     * @param title
     * @return true: clean,doesn't contain, false:contain
     */
    public static boolean isValidTitle(String title){
        Pattern p = Pattern.compile("^<a.*");
        Matcher m = p.matcher(title.trim());

        return !m.matches();
    }



    public static boolean isValidImageUrl(String url)  {
        if (!isValidURL(url) || url.equals("")){
            return false;
        }
        int code = 404;
        try {
            URL u  = new URL( url);
            HttpURLConnection huc =  ( HttpURLConnection )  u.openConnection ();
            huc.setReadTimeout(2000);
            huc.setConnectTimeout(3000);
            huc.setRequestMethod ("GET");  //OR  huc.setRequestMethod ("HEAD");
            huc.connect () ;
            code = huc.getResponseCode() ;

        } catch (MalformedURLException e) {
            //e.printStackTrace();
        } catch (ProtocolException e) {
            //e.printStackTrace();
        } catch (IOException e) {
           // e.printStackTrace();
        }
        if(code == 404)
            return false;
        else
            return true;
    }



    public static String resetTitle(String title){
        String timepatten = "(\\d{2}月\\d{2}日) (\\d{2}:\\d{2})";
        String wordpatten = "[（图）|(图)|(组图)|/图|\\[图\\]|\\[组图\\]|(高清组图)|图片来源：|图片说明：]";
        String timepatten1 = "(\\d{2}月\\d{2}日)[ ]*(\\d{2}:\\d{2})";
        String tp1 = "(\\d{2}月\\d{2}日)";
        String tp2 = "(\\d{2}:\\d{2})";
        return title.replaceAll(tp1,"").replaceAll(wordpatten, "").replaceAll(tp2,"").trim();

    }



    //segment
    public static List<String> segmentText(String content){
        //return the words whose length>1
        List<String> txtArray = JiebaTokenizer.getInstance().process(getReplacement(content),
                JiebaSegmenter.SegMode.SEARCH);
        return txtArray;
    }



    //remove special charactor
    public static String getReplacement( String text){

        String stxt = text.replaceAll("[\\(（]分享自[^\\)）]*[\\)）]|<a.*</a>0123456789", "");
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].-<>/?~！@#￥%……&*-——（）——+|{}【】‘；：”“’.。，、？@{a-z|A-Z|0-9}*《》http{a-z|A-Z|0-9}*]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(stxt);
        return   m.replaceAll("").trim();
    }



    public static List<String> getSentence(String paraText){
        List<String> ret = new ArrayList<String>();

        Pattern senPattern = Pattern.compile("([^\\。\\!\\！\\?\\？\"\\“\\”]|([\"\\“][^\"\\“\\”]*[^\\。\\!\\！\\?\\？]*[\"\\“\\”]))*([\\。\\!\\！\\?\\？]+[\\s\\ \\　]*|[\"\\“]+[^\"\\“\\”]*[\\。\\!\\！\\?\\？]+[\"\\“\\”]+[\\s\\ \\　]*|[^\\。\\!\\！\\?\\？\"\\“\\”]*$)");

        //建立List，存放本段句子的原始文本
        ArrayList<String> senRaws = new ArrayList<String>();
        //根据正则，获取段落中的句子，存入senRaws数组
        //String tex = new String(paraText);
        //tex = tex.replace("\n", "").trim();
        //tex = tex.replace("\r","").trim();
        //tex = tex.replace(" ", "").trim();

        Matcher matcher = senPattern.matcher(paraText.replace("\n", "").replace("\r","").replace(" ", "").trim());
        while (matcher.find()) {
            String raw = matcher.group().replace(" ", "");
            if(!raw.equals(""))
                senRaws.add(raw);
        }

        if(senRaws.size() > 0){
            ret.add(senRaws.get(0));
            ret.add(senRaws.get(senRaws.size()-1));
        }
        //System.out.println(ret);
        return ret;
    }



    public static boolean checkSimi(String tex1, String tex2, double textSimi){
        List<String> lst1 = getSentence(tex1);
        List<String> lst2 = getSentence(tex2);
        String sent1 = "";
        String sent2 = "";

        if(lst1.size() > 0)
            sent1 = lst1.get(0) + lst1.get(1);
        if(lst2.size() > 0)
            sent2 = lst2.get(0) + lst2.get(1);

        if(sent1.equals("") && sent2.equals("")){
            if(StringSimilarity(tex1, tex2) > textSimi)
                return true;
        }

        if(sent1.equals(sent2) || sent1.contains(sent2) || sent2.contains(sent1))
            return true;

        return false;
    }



    public static String sentence(String text){
        Pattern firstSenPattern = Pattern.compile("^[^\\。\\!\\！\\?\\？\"\\“\\”]*[\\。\\!\\！\\?\\？\"\\“\\”]");
        Pattern lastSenPattern = Pattern.compile("[^\\。\\!\\！\\?\\？\"\\“\\”]*[\\。\\!\\！\\?\\？\"\\“\\”]{0,1}$");

        //要匹配的文本
        //String text = "这是第一句，第一句。第二句在这里。这是最后一句!";
        //第一句话与最后一句话的matcher
        Matcher firstSenMatcher = firstSenPattern.matcher(text.replace(" ", "").replace("\n", "").replace("\r","").trim());
        Matcher lastSenMatcher = lastSenPattern.matcher(text.replace(" ", "").replace("\n", "").replace("\r","").trim());

        //第一句话与最后一句话
        String firstSen = "";
        String lastSen = "";

        //匹配第一句话
        if (firstSenMatcher.find()) {
            firstSen = firstSenMatcher.group().replace(" ", "");
        }
        //匹配最后一句话
        if (lastSenMatcher.find()) {
            lastSen = lastSenMatcher.group().replace(" ", "");
        }

        return firstSen + lastSen;
    }



    public static boolean isSameText(String text1, String text2, double textSimi){
        String sent1 = sentence(text1);
        String sent2 = sentence(text2);

        if(sent1.equals("") && sent2.equals("")){
            if(tool.StringSimilarity(text1, text2) > textSimi)
                return true;
        }

        if(sent1.equals(sent2) || sent1.contains(sent2) || sent2.contains(sent1))
            return true;

        return false;
    }



    public static Set<String>  extractEnglish(String txt){
        Set<String> newMed = new HashSet<String>();
        try {
            String cleanTxt = txt.replaceAll("(http://)*(www.)*[\\S]*(.com)(\\/\\S*)*", "");

            //String patten = "[\\w]+(\\-)*[\\w]+|[\\w]+";
            Pattern p = Pattern.compile("[A-Za-z]+(\\-)*[A-Za-z]+|[A-Za-z]+");
            Matcher m = p.matcher(cleanTxt);
            while(m.find()){
                if(m.group().length() > 1){
                    newMed.add(m.group().trim());
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            return newMed;
        }
    }




    public static Set<String> getWordDict(Logs hsLogger){
        Set<String> wordDict = new HashSet<String>();
        Scanner sc = null;
        try {
            FileInputStream inputStream = new FileInputStream("./dict");
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String word = sc.nextLine().replace("\r\n", "").replace("\n", "").trim();
                wordDict.add(word.toLowerCase());
            }
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
        hsLogger.info("FilterDict Info [ Load Filter Words " + wordDict.size() + "]");
        return wordDict;
    }



    public static List<String> getSegText(String text){
        List<String> sentence = new ArrayList<String>();
        try{
            Pattern senPattern = Pattern.compile("([^\\。\\!\\！\\?\\？\"\\“\\”]|([\"\\“][^\"\\“\\”]*[^\\。\\!\\！\\?\\？]*[\"\\“\\”]))*([\\。\\!\\！\\?\\？]+[\\s\\ \\　]*|[\"\\“]+[^\"\\“\\”]*[\\。\\!\\！\\?\\？]+[\"\\“\\”]+[\\s\\ \\　]*|[^\\。\\!\\！\\?\\？\"\\“\\”]*$)");
            Matcher matcher = senPattern.matcher(text);
            while (matcher.find()) {
                String raw = matcher.group().replace(" ", "");
                if(!raw.equals("")){
                    //System.out.println(raw);
                    sentence.add(raw);
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
       finally {
            return sentence;
        }
    }


    public static boolean isValidNews(String title, String text){
        if(title.contains("枪") || text.contains("枪") || title.contains("qq")){
            return false;
        }
        return true;
    }


    public static void printNewsTitle(List<News> newsList){
        for(News n : newsList){
            System.out.println(n.getNewsTitle());
        }
        System.out.println();

    }
}
