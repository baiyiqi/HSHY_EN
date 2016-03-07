package Remover;


import Entity.News;

import java.util.List;

/**
 * Created by yiqibai on 7/1/15.
 */
public interface Remover {

    /**
     * remove duplicate news in previous news
     * @return
     */
    public void pastRemover(List<News> currentNews, List<News> pastNews);



    /**
     * remove duplicate news in current loaded news
     * @return
     */
    public void realTimeRemover(List<News> currentNews);
}
