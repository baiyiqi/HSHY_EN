package Remover;

import Entity.News;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yiqibai on 12/24/15.
 */
public class HashRemover implements Remover {
    private static Set<String> titleSet = new HashSet<String>();
    private static Set<String> imageurlset = new HashSet<String>();
    private static Set<String> newsurlset = new HashSet<String>();
    private static Set<String> sentenceSet = new HashSet<String>();



//    public HashRemover(){
//        Initiate();
//    }


    @Override
    public void pastRemover(List<News> currentNews, List<News> pastNews) {
        if(loadIndex(pastNews)){
            hashRemover(currentNews);
        }
    }



    @Override
    public void realTimeRemover(List<News> currentNews) {
        Initiate();
        hashRemover(currentNews);
    }



    /**
     * filter same image url and news url
     * @param newsList
     * @return
     */
    private void hashRemover(List<News> newsList){
        int count = 0;
        if(newsList != null && newsList.size() > 0){
            for(News n : newsList){
                if(!n.getIsDup() && !isNotRepeatNews(n.getNewsImageUrl(), n.getNewsUrl(), n.getFirstLastSentence(), n.getNewsTitle())){
                    n.setIsDup();
//                    System.out.println(" DUP " + n.getNewsTitle());
                    count ++;
                }
            }
        }
        System.out.println("重复新闻 " + count);
    }



    /**
     *
     * @param imageurl
     * @param newsurl
     * @param sentence
     * @param title
     * @return True if is not a repeated news
     */
    private boolean isNotRepeatNews(String imageurl, String newsurl, String sentence, String title){
        if(!newsurl.equals("") && !sentence.equals("") && !title.equals("")){
            if(!imageurl.equals("") ){
                return imageurlset.add(imageurl) && newsurlset.add(newsurl)
                        && sentenceSet.add(sentence) && titleSet.add(title);
            }
            else{
                return newsurlset.add(newsurl)
                        && sentenceSet.add(sentence) && titleSet.add(title);
            }
        }
        return false;
    }



    private void Initiate(){
        titleSet.clear();
        imageurlset.clear();
        newsurlset.clear();
        sentenceSet.clear();
    }



    /**
     * load index from past news
     * @param pastNews
     */
    private boolean loadIndex(List<News> pastNews){
        Initiate();
        if(pastNews != null && pastNews.size() > 0){
            for(News n : pastNews ){
                imageurlset.add(n.getNewsImageUrl());
                newsurlset.add(n.getNewsUrl());
                sentenceSet.add(n.getFirstLastSentence());
                titleSet.add(n.getNewsTitle());  //used not exist ???????
            }
            return true;
        }
        return false;
    }


}
