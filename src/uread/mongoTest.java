/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uread;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import java.net.UnknownHostException;

/**
 *
 * @author tigospodinova
 */
public class mongoTest {
    public static void main(String[] args) throws UnknownHostException{
        Mongo m = new Mongo();
        DB db = m.getDB( "uRead" );
        DBCollection books = db.getCollection("books");
        DBCollection users = db.getCollection("users");
        
        BasicDBObject book = new BasicDBObject();

        book.put("ISBN-10", "0764588745");
        book.put("ISBN-13", "978-0764588747");
        book.put("author", "Barry Burd");
        book.put("title", "Beginning Programming with Java For Dummies");
        book.put("floor", "1st Floor");
        book.put("section", "A1 C7");

//        books.insert(book);
        
        BasicDBObject find = Books.searchTitle("java");
        
        mongoHelper helper = new mongoHelper();
        System.out.println(helper.findBook(find));
        
    }
   
    
}
