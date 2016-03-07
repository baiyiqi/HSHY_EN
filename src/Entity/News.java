package Entity;

/**
 * Created with IntelliJ IDEA.
 * User: BYQ
 * Date: 6/8/14
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */

import Utility.tool;

import java.util.List;

public abstract class News implements newsAttribute {

    private String newsTitle = "";
    private String newsText;
    private String newsSrc = "";
    private String newsCate = "";
    private String newsUrl = "";
    private String newsImageUrl = "";
    private long newsTime = 0;    //news generate time timestamp
    private String newsTimeReadble = "";  //readble time
    private String newsSummary = "";
    private String firstLastSentence = "";
    private List<String> textSegArray;
    private long id;


    public News(String title, String text, long ts, String src,
                String imageurl, String summary, String newsurl, String cate, long id){

        newsTitle = title;
        newsText = text;
        newsSummary = summary;
        newsSrc = src;
        newsCate = cate;
        newsUrl = newsurl.trim();
        newsImageUrl = imageurl.trim();
        newsTime = ts;
        newsTimeReadble = tool.convertTS(ts);
        firstLastSentence = tool.sentence(summary);
        textSegArray = tool.segmentText(text);

    }



    /**
     * @return title
     */
    public String getNewsTitle(){
        return newsTitle;
    }


    /**
     * @return Content
     */
    public String getNewsText(){
        return newsText;
    }


    /**
     * @return news summary
     */
    public String getNewsSummary(){
        return newsSummary;
    }


    /**
     * @return source
     */
    public String getNewsSrc(){
        return newsSrc;
    }


    /**
     * @return category
     */
    public String getNewsCate(){
        return newsCate;
    }


    public void setNewsCate(String cate){ newsCate = cate; }


    /**
     * @return news url
     */
    public String getNewsUrl(){
        return newsUrl;
    }


    /**
     * @return Image url
     */
    public String getNewsImageUrl(){
        return newsImageUrl;
    }


    public long getNewsTime(){return newsTime;}


    /**
     * @return human readable time
     */
    public String getNewsTimeReadble(){
        return newsTimeReadble;
    }


    public String getFirstLastSentence(){ return firstLastSentence; }


    /**
     * @return segmented content
     */
    public List<String> getTextSegArray(){
        return textSegArray;
    }


    public void setId(long id) {
        this.id = id;
    }


    public long getId(){
        return id;
    }


    public boolean containsWord(String word){
        return newsText.contains(word);
    }


//    public abstract boolean isValidNews();


}
