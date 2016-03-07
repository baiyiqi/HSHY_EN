package KWBRank;


import java.util.HashMap;
import java.util.Map;


/**
 * Created by yiqibai on 12/26/15.
 */
public class PopuRankWord {
    private static Map<String, Double[]> popuRank ;
    private static Map<String, Double> sudWords ; //both tf and df sudden increased words
    private static Map<String, Map<Long, Double>> popuRankTable = new HashMap<String, Map<Long, Double>>();




    public static void setPopuRank (Map<String, Double[]> popurank ){ PopuRankWord.popuRank = popurank; }



    public static Map<String, Double[]> getPopuRank(){
        return popuRank;
    }



    public static void setSudWords(Map<String, Double>  sudWords) {
        PopuRankWord.sudWords = sudWords;
    }



    public static Map<String, Double>  getSudWords() {
        return sudWords;
    }



    public static Map<String, Map<Long, Double>> getPopuRankTable() {
        return popuRankTable;
    }
}
