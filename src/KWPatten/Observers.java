package KWPatten;


import Entity.NewsSubjects;

/**
 * Created by yiqibai on 12/24/15.
 */
public interface Observers {


    /**
     * each module do the calculation and updating in this function
     * each module has their own data in their class;
     * @param subjects
     */
    public void update(NewsSubjects subjects);
}
