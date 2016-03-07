package DataBase;


/**
 * Created by yiqibai on 12/23/15.
 */
public interface DataBase{


    /**
     * connect mongo
     */
    public  void Connect();


    public  void Load();


    /**
     * close connection
     */
    public  void Close();


    public void save();


}
