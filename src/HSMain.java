import DataBase.*;
import Entity.CateKWord;
import Entity.HSKeywords;
import Entity.News;
import HSRank.HSProcessor;
import KWBRank.PopuRank;
import KWBRank.PopuRankWord;
import Entity.NewsSubjects;
import KWPatten.Observers;
import Log.Logs;
import Property.LocalPropertyFactory;
import Property.RemotePropertyFactory;
import Property.Property;
import Property.PropertyFactory;
import Remover.HashRemover;
import HSDetector.HSDetector;
import Classifier.*;
import Remover.DupRemover;
import Remover.SimilarityRemover;
import Utility.tool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by yiqibai on 12/27/15.
 */



public class HSMain implements Runnable {
   // PropertyFactory propertyFactory = new LocalPropertyFactory();
    PropertyFactory propertyFactory = new RemotePropertyFactory();
    Property hsProperty = propertyFactory.createProperty("hs.properties");
    Logs hsLogger = new Logs("HSKW", propertyFactory.createProperty("log.properties"));

    long beginTS = hsProperty.getLong("beginTS");
    long timeRange = hsProperty.getLong("updateFreqency");


    public static void main(String[] args) {
        HSMain mainObj = new HSMain();
        mainObj.run();
    }


    public void run() {

        while (true) {
            long nowTS = System.currentTimeMillis() / 1000;
            if(nowTS - beginTS > timeRange){
                hsLogger.info(" ---------------------- " + beginTS + "  ------------------------");
                long processTS = beginTS - 2 * timeRange;
                NewsSubjects newsSubjects = new NewsSubjects(processTS, timeRange, hsLogger);
                HSKeywords hsKeywords = new HSKeywords();
                CateKWord cateKWord = new CateKWord();

                DataBase mySql = new MySql(hsProperty, hsLogger, newsSubjects);
                mySql.Load();

                if(newsSubjects.getCurrentNews().size() > 0){
                    Set<String> wordDict = tool.getWordDict(hsLogger);
                    hsLogger.info("HSInfo [Load Word Dictionary " + wordDict.size() + "]");

                    //load past news
                    DataBase mongoNews = new MongoNews(hsProperty, hsLogger, newsSubjects);
                    mongoNews.Load();

                    //load keywords
                    DataBase mongoKWord = new MongoKWord(hsProperty, hsLogger, hsKeywords);
                    mongoKWord.Load();

                    //load category words
                    DataBase mongoCate = new MongoCategory(hsProperty, hsLogger, cateKWord);
                    mongoCate.Load();

                    //setup remover
                    Observers remover = new DupRemover(new HashRemover(), hsLogger);
                    newsSubjects.addObservers(remover);

                    //setup classifier
                    Observers classifier = new HSClassifier(cateKWord,  hsLogger); //classification should be prior to HSProcess
                    newsSubjects.addObservers(classifier);

                    //setup popuRank
                    Observers popuRank = new PopuRank(new PopuRankWord(),  hsLogger);
                    newsSubjects.addObservers(popuRank);

                    //setup hsRank
                    Observers hsrank = new HSProcessor(hsKeywords, hsLogger);
                    newsSubjects.addObservers(hsrank);

                    //setup hsDetector
                    Observers hsdetector = new HSDetector(hsLogger, wordDict, hsKeywords.getHSKeyWords());
                    newsSubjects.addObservers(hsdetector);

                    //invoke all processors
                    newsSubjects.notifyObservers();

                    //save data
                    mongoNews.save();
                    mongoKWord.save();
                    mongoKWord.Close();
                    mongoNews.Close();

                    newsSubjects.setCurrentNews(new ArrayList<News>());
                }
                beginTS += timeRange;
                System.gc();
            }
            else{
                try {
                    Thread.sleep( 600 );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    System.gc();
                }
            }

        }
    }









}
