package Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yiqibai on 12/28/15.
 */
public class CateKWord {
    private  Map<String, Map<String, Double>> cateKWord = new HashMap<String, Map<String, Double>>();



    public  Map<String, Map<String, Double>> getCateKWord() {
        return cateKWord;
    }


    public  void setCateKWord(Map<String, Map<String, Double>> category) {
        cateKWord = category;
    }
}
