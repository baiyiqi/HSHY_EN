package Remover;

import Entity.News;
import KWPatten.ModuleProcessor;
import Entity.NewsSubjects;
import KWPatten.Observers;
import Log.Logs;
import Utility.tool;

import java.util.List;

/**
 * Created by yiqibai on 12/24/15.
 */
public class DupRemover implements Observers, ModuleProcessor{

    List<News> currentNews;
    List<News> pastNews;
    Remover remover;
    Logs loger;

    public DupRemover(Remover remover, Logs loger){
        this.remover = remover;
        this.loger = loger;
    }



    @Override
    public void update(NewsSubjects NewsSubjects){
        if(NewsSubjects != null){
            currentNews = NewsSubjects.getCurrentNews();
//            System.out.println(" current news");
//            tool.printNewsTitle(currentNews);
        }
        if(NewsSubjects != null){
            pastNews = NewsSubjects.getPastNews();
//            System.out.println(" past news");
//            tool.printNewsTitle(pastNews);
        }

        coreProcess();
        loger.info("remover updated");
    }


    @Override
    public void preProcess(){

    }


    @Override
    public void coreProcess(){
        if(currentNews != null){
            remover.realTimeRemover(currentNews);
            if(pastNews != null){
                remover.pastRemover(currentNews, pastNews);
            }
        }
    }


    @Override
    public void finalProcess(){}




}
