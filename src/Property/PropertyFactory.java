package Property;

/**
 * Created by yiqibai on 12/23/15.
 */
public abstract class PropertyFactory {

    /**
     * create the kwbproperty object.
     * @param file
     * @return
     */
    public abstract  Property createProperty(String file);
}
