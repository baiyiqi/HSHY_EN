package Entity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.*;

/**
 * Created by yiqibai on 12/26/15.
 */
public class HSKeywords {


    //[category:[word:[tf, df, tfidf]]]
    private Map<String, Map<String, double[]>> categoryKeywords = new HashMap<String, Map<String, double[]>>();
    //[category: 0: document, 1:words num]
    private Map<String, double[]> docWordsNum = new HashMap<String, double[]>();




    public void setCategoryKeywords(Map<String, Map<String, double[]>> categoryKeywords) {
        this.categoryKeywords = categoryKeywords;
    }



    public Map<String, Map<String, double[]>> getCategoryKeywords(){
        return categoryKeywords;
    }



    public void setDocWordsNum(Map<String, double[]> docWordsNum) {
        this.docWordsNum = docWordsNum;
    }



    public Map<String, double[]> getDocWordsNum() {
        return docWordsNum;
    }



    public List<DBObject> cateKWordToBasicDB(){
        List<DBObject> dbObjectList = new ArrayList<DBObject>();

        Map<String, Map<String, double[]>> data = new HashMap<String, Map<String, double[]>>();
        for(String cate : categoryKeywords.keySet()){
            for(String word : categoryKeywords.get(cate).keySet()){
                if(!data.containsKey(word))
                    data.put(word, new HashMap<String, double[]>());
                if(!data.get(word).containsKey(cate))
                    data.get(word).put(cate, categoryKeywords.get(cate).get(word));
            }
        }

        for(String word : data.keySet()){
            BasicDBObject wobj = new BasicDBObject();
            wobj.append("word", word);
            BasicDBObject catobj = new BasicDBObject();
            for(String cate : data.get(word).keySet()){
                double[] percent = data.get(word).get(cate);
                BasicDBObject perObj = new BasicDBObject();
                perObj.append("tf", percent[0]);
                perObj.append("df", percent[1]);

                catobj.append(cate, perObj);

            }
            wobj.append("categoryScore", catobj);
            dbObjectList.add(wobj);
        }

        return dbObjectList;
    }



    public List<DBObject> docToBasicDB(){
        List<DBObject> dbObjectList = new ArrayList<DBObject>();

        for(String cate : docWordsNum.keySet()){
            BasicDBObject obj = new BasicDBObject();
            obj.append("category", cate);
            obj.append("documents", docWordsNum.get(cate)[0]);
            obj.append("words", docWordsNum.get(cate)[1]);
            dbObjectList.add(obj);
        }
        return dbObjectList;
    }



    public Set<String> getHSKeyWords(){
        Set<String> keywords = new HashSet<String>();
        for(String c : categoryKeywords.keySet()){
            for(String s : categoryKeywords.get(c).keySet()){
                keywords.add(s.toLowerCase());
            }
        }
        return keywords;
    }
}
