package Entity;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yiqibai on 12/25/15.
 */
public interface newsAttribute {


    /**
     * @return news hot score
     */
    public double getRankScore();


    /**
     * add scores
     * @param score
     */
    public void setRankScore(double score);



    public void setIsDup();



    public boolean getIsDup();



    /**
     * get keyword map
     * @return
     */
    public Map<String, Integer> getKeyWordMap();



    /**
     *
     * @param map
     */
    public void setKeyWordMap(Map<String, Integer> map);



    public void addRankScore(double score);



    public void setId(long id);



    public long getId();



    public int getKeyWordNum(String word);



    public void setNewTreatmentTrue();



    public boolean getIsNewTreatment();



    public void setNewMedicineTrue();



    public boolean getIsNewMedicine();



    public void setNewMedWord(Map<String, String> map);



    public Map<String, String> getNewMedWord();



    public String getNewsHtml();


    public String getEnTitle();


    public String getEnText();

}
