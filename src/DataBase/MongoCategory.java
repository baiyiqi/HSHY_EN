package DataBase;

import Entity.CateKWord;
import Log.Logs;
import Property.Property;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yiqibai on 12/28/15.
 */
public class MongoCategory extends MongoDB {
    private String cateColl;

    CateKWord cateKWord;



    public MongoCategory(Property hsProperty, Logs loger, CateKWord cateKWord) {
        super(hsProperty.getString("mongo-ip"), hsProperty.getInt("mongo-port"), loger);
        setDb(hsProperty.getString("cate-db"));

        cateColl = hsProperty.getString("cate-coll");
        this.cateKWord = cateKWord;
    }



    @Override
    public void Load() {
        Map<String, Map<String, Double>> cateKWord = new HashMap<String, Map<String, Double>>();

        BasicDBObject query = new BasicDBObject();
        BasicDBObject proj = new BasicDBObject("category", 1)
                .append("keyword", 1);
        try{
            DBCursor c = mongo.getDB(getDb()).getCollection(cateColl).find(query, proj);//###########
            while(c.hasNext()){
                BasicDBObject obj = (BasicDBObject) c.next();
                String cate = obj.getString("category");
                BasicDBObject catekword = (BasicDBObject)obj.get("keyword");

                cateKWord.put(cate, catekword.toMap());
            }
            loger.info("MongoInfo [ Load Category: " + cateKWord.size() + " ]");
        }catch (Exception e){
            loger.server("MongoServer [ " + e.getMessage() + " ]");
        }
        finally {
            mongo.close();
            this.cateKWord.setCateKWord(cateKWord);
        }
    }

    @Override
    public void save() {

    }
}
