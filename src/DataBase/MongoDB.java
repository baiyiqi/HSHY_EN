package DataBase;

import Log.Logs;
import com.mongodb.MongoClient;

/**
 * Created by yiqibai on 12/27/15.
 */
public abstract class MongoDB implements DataBase {
    private String ip;
    private int port;
    private String db;
    MongoClient mongo;
    Logs loger;



    MongoDB(String ip, int port, Logs loger) {
        this.loger = loger;
        this.ip = ip;
        this.port = port;
        Connect();
    }



    @Override
    public void Connect() {
        try {
            mongo = new MongoClient(ip, port);
        } catch (Exception e) {
            loger.server("MongoException [" + e.getMessage() + "]");
        }
    }



    @Override
    public void Close() {
        mongo.close();
    }


    public void setIp(String ip) {
        this.ip = ip;
    }


    public String getIp() {
        return ip;
    }


    public void setPort(int port) {
        this.port = port;
    }


    public int getPort() {
        return port;
    }


    public void setDb(String db) {
        this.db = db;
    }


    public String getDb() {
        return db;
    }



}
