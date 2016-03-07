package HSRank;

import Entity.HSKeywords;
import Entity.News;
import KWPatten.ModuleProcessor;
import Entity.NewsSubjects;
import KWPatten.Observers;
import Log.Logs;

import java.util.*;


/**
 * Created by yiqibai on 12/27/15.
 */
public class HSProcessor implements Observers, ModuleProcessor {

    private List<News> currentNews;
    private Map<String, Map<String, double[]>> categoryKeywords;
    //[category: 0: document, 1:words num]
    private Map<String, double[]> docWordsNum ;
    Logs logs;


    public HSProcessor(HSKeywords hsKeywords, Logs logs){
        categoryKeywords = hsKeywords.getCategoryKeywords();
        docWordsNum = hsKeywords.getDocWordsNum();
        this.logs = logs;
    }



    @Override
    public void update(NewsSubjects NewsSubjects){
        if(NewsSubjects != null){
            currentNews = NewsSubjects.getCurrentNews();
        }

        preProcess();
        coreProcess();
        finalProcess();
        logs.info("HSProcessor updated");
    }



    public void preProcess(){
        setNewsKeyWord(getKeyWordSet());
    }



    public void coreProcess(){
        hsAlgorithm();
    }



    public void finalProcess(){
        hsTFIDF();
        setNewsHSRank();
    }



    /**
     * get all keywords
     */
    private Set<String> getKeyWordSet(){
        Set<String> kwSet = new HashSet<String>();
        for(String cate : categoryKeywords.keySet()){
            kwSet.addAll(categoryKeywords.get(cate).keySet());
        }
        return kwSet;
    }



    /**
     * update news keyword frequent
     */
    public void setNewsKeyWord(Set<String> keywordset){
        if(keywordset.size() == 0)
            return;
        for(News n : currentNews){
            Map<String, Integer > keywordNum = new HashMap<String, Integer>();
            for(String w : keywordset){
                keywordNum.put(w, Collections.frequency(n.getTextSegArray(), w));
            }
            n.setKeyWordMap(keywordNum);
        }
    }



    /**
     * calculate TF and DF for HS KeyWords
     * @return {category : {word : [word总频率,  包含word的新闻数]}}
     */
    private void hsAlgorithm(){
        for(News n : currentNews){
            int nwordnum = n.getTextSegArray().size();

            String cate = n.getNewsCate();
            if(cate.equals("")){
                continue;
            }
            if(categoryKeywords.containsKey(cate)){
                for(String kword : categoryKeywords.get(cate).keySet()) {
                    //tf
                    categoryKeywords.get(cate).get(kword)[0] = (n.getKeyWordNum(kword) + categoryKeywords.get(cate).get(kword)[0] * docWordsNum.get(cate)[1] ) /  (docWordsNum.get(cate)[1] + nwordnum) ;
                    //df
                    if(n.getNewsText().contains(kword))
                        categoryKeywords.get(cate).get(kword)[1] =  (categoryKeywords.get(cate).get(kword)[1] * docWordsNum.get(cate)[0] + 1 ) / (docWordsNum.get(cate)[0] + 1) ;
                    else
                        categoryKeywords.get(cate).get(kword)[1] =  (categoryKeywords.get(cate).get(kword)[1] * docWordsNum.get(cate)[0] ) / (docWordsNum.get(cate)[0] + 1) ;
                }
            }

            if(docWordsNum.containsKey(cate)){
                double[] count = new double[2];
                count[0] = docWordsNum.get(cate)[0] + 1;
                count[1] = docWordsNum.get(cate)[1] + nwordnum ;
                //update 各个分类下文章数目和文字书目
                docWordsNum.put(cate, count );
            }
        }
    }


    /**
     * calculate TFIDF for HS keywords
     */
    private void hsTFIDF(){
        for(String cate : categoryKeywords.keySet()){
            for(String word : categoryKeywords.get(cate).keySet()){
                //df * tf
                if( categoryKeywords.get(cate).get(word)[1] > 0)
                    categoryKeywords.get(cate).get(word)[2] = -1 * categoryKeywords.get(cate).get(word)[0] *  Math.log( categoryKeywords.get(cate).get(word)[1]);
            }
        }
    }


    /**
     * update news rank according to HS keywords TFIDF
     */
    private void setNewsHSRank(){
        for(News n : currentNews){
            String cate = n.getNewsCate();
            if(cate.equals(""))
                continue;
            double score = 0.0;
            for(String word : n.getKeyWordMap().keySet()){
                if(categoryKeywords.containsKey(cate)){
                    if(categoryKeywords.get(cate).containsKey(word)){
                        if(n.getKeyWordNum(word) != 0)
                            score += n.getKeyWordNum(word) * categoryKeywords.get(cate).get(word)[2];
                    }
                }
            }
            n.setRankScore(score);
        }
    }








}
