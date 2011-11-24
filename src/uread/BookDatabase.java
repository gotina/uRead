
package uread;

import java.lang.String;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.Collection;

import org.yaml.snakeyaml.Yaml;

/** Represents a Book Database */
public class BookDatabase {

/** Indicates the Title field for {@link #search} */
	public static final int S_TITLE = 1;
/** Indicates the Author field for {@link #search} */
	public static final int S_AUTHOR = 2;
/** Indicates the ISBN field for {@link #search} */
	public static final int S_ISBN = 4;
/** Indicates the Description field for {@link #search} */
	public static final int S_DESCRIPTION = 8;

/** Creates an empty {@link BookDatabase}.
*/	public BookDatabase() { books = new ArrayList<Book>(); }

/** Connects to a {@link BookDatabase} previously {@link #save}d to a file
@param filename The file containing the previously {@link #save}d {@link BookDatabase}
*/	public BookDatabase( String filename ) throws java.io.FileNotFoundException
		{ books = new ArrayList<Book>(); load(filename); }

/** Returns the number of {@link Book}s in the database.
@return The number of {@link Book}s in the database
*/	public int count() { return books.size(); }

/** Returns the {@link Book} at the given index.
  * Note that the index of a book may change when books are {@link #remove}d.
@param index Which {@link Book} to get, valid from 0 to {@link #count()}-1 (inclusive)
@return The specified {@link Book}
*/	public Book get( int index ) { return books.get( index ); }


/** Adds a {@link Book} to the database.
@param book The {@link Book} to add
@return Returns true (to match {@link Collection#add(E)})
*/	public boolean add( Book book ) { return books.add( book ); }

/** Removes a {@link Book} from the database.
@param index The index of the {@link Book} to remove
@return The {@link Book} which has been removed
*/	public Book remove( int index ) { return books.remove( index ); }

/** Removes a {@link Book} from the database.
@param book The {@link Book} to remove
@return True if the {@link Book} was found and removed, false if the {@link Book} was not in this database
*/	public boolean remove( Book book ) { return books.remove( book ); }

/** Searches this {@link BookDatabase}.
@param fields Which fields to check - a sum of at least one of {@link BookDatabase#S_TITLE}, {@link BookDatabase#S_AUTHOR}, and {@link BookDatabase#S_ISBN}.
@param query The string to attempt to match.  Matching rules may vary according to field (e.g. {@link BookDatabase#S_ISBN} is not sensitive to hyphenation).
@return A {@link SearchResults} of all books in this {@link BookDatabase} which have a match to the query in one or more of the indicated fields - if there are no matches, the {@link SearchResults} will be empty (the return value will not be null).
*/	public SearchResults search( int fields, String query ) {
		SearchResults r = new SearchResults( fields, query );
		Keywords k = new Keywords( query );
		int i=0;
		Book b; String m;
		while( i < books.size() ) {
			b=books.get(i);
			if( 0 != (fields & S_TITLE) ) {
				m = k.first( b.getTitle() );
				if( null != m ) {
					r.add( new SearchResult( b, S_TITLE, m ) );
				}
			} else if( 0 != (fields & S_AUTHOR) ) {
				m = k.first( b.getAuthor() );
				if( null != m ) {
					r.add( new SearchResult( b, S_AUTHOR, m ) );
				}
			} else if( 0 != (fields & S_ISBN) ) {
				m = k.first( b.getISBN13() );
				if( null == m ) { m = k.first( b.getISBN10() ); }
				if( null != m ) {
					r.add( new SearchResult( b, S_ISBN, m ) );
				}
			} else if( 0 != (fields & S_DESCRIPTION) ) {
				m = k.first( b.getDescription() );
				if( null != m ) {
					r.add( new SearchResult( b, S_DESCRIPTION, m ) );
				}
			}
			i++;
		}
		return r;
	}

/** Generates a String representation of this entire database.
@return A String representation of this database
*/	public String toString() {
		if( 0 == books.size() ) { return "Database is empty"; }
		Book b=books.get(0);
		String s="--- Book 1:\n"+b.toString();
		int i=1;
		while( i < books.size() ) { b=books.get(i); i++; s+= "\n--- Book "+i+":\n"+b.toString();  }
		return s;
	}

/** Generates a YAML representation of this entire database
@return A YAML representation of this database
*/	public String toYaml() { return (new Yaml()).dumpAll( books.iterator() ); }

/** Saves this {@link BookDatabase} to a file.
  * Overwrites any existing file.
@param filename The name of the file to overwrite with this {@link BookDatabase}
@throws java.io.IOException If there is a problem writing the file
*/	public void save( String filename ) throws java.io.IOException {
		Yaml y = new Yaml();
		FileWriter w = new FileWriter(filename);
		y.dumpAll( books.iterator(), w );
	}

/** Loads book information from a previously {@link #save}d database and adds them to this database.
@param filename The file containing the previously {@link #save}d {@link BookDatabase}
@throws java.io.FileNotFoundException If the file is not found
*/	public int load( String filename ) throws java.io.FileNotFoundException {
	Yaml y = new Yaml();
	Iterable add = y.loadAll( new FileReader(filename) );
	int r=0;
	for( Object b : add ) { r++; books.add( (Book) b ); }
	return r;
	}

	private ArrayList<Book> books;
}

