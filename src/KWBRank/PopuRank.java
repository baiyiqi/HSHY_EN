package KWBRank;

import Entity.News;
import KWPatten.ModuleProcessor;
import Entity.NewsSubjects;
import KWPatten.Observers;
import Log.Logs;
import Utility.tool;

import java.util.*;


/**
 * Created by yiqibai on 12/26/15.
 * Count word frequency, popuRank
 */
public class PopuRank implements Observers, ModuleProcessor {
    PopuRankWord popuRankWord;
    List<News> currentNews;
    long ts;

    static final float ALPHA = 0.5f;
    static final float BETA = 1 - ALPHA;
    static final double Theta = 0.1;
    Logs logs;


    public PopuRank(PopuRankWord popuRankWord, Logs logs){
        this.popuRankWord = popuRankWord;
        this.logs = logs;

    }



    @Override
    public void update(NewsSubjects newsSubjects) {
        currentNews = newsSubjects.getCurrentNews();
        ts = newsSubjects.getTs();

        preProcess();
        coreProcess();
        finalProcess();
        logs.info("Popurank updated");
    }




    public void preProcess(){
        Map<String, Double[]> popuRank = getPopuRank(getTextSegArraies());
        popuRankWord.setPopuRank(popuRank);
        updatePopuRankTable(ts, popuRankWord.getPopuRankTable() ,popuRank);
    }


    public void coreProcess(){
        Map<String, Double> map = getSuddenWords(popuRankWord.getPopuRankTable());
        PopuRankWord.setSudWords(map);
        updateNewsPopuRank(map);
    }


    public void finalProcess(){
        keepRange(3600 * 24, 3, popuRankWord.getPopuRankTable());
    }





    /**
     * get all news content in from of 2-demension arrayList
     * @return
     */
    private  List<List<String>> getTextSegArraies(){
        List<List<String>> contentList = new ArrayList<List<String>>();
        if(currentNews != null && currentNews.size() > 0){
            for(News n : currentNews){
                if(!n.getIsDup()){
                    contentList.add(n.getTextSegArray());
                }
            }
        }
        return contentList;
    }



    /**
     * all the info of each word in form of array
     * @param allnewsArray
     * @return {word : [df,  tf in all file,  popurank]}
     */
    private Map<String, Double[]> getPopuRank(List<List<String>> allnewsArray){
        Map<String, Double[]> wordTFIDF = new HashMap<String, Double[]>();

        if(allnewsArray.size() > 0){
            double numNews = allnewsArray.size();
            for(List<String> lst : allnewsArray){
                Set<String> wordset = new HashSet<String>();
                for(String s : lst){
                    if(!wordTFIDF.containsKey(s)){
                        // 0:df, 1:tf, 2: popurank
                        Double[] DF_TF = {0.0, 0.0, 0.0};
                        wordTFIDF.put(s, DF_TF);
                    }
                    if(wordset.add(s)){
                        wordTFIDF.get(s)[0] += 1 / numNews;
                        wordTFIDF.get(s)[1] += ( double ) Collections.frequency(lst, s) / lst.size();
//                        tfdf.get(s)[2] += 1; //包含词的新闻数，【0】的倒数
                    }
                }
            }
        }
        for(String s : wordTFIDF.keySet()){
            wordTFIDF.get(s)[2] =  wordTFIDF.get(s)[0] * ALPHA + wordTFIDF.get(s)[1] * BETA;
        }

        return wordTFIDF;
    }



    private void updatePopuRankTable(long ts, Map<String, Map<Long, Double>> popuRankTable , Map<String, Double[]> popuRank){
        if(popuRank.size() > 0){
            for(String word : popuRank.keySet()) {
                //time:hotdegree
                if (!popuRankTable.containsKey(word)) {
                    Map<Long, Double> timeTofreq = new Hashtable<Long, Double>();
                    timeTofreq.put(ts, popuRank.get(word)[2]);
                    popuRankTable.put(word, timeTofreq);
                } else if (!popuRankTable.get(word).containsKey(ts) || popuRankTable.get(word).get(ts) == null)
                    popuRankTable.get(word).put(ts, popuRank.get(word)[2]);
            }
        }
    }



    private  Map<String, Double> getSuddenWords(Map<String, Map<Long, Double>> popuRankTable ){
        Map<String, Double> hotwordmap = new HashMap<String, Double>();

        int loop;
        if(popuRankTable.size() > 0){
            for(String word : popuRankTable.keySet()){
                if(popuRankTable.get(word) != null){
                    if (popuRankTable.get(word).get(ts) != null ){
                        //get entries in order it added
                        List<Map.Entry<Long,Double>> entryList =
                                new ArrayList<Map.Entry<Long, Double>>(popuRankTable.get(word).entrySet());
                        loop = entryList.size();
                        if(loop > 1){
                            ///check words which sudden Hot
                            Double currentHotDegree = popuRankTable.get(word).get(ts);
                            float avg1 = 0;
                            for (int i = 0 ; i < loop -1 ; i++ ){
                                avg1 += entryList.get( i ).getValue().floatValue();
                            }
                            avg1 = avg1 / ( loop - 1);
                            if(avg1 > 0){
                                if (currentHotDegree > Theta * avg1){
                                    hotwordmap.put(word, currentHotDegree);
                                }
                            }
                        }
                        continue;
                    }
                }
            }
        }
        return hotwordmap;
    }



    private void keepRange(long timerange, int tableMaxLen, Map<String, Map<Long, Double>> popuRankTable ){
        long oldTime = tool.getDayStartTimestamp(ts) - tableMaxLen * timerange ;
        for(String s : popuRankTable.keySet()){
            List<Long> timeSet = new ArrayList<Long>();
            for(long t : popuRankTable.get(s).keySet()){
                if(t <= oldTime)
                    timeSet.add(t);
            }
            for(long t : timeSet){
                popuRankTable.get(s).remove(t);
            }
        }
    }



    private void updateNewsPopuRank(Map<String, Double> suddenword){
        for(News n : currentNews){
            for(String s : suddenword.keySet()){
                if(n.getNewsText().contains(s)){
                    n.addRankScore(suddenword.get(s));
                }
            }
        }
    }


}
