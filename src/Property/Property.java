package Property;


import java.util.Properties;

/**
 * Created by yiqibai on 12/23/15.
 */
public abstract class Property {
    protected Properties Properties = new Properties();;
    protected String filePath = "";
    protected String fileName = "";


    protected abstract void loadProperty();


    /**
     * set the property file path
     * @param path
     */
    public void setFilePath(String path){
        filePath = path;
    }


    /**
     * @return path of the property
     */
    public String getFilePath(){
        return filePath;
    }


    /**
     * @return int
     */
    public int getInt(String key) {
        return Integer.valueOf( Properties.getProperty(key) );
    }


    /**
     *
     * @param key
     * @return
     */
    public double getDouble(String key){
        return Double.parseDouble(Properties.getProperty(key));
    }


    /**
     * @param key
     * @return
     */
    public boolean getBoolean(String key){
        return Boolean.parseBoolean(Properties.getProperty(key));
    }


    /**
     *
     * @param  key
     * @return String
     */
    public String getString(String key) {
        return Properties.getProperty(key);
    }


    /**
     * save properties in form of key = value into properties file
     * @param key
     * @param value
     */
    public void putIntoProperties(String key, String value){
        Properties.put(key, value);
    }


    /**
     * get value of given key
     * @param key
     * @return
     */
    public String getProperty(String key){
        return Properties.getProperty(key);
    }



    public String getFile() {
        return fileName;
    }


    public long getLong(String key){
        return Long.parseLong(Properties.getProperty(key));
    }
}
