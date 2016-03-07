
import Utility.tool;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by yiqibai on 12/28/15.
 */
public class test {
    //static Set<String> wordDict = tool.getWordDict();

    public static void main(String[] args){
//       PropertyFactory propertyFactory = new LocalPropertyFactory();
//
//        Property hsProperty = propertyFactory.createProperty("hs.properties");
//        Logs hsLogger = new Logs("HSKW", propertyFactory.createProperty("log.properties"));
//
//        long beginTS = 1443682800;
//        long timeRange = 3600 * 24;
//
//        while(beginTS < System.currentTimeMillis() / 1000){
//            NewsSubjects newsSubjects = new NewsSubjects(beginTS, timeRange, hsLogger);
//
//            DataBase mySql = new MySql(hsProperty, hsLogger, newsSubjects);
//            mySql.Load();
//
//            Observers hsdetector = new HSDetector( hsLogger);
//            hsdetector.update(newsSubjects);
//            beginTS += timeRange;
//        }

        String txt = "国秃图片 ,qq\t";
        System.out.println(tool.isValidNews(txt, ""));
        double v = 0.0000001;
        System.out.println(Double.parseDouble(new DecimalFormat("##.######").format(v)));


    }



//    private static Set<String> extratNewWords(String txt){
//        Set<String> newMedWords = new HashSet<String>();
//        for(String s : tool.extractEnglish(txt)){
//            String ns = s.toLowerCase();
//            if(!wordDict.contains(ns)){
//                newMedWords.add(s);
//            }
//        }
//        return newMedWords;
//    }



   public static List<String>  getFirstSentence(String text){
        Pattern senPattern = Pattern.compile("([^\\。\\!\\！\\?\\？\"\\“\\”]|([\"\\“][^\"\\“\\”]*[^\\。\\!\\！\\?\\？]*[\"\\“\\”]))*([\\。\\!\\！\\?\\？]+[\\s\\ \\　]*|[\"\\“]+[^\"\\“\\”]*[\\。\\!\\！\\?\\？]+[\"\\“\\”]+[\\s\\ \\　]*|[^\\。\\!\\！\\?\\？\"\\“\\”]*$)");
        List<String> sentence = new ArrayList<String>();
        Matcher matcher = senPattern.matcher(text);
        while (matcher.find()) {
            String raw = matcher.group().replace(" ", "");
            if(!raw.equals("")){
                System.out.println(raw);
                sentence.add(raw);
            }
        }
        return sentence;
   }

    public static Set<String>  getNewMed(String text){
        Pattern urlPatten = Pattern.compile("(http://)*(www.)*[\\S]*(.com) ");
        Matcher matcher = urlPatten.matcher(text);
        String t = matcher.replaceAll("");
        t = text.replaceAll("(http://)*(www.)*[\\S]*(.com||)(\\/\\S*)*", "");


        System.out.println(t);
        Set<String> newMed = new HashSet<String>();
        try {
            Pattern p = Pattern.compile("[A-Za-z]+(\\-)*[A-Za-z]+|[A-Za-z]+");
            Matcher m = p.matcher(t);
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




}
