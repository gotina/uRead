/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uRead;

import com.mongodb.BasicDBObject;
import java.util.regex.Pattern;

/**
 *
 * @author tigospodinova
 */
public class Books {
    public static BasicDBObject findByTitle(String title){
        BasicDBObject ret = new BasicDBObject();
        ret.put("title", title);
        return ret;
    }
    
    public static BasicDBObject searchTitle(String title){
        Pattern searchQuery = Pattern.compile(title, Pattern.CASE_INSENSITIVE);
        BasicDBObject query = new BasicDBObject("title", searchQuery);
        return query;
    }
    
}
