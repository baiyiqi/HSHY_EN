package DataBase;

import Entity.HSKeywords;
import Log.Logs;
import Property.Property;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by yiqibai on 12/27/15.
 */
public class MongoKWord extends MongoDB {
    private HSKeywords hsKeywords;
    private String kwordColl;
    private String docColl;



    public MongoKWord(Property hsProperty, Logs loger, HSKeywords hsKeywords) {
        super(hsProperty.getString("mongo-ip"), hsProperty.getInt("mongo-port"), loger);
        setDb(hsProperty.getString("keyword-DB"));

        this.hsKeywords = hsKeywords;
        this.kwordColl = hsProperty.getString("keyword-coll");
        this.docColl = hsProperty.getString("doc-coll");
    }



    @Override
    public void Load() {
        hsKeywords.setCategoryKeywords(loadKeyWords());
        hsKeywords.setDocWordsNum(loadDocWordNum());
    }



    @Override
    public void save() {
        updateKeyword(hsKeywords.cateKWordToBasicDB());
        updateDocNumWordNum(hsKeywords.docToBasicDB());
        mongo.close();
    }



    /**
     * @return [category:[word:[tf, df, tfidf]]]
     */
    public Map<String, Map<String, double[]>> loadKeyWords() {
        Map<String, Map<String, double[]>> CategoryKeywords = new HashMap<String, Map<String, double[]>>();

        String word = "";
        try {
            DBCursor curObj = mongo.getDB(getDb()).getCollection(kwordColl).find();//###########
            while (curObj.hasNext()) {
                BasicDBObject obj = (BasicDBObject) curObj.next();
                word = obj.getString("word");
                BasicDBObject categoryScore = (BasicDBObject) obj.get("categoryScore");
                for (String cate : categoryScore.keySet()) {
                    BasicDBObject value = (BasicDBObject) categoryScore.get(cate);
                    double[] tfdf = new double[3];

                    tfdf[0] = Double.parseDouble(value.getString("tf"));
                    tfdf[1] = Double.parseDouble(value.getString("df"));
                    tfdf[2] = 0.0;

                    if (!CategoryKeywords.containsKey(cate))
                        CategoryKeywords.put(cate, new HashMap<String, double[]>());
                    CategoryKeywords.get(cate).put(word, tfdf);
                }
            }
        }catch (Exception e){
            loger.server("MongoException [ can't load keyword ]");
        }
        finally {
            loger.info("MongoInfo [ KeywordsLoaded: " + CategoryKeywords.size()  +"]");
            return CategoryKeywords;
        }
    }



    /**
     * @return [category: Documentnum]
     */
    public Map<String, double[]> loadDocWordNum() {
        Map<String, double[]> DocumentNum = new HashMap<String, double[]>();
        try{
            BasicDBObject query = new BasicDBObject();
            BasicDBObject proj = new BasicDBObject();

            DBCursor curObj = mongo.getDB(getDb()).getCollection(docColl).find(query, proj);//###########
            while(curObj.hasNext()){
                BasicDBObject obj = (BasicDBObject) curObj.next();
                String cate = obj.getString("category");
                double dcount = obj.getDouble("documents");
                double wcount = obj.getDouble("words");

                double[] count = new double[2];
                count[0] = dcount;
                count[1] = wcount;
                DocumentNum.put(cate, count);
            }
        }catch (Exception e){
            loger.server("MongoException [ "+ e.getMessage() + " ]");
        }finally {
            loger.info("MongoInfo [ DocmentInfoLoaded: " + DocumentNum.size()  +"]");
            return DocumentNum;
        }
    }




    public void updateKeyword(List<DBObject> wordlst){
        try{
            mongo.getDB(getDb()).getCollection(kwordColl).drop();
            mongo.getDB(getDb()).getCollection(kwordColl).insert(wordlst);
            loger.info("MongoInfo [ HuaSheng keyword updated ]");
        }catch (Exception e){
            loger.server("MongoException [ "+ e.getMessage() + " ]");
        }
        finally {

        }
    }



    /**
     * update DocumentNum and WordNum in DB
     */
    public void updateDocNumWordNum(List<DBObject> docList){
        try {
            mongo.getDB(getDb()).getCollection(docColl).drop();
            mongo.getDB(getDb()).getCollection(docColl).insert(docList);
            loger.info("MongoInfo [ HuaSheng document updated ]");
        }catch (Exception e){
            loger.server("MongoException [ "+ e.getMessage() + " ]");
        }finally {

        }
    }
}
