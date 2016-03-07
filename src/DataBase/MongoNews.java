package DataBase;

import Entity.HSNews;
import Entity.News;
import Log.Logs;
import Entity.NewsSubjects;
import Property.Property;
import Utility.tool;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by yiqibai on 12/27/15.
 */
public class MongoNews extends MongoDB {
    private NewsSubjects newsSubjects;


    public MongoNews(Property hsProperty, Logs loger, NewsSubjects subject) {
        super(hsProperty.getString("mongo-ip"), hsProperty.getInt("mongo-port"), loger);
        setDb(hsProperty.getString("mongo-hs-news-DB"));
        newsSubjects = subject;
    }



    @Override
    public void Load() {
        List<News> oldnews = new ArrayList<News>();
        List<Long> monthList = new ArrayList<Long>();

        long endTS = newsSubjects.getTs();
        long beginTS = endTS - 24 * 3 * newsSubjects.getTimerange();

        //check which month to load
        if(tool.getMonthStartTimestamp(beginTS) == tool.getMonthStartTimestamp(endTS)){
            monthList.add(tool.getMonthStartTimestamp(beginTS));
        }
        else{
            monthList.add(tool.getMonthStartTimestamp(beginTS));
            monthList.add(tool.getMonthStartTimestamp(endTS));
        }
        //for each month
        try {
            for(long mday : monthList){
                String colName = "HotNews" + tool.getYear(mday) + tool.getMonth(mday)+"En";

                BasicDBObject query = new BasicDBObject("dateTS", new BasicDBObject("$gt", beginTS));
                BasicDBObject proj = new BasicDBObject("title", 1)
                        .append("newsurl", 1)
                        .append("summary", 1)
                        .append("imageurl", 1);

                DBCursor c = mongo.getDB(getDb()).getCollection(colName).find(query, proj);//###########

                while (c.hasNext()) {
                    BasicDBObject obj = (BasicDBObject) c.next();
                    //shouldn't filter news whose imageurl, newsurl == "" when using hashRemover
//                    if( obj.getString("imageurl").equals("") || obj.getString("newsurl").equals(""))
//                        continue;

                    News n = new HSNews(obj.getString("title"), "", 0, "", obj.getString("imageurl"), obj.getString("summary"),
                            obj.getString("newsurl"), "", 0, "", "", "");

                    oldnews.add(n);
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            loger.info("MongoException [  " + e.getMessage()  +"]");
        }finally {
            newsSubjects.setPastNews(oldnews);
            loger.info("MongoInfo [ LoadPastNews: " + oldnews.size()  +"]");
        }
    }



    public void save() {
        List<DBObject> data = newsSubjects.toDBObj();
        long ts = newsSubjects.getTs();
        try{
            String coll = "HotNews" + tool.getYear(ts) + tool.getMonth(ts) + "En";
            mongo.getDB(getDb()).getCollection(coll).insert(data);
            loger.info("MongoInfo [ SaveNews: " + data.size()  +  "(" + ts + ")]");
        }catch (Exception e){
            System.out.println(e.getMessage());
            loger.info("MongoException [  " + e.getMessage()  +"]");
        }
       finally {
            mongo.close();
        }
    }
}
