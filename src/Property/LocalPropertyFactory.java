package Property;


/**
 * Created by yiqibai on 12/23/15.
 */
public class LocalPropertyFactory extends PropertyFactory {

    @Override
    public Property createProperty(String file){
        return new LocalProperty(file);
    }

}
