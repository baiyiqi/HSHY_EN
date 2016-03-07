package Classifier;

import Entity.CateKWord;
import Entity.News;
import KWPatten.ModuleProcessor;
import Entity.NewsSubjects;
import KWPatten.Observers;
import Log.Logs;

import java.util.*;

/**
 * Created by yiqibai on 12/28/15.
 */
public class HSClassifier implements Observers, ModuleProcessor {
    List<News> newsList;
    Map<String, Map<String, Double>> category;
    Logs loger;

    public HSClassifier(CateKWord cateKWord, Logs logs){
        category = cateKWord.getCateKWord();
        loger = logs;
    }


    @Override
    public void update(NewsSubjects subjects) {
        newsList = subjects.getCurrentNews();
        classifyNews();
        loger.info(" classifier updated");
    }



    @Override
    public void preProcess() {

    }



    @Override
    public void coreProcess() {

    }

    @Override
    public void finalProcess() {

    }



    public void classifyNews(){
        if(newsList != null && category != null && newsList.size() > 0 && category.size() > 0 ){
            for(News n : newsList){
                if( !n.getIsDup() && n.getNewsCate().equals("")){
                    List<String> newsWord = new ArrayList<String>(n.getTextSegArray());
                    n.setNewsCate(wordMatch(newsWord, new HashMap<String, Map<String, Double>>(category)));
                }
            }
        }
    }



    private String wordMatch(List<String> newsword, Map<String, Map<String, Double>> category){
        int score = 0;
        String cate = "";

        if(newsword.size() > 0){
            for(String c : category.keySet()){
                Set<String> cateword = category.get(c).keySet();
                Set<String> newsword1 = new HashSet<String>(newsword);
                newsword1.retainAll(cateword);

                if(newsword1.size() > 0){
                    if(newsword1.size() > score){
                        score = newsword1.size();
                        cate = c;
                    }
                }
            }
        }
        return cate;
    }
}
