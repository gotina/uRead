/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uread;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tigospodinova
 */
public class mongoHelper {
    DB db;
    DBCollection books; 
    DBCollection users; 
    public mongoHelper(){
        Mongo m;
        try {
            m = new Mongo();
            db = m.getDB( "uRead" );
            books = db.getCollection("books");
            users = db.getCollection("users");
        } catch (UnknownHostException ex) {
            Logger.getLogger(mongoHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MongoException ex) {
            Logger.getLogger(mongoHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void writeBook(BasicDBObject book){
        books.insert(book);   
    }
    
    public void writeUser(BasicDBObject user){
        users.insert(user);   
    }
    
    public DBObject findBook(BasicDBObject query){
        return books.find(query).next();
    }
}
