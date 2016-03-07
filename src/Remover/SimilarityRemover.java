package Remover;

import Entity.News;
import Utility.tool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiqibai on 12/24/15.
 */
public class SimilarityRemover implements Remover {
    static double titleSimilarity = 0.75;
    static double txtSimilarity = 0.75;
    static double sumSimilarity = 0.75;

    static boolean useTilte = true;
    static boolean useText = true;
    static boolean useSummary = true;



    @Override
    public void pastRemover(List<News> currentNews, List<News> pastNews) {
        simiRemover(currentNews, pastNews);
    }



    @Override
    public void realTimeRemover(List<News> currentNews) {
        simiRemover(currentNews, null);
    }



    /**
     * remove news that is similar to current news
     * @param newsList
     * @return
     */
    private void simiRemover(List<News> newsList, List<News> previousNews){
        List<News> data = new ArrayList<News>();

        if(newsList == null || newsList.size() == 0){
            return;
        }
        if(previousNews != null){
            data = previousNews;
        }
        else{
            data.add(newsList.get(0));
        }

        for (int i = 1; i < newsList.size(); i++) {
            News n = newsList.get(i);
            if(n.getIsDup()){
                continue;
            }
            boolean flag = true; //flag == true is nonrepeated news
            for (News en : data) {
                if (n.getNewsTitle().contains(en.getNewsTitle()) || en.getNewsTitle().contains(n.getNewsTitle())) {
                    flag = false;
                    n.setIsDup();
                    break;
                }
                if(!n.getNewsImageUrl().equals("") && !en.getNewsImageUrl().equals("")){
                    if(n.getNewsImageUrl().equals(en.getNewsImageUrl())){
                        flag = false;
                        n.setIsDup();
                        break;
                    }
                }
                if(n.getNewsUrl().equals(en.getNewsUrl())) {
                    flag = false;
                    n.setIsDup();
                    break;
                }
                if (useTilte && tool.StringSimilarity(n.getNewsTitle(), en.getNewsTitle()) >= titleSimilarity) {
                    flag = false;
                    n.setIsDup();
                    break;
                }
                if (useSummary && tool.StringSimilarity(n.getNewsSummary(), en.getNewsSummary()) >= sumSimilarity) {
                    flag = false;
                    n.setIsDup();
                    break;
                }
                if (useText && tool.StringSimilarity(n.getFirstLastSentence(), en.getFirstLastSentence()) >= txtSimilarity) {
                    flag = false;
                    n.setIsDup();
                    break;
                }
            }
            if (flag){
                data.add(n);
            }
        }
    }
}
