import Classifier.HSClassifier;
import DataBase.DataBase;
import Entity.CateKWord;
import Entity.HSKeywords;
import Entity.News;
import Entity.NewsSubjects;
import HSDetector.HSDetector;
import HSRank.HSProcessor;
import KWBRank.PopuRank;
import KWBRank.PopuRankWord;
import KWPatten.Observers;
import Log.Logs;
import Property.LocalPropertyFactory;
import Property.Property;
import Property.PropertyFactory;
import Remover.DupRemover;
import Remover.HashRemover;
import Utility.tool;

import java.util.ArrayList;
import java.util.Set;
import DataBase.*;
/**
 * Created by yiqibai on 1/10/16.
 */
public class testRemover {



    PropertyFactory propertyFactory = new LocalPropertyFactory();
    //PropertyFactory propertyFactory = new RemotePropertyFactory();
    Property hsProperty = propertyFactory.createProperty("hs.properties");
    Logs hsLogger = new Logs("HSKW", propertyFactory.createProperty("log.properties"));

    long beginTS = hsProperty.getLong("beginTS");
    long timeRange = hsProperty.getLong("updateFreqency");

    public static void main(String[] args) {
        testRemover mainObj = new testRemover();
        mainObj.run();
    }


    public void run() {

        while (true) {
            long nowTS = System.currentTimeMillis() / 1000;
            if(nowTS - beginTS > timeRange){
                //load data

                long processTS = beginTS - 2 * timeRange;
                NewsSubjects newsSubjects = new NewsSubjects(processTS, timeRange, hsLogger);

                DataBase mySql = new MySql(hsProperty, hsLogger, newsSubjects);
                mySql.Load();

                if(newsSubjects.getCurrentNews().size() > 0){
                    hsLogger.info(" ---------------------- " + beginTS + "  ------------------------");
                    //Set<String> wordDict = tool.getWordDict(hsLogger);
                    //hsLogger.info("HSInfo [Load Word Dictionary " + wordDict.size() + "]");

//                    //load past news
                   DataBase mongoNews = new MongoNews(hsProperty, hsLogger, newsSubjects);
                   mongoNews.Load();
//
//                    //setup remover
                    Observers remover = new DupRemover(new HashRemover(), hsLogger);
                    newsSubjects.addObservers(remover);



                    //invoke all processors
                    newsSubjects.notifyObservers();

                    //save data
                    mongoNews.save();
                    //mongoKWord.save();
                    // mongoKWord.Close();
                    mongoNews.Close();

                    System.out.println();
                    newsSubjects.setCurrentNews(new ArrayList<News>());
                }
            }
            else{
                try {
                    Thread.sleep((Math.abs(nowTS - beginTS)) * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {

                }
            }
            beginTS += timeRange;
            System.gc();
        }
    }



}
