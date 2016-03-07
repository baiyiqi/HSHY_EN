package HSDetector;

import Entity.News;
import KWPatten.ModuleProcessor;
import Entity.NewsSubjects;
import KWPatten.Observers;
import Log.Logs;
import Utility.tool;

import java.util.*;


/**
 * Created by yiqibai on 12/27/15.
 */
public class HSDetector implements Observers, ModuleProcessor {
    List<News> newsList = new ArrayList<News>();
    Logs logs;
    Set<String> allEnglishWords;
    Set<String> hsKeyWord;


    public  HSDetector(Logs logs, Set<String> allEnglishWords, Set<String> hsKeyWord){
        this.logs = logs;
        this.allEnglishWords = allEnglishWords;
        this.hsKeyWord = hsKeyWord;
    }



    @Override
    public void update(NewsSubjects subject) {
        newsList = subject.getCurrentNews();
        coreProcess();
        logs.info("DetectorInfo : Updated");

    }



    @Override
    public void preProcess() {

    }



    @Override
    public void coreProcess() {
        detectNewMed();
    }



    @Override
    public void finalProcess() {

    }



    private void detectNewMed(){
        for(News n : newsList){
            if(n.getIsDup()){
                continue;
            }
            if(n.getNewsText().contains("新药") || n.getNewsTitle().contains("新药")){
                n.setNewMedicineTrue();
                n.setNewMedWord(getNewMedInfo(n.getNewsText()));

            }
            if(n.getNewsText().contains("新医疗") || n.getNewsTitle().contains("新医疗")){
                n.setNewTreatmentTrue();
                n.setNewMedWord(getNewMedInfo(n.getNewsText()));
            }
        }

    }



    private Map<String, String> getNewMedInfo(String txt){
        Set<String> medWords = extratNewWords(txt);
        List<String> sentences = tool.getSegText(txt);
        Map<String, String> rst = new HashMap<String, String>();

        if(medWords.size() == 0 || sentences.size() == 0)
            return rst;
        for(String mword : medWords){
            rst.put(mword, getFirstSentence(mword, sentences));
        }
        return rst;
    }



    private Set<String> extratNewWords(String txt){
        Set<String> newMedWords = new HashSet<String>();
        for(String s : tool.extractEnglish(txt)){
            String ns = s.toLowerCase();
            if(!allEnglishWords.contains(ns) && !hsKeyWord.contains(ns)){
                newMedWords.add(s);
            }
        }
        return newMedWords;
    }



    private String getFirstSentence(String word, List<String> sentences){
        for(String s : sentences){
            if(s.contains(word)){
                return s;
            }
        }
        return "";
    }

}
