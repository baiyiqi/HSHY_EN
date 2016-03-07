package DataBase;


import Entity.HSNews;
import Entity.News;
import Log.Logs;
import Entity.NewsSubjects;
import Property.Property;
import Utility.tool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiqibai on 12/27/15.
 */
public class MySql implements DataBase {
    private Connection connect;
    private String url;
    private String user;
    private String pwd;
    private String table;
    NewsSubjects newsSubjects;
    Logs loger;


    public MySql(Property hsProperty, Logs loger, NewsSubjects subjects){
        this.loger = loger;
        this.url = hsProperty.getString("mysql-url");
        this.user = hsProperty.getString("mysql-user");
        this.pwd = hsProperty.getString("mysql-pwd");
        this.table = hsProperty.getString("mysql-table");
        this.newsSubjects = subjects;

        Connect();
    }



    @Override
    public void Connect() {
        try{
            String myDriver = "org.gjt.mm.mysql.Driver";
            Class.forName(myDriver);
            connect = DriverManager.getConnection(url, user, pwd);
        }catch (SQLException e) {
            loger.server("SQLException [ " + e.getMessage() + " ]");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void Close() {
        try {
            connect.close();
        } catch (SQLException e) {
            loger.server("SQLException [ " + e.getMessage() + " ]");
        }
    }



    @Override
    public void Load() {
        long endTS = newsSubjects.getTs();
        long beginTS = newsSubjects.getTs() - newsSubjects.getTimerange();
        List<News> lst = new ArrayList<News>();

        try {
            String query = "SELECT * from " + table + " where grabbedTime >= " + beginTS +
                    " and grabbedTime < " + endTS;
            PreparedStatement pst = connect.prepareStatement(query);
            ResultSet resultSet = pst.executeQuery();


            while (resultSet.next()) {
                long grabTime = resultSet.getLong("grabbedTime");
                if (tool.getDayStartTimestamp(grabTime) != tool.getDayStartTimestamp(endTS))
                    continue;
                Long ts = resultSet.getLong("ts");

                String text = resultSet.getString("text");
                if (text == null || text.length() < 20)
                    continue;

                String entext = resultSet.getString("enText");
                if (entext == null || entext.length() < 20)
                    continue;

                String title = resultSet.getString("title");
                if (title == null || title.trim().equals(""))
                    continue;

                String entitle = resultSet.getString("enTitle");
                if (entitle == null || entitle.trim().equals(""))
                    continue;

                String newsUrl = resultSet.getString("newsUrl");
                if (newsUrl == null)
                    continue;

                String summary = resultSet.getString("abs");
                if (summary == null || summary.equals(""))
                    continue;

                String src = resultSet.getString("src");
                if (src == null || src.equals("null"))
                    src = "";

                String imageurl = resultSet.getString("imageurl");
                if (imageurl == null || !tool.isValidURL(imageurl))
                    imageurl = "";

//                String cate = resultSet.getString("category");
//                if (cate == null)
//                    cate = "";
//                else if (cate.length() > 4)
//                    cate = cate.substring(0, 4);

                if (!tool.isValidTime(ts)) {
                    continue;
                }

                String html = resultSet.getString("contentHTML");
                if(html == null){
                    html = "";
                }

                News n = new HSNews(title.trim(), text.trim(), ts, src, imageurl, summary.trim(), newsUrl, "", lst.size(), html, entitle.trim(), entext.trim());
                lst.add(n);
            }
//            newsSubjects.setCurrentNews(lst);
            loger.info("SQLInfo [ Load News " + lst.size() + " ]");

        } catch (SQLException e) {
            loger.server("SQLException [ " + e.getMessage() + " ]");
        }
        finally {
            newsSubjects.setCurrentNews(lst);
            this.Close();
        }
    }



    @Override
    public void save() {
    }

}
