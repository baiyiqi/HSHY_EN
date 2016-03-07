package Entity;


import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yiqibai on 12/25/15.
 * score : popurank
 * textArray : segment text to List
 * simiTag : false (no similar)
 * sentence : first sentence  + last sentence. For remove duplications
 * keywordmap :
 * newMedicineTag : false(not new medicine)
 */
public class HSNews extends News {

    private double rankScore;
    private boolean isDup;
    private Map<String, Integer> keyWordMap;
    private boolean isNewMedicine ;
    private boolean isNewTreatment ;
    private Map<String, String> newMedWord;
    private String html;
    private String enTitle;
    private String enText;

    public HSNews(String title, String text, long ts, String src, String imageurl, String summary,
                  String newsurl, String cate, long id, String html, String entitle, String entext) {
        super(title, text, ts, src, imageurl, summary, newsurl, cate, id);

        keyWordMap = new HashMap<String, Integer>();
        isNewMedicine = false;
        isNewTreatment = false;
        isDup = false;
        rankScore = 0.0;
        newMedWord = new HashMap<String, String>();
        this.html = html;
        enTitle = entitle;
        enText = entext;
    }



//    @Override
//    public boolean isValidNews() {
//        if(!isDup && !getNewsTitle().equals("") && !getNewsText().equals("") && !getNewsSummary().equals("")
//                && !getNewsTimeReadble().equals("") && !getNewsUrl().equals("")){
//            return true;
//        }
//        return false;
//    }



    public double getRankScore(){
        rankScore = Double.parseDouble(new DecimalFormat("##.######").format(rankScore));
        return rankScore;
    }



    public void setRankScore(double score){
        this.rankScore = score;
    }



    public void addRankScore(double score) {

    }



    public void setIsDup(){
        isDup = true;
    }



    public boolean getIsDup(){ return isDup; }



    public void setKeyWordMap(Map<String, Integer> map){
        keyWordMap = map;
    }



    public Map<String, Integer> getKeyWordMap(){
        return keyWordMap;
    }



    public void setNewMedicineTrue(){
        isNewMedicine = true;
    }



    public boolean getIsNewMedicine(){
        return isNewMedicine;
    }



    public void setNewTreatmentTrue() {
        isNewTreatment = true;
    }



    public boolean getIsNewTreatment() {
        return isNewTreatment;
    }



    public int getKeyWordNum(String word){
        if(!keyWordMap.containsKey(word)){
            return 0;
        }
        return keyWordMap.get(word);
    }



    public void setNewMedWord(Map<String, String> map) {
        this.newMedWord = map;
    }



    @Override
    public Map<String, String> getNewMedWord() {
        return newMedWord;
    }


    @Override
    public String getNewsHtml(){
        return html;
    }



    @Override
    public String getEnText() {
        return enText;
    }


    @Override
    public String getEnTitle() {
        return enTitle;
    }
}
