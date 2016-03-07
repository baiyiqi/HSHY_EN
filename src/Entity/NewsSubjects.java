package Entity;



import KWPatten.Observers;
import KWPatten.Subjects;
import Log.Logs;
import Utility.tool;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by yiqibai on 12/24/15.
 */
public class NewsSubjects implements Subjects {

    private Queue<Observers> observersQueue = new LinkedList<Observers>();

    private List<News> currentNews = new ArrayList<News>();
    private List<News> pastNews = new ArrayList<News>();
    private long ts = 0;
    private long timerange = 3600;
    Logs loger;


    public NewsSubjects(long ts, long timerange, Logs loger){
        this.ts = ts;
        this.timerange = timerange;
        this.loger = loger;
    }



    //@Override
    public List<News> getCurrentNews() {
        return currentNews;
    }


    //@Override
    public void setCurrentNews(List<News> newslist) {
        currentNews = newslist;
    }



    //@Override
    public List<News> getPastNews() {
        return pastNews;
    }



    //@Override
    public void setPastNews(List<News> newsList) {
        pastNews = newsList;
    }



    /**
     *
     * @param o
     */
    public void addObservers(Object o){
        if(o instanceof Observers && !observersQueue.contains(o)){
            observersQueue.add( (Observers) o);
        }
    }



    /**
     *
     * @param o
     */
    public void removeSubscribe(Object o){
        if(o instanceof Observers && observersQueue.contains(o)){
            observersQueue.remove(o);
        }
    }



    public void notifyObservers(){
        while(observersQueue.size() > 0){
            Observers observer = observersQueue.poll();
            observer.update(this);
        }
    }



    public long getTs() {
        return ts;
    }



    public long getTimerange(){
        return timerange;
    }



    public List<DBObject> toDBObj(){
        List<DBObject> dbObjectList = new ArrayList<DBObject>();
        List<News> sortNews = tool.getAllTopNews(currentNews, currentNews.size());
        for(News n : sortNews){
            if(n.getIsDup()){
                continue;
            }
            if(!tool.isValidNews(n.getNewsTitle(), n.getNewsText())){
                continue;
            }
            DBObject dbObject = new BasicDBObject();
            dbObject.put("title", n.getNewsTitle());
            dbObject.put("newsurl", n.getNewsUrl());
            dbObject.put("src", n.getNewsSrc());
            dbObject.put("category", n.getNewsCate());
            dbObject.put("content", n.getNewsText());
            dbObject.put("dateTS", ts);
            dbObject.put("happentime", n.getNewsTimeReadble());
            dbObject.put("summary", n.getNewsSummary());
            dbObject.put("imageurl", n.getNewsImageUrl());
            dbObject.put("showNews", false);
            dbObject.put("score", n.getRankScore());
            dbObject.put("isTrained", false);
            dbObject.put("isNewMedicine", n.getIsNewMedicine());
            dbObject.put("isNewTreatment", n.getIsNewTreatment());
            dbObject.put("html", n.getNewsHtml());
            dbObject.put("enTitle", n.getEnText());
            dbObject.put("enText", n.getEnText());

            if(n.getNewMedWord() != null && n.getNewMedWord().size() > 0){
                DBObject words = new BasicDBObject();
                for(String word : n.getNewMedWord().keySet()){
                    DBObject wInfo = new BasicDBObject();
                    wInfo.put("cn", "");
                    wInfo.put("flag", false);
                    wInfo.put("sentence", n.getNewMedWord().get(word));
                    words.put(word, wInfo);
                }
                dbObject.put("newMedWord", words);
            }
            dbObjectList.add(dbObject);
        }
        loger.info("最终新闻 " + dbObjectList.size() );
        return dbObjectList;
    }
}
