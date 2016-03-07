package Property;

import java.io.IOException;

/**
 * Created by yiqibai on 12/23/15.
 * localPropety is the kwb.properties under path: ./src/
 * for debug on local server
 */
public class LocalProperty extends Property {


    LocalProperty(String filename){
        filePath = filename;
        fileName = filename;
        loadProperty();
    }


    @Override
    protected void loadProperty(){
        try {
            Properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
