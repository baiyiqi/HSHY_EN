package Classifier;

import Entity.News;
import Json.JSONObject;
import KWPatten.ModuleProcessor;
import Entity.NewsSubjects;
import KWPatten.Observers;
import Log.Logs;
import Property.Property;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by yiqibai on 12/27/15.
 */
public class Classifier implements Observers, ModuleProcessor{
    List<News> newsList = new ArrayList<News>();
    String url = "";
    Logs loger;


    public Classifier(Property property){
        url = property.getString("classify-url");
    }



    @Override
    public void update(NewsSubjects subject) {
        newsList = subject.getCurrentNews();

        coreProcess();
    }



    @Override
    public void preProcess() {

    }



    @Override
    public void coreProcess() {
        for(News n : newsList){
            if(!n.getIsDup() && n.getNewsCate().equals("")){
                n.setNewsCate(getCategory(n.getNewsText()));
            }
        }
    }



    @Override
    public void finalProcess() {

    }



    private synchronized String getCategory(String text){
        String cate = "";
        try{
            JSONObject data = new JSONObject();
            data.put("text", text);

            String rst = HttpRequest.sendPost(url, data.toString());
            if(rst.contains("label")){
                JSONObject jTag = new JSONObject(rst);
                cate = String.valueOf(jTag.get("label"));
            }
        }catch (Exception e){
            loger.server("CLASSIFICATION Exception: [ " + e.getMessage() +" ]");
        }

        return cate;
    }


}
