package Property;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by yiqibai on 12/23/15.
 * RemoteProperty is the kwb.propeties file out of ./src directory.
 * Using RemoteProperty, kwb.properties out of ./src directory will cover the
 * kwb.properties in ./src directory.
 * usually it will under the same directory with kwb main file on server, which is
 * for modify kwb.properties file easily.
 */

public class RemoteProperty extends Property {
    private String filePath = "./";

    RemoteProperty(String filename){
        filePath  += filename;
        fileName = filename;
        loadProperty();
    }


    @Override
    protected void loadProperty(){
        try {
            FileInputStream filepath = new FileInputStream(filePath);
            Properties.load(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
