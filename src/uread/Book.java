
package uread;

import java.lang.String;

/** Represents a book, usually a book cataloged in a {@link BookDatabase} */
public class Book {

/** Empty constructor creates a book with no properties.  You shouldn't use this.
  * If used, follow immediately by setters for at least title, author, year, ISBN13, and description.
*/	public Book() { title = edition = author = publisher = ISBN10 = ISBN13 = description = null; year = 0; }

/** Normal constructor, creates a book with the most important fields set.
*/	public Book( String newTitle, String newAuthor, int newyear, String newISBN13, String newDescription )
	{ edition = publisher = ISBN10 = null; setTitle( newTitle ); setAuthor( newAuthor ); setYear( newyear); setISBN13( newISBN13 ); setDescription( newDescription ); }

//**TODO: complete constructor

/** Returns the title of this {@link Book}.
@return The title of this {@link Book}
*/	public String getTitle() { return title; }

/** Replaces the title of this {@link Book}.
*/	public void setTitle( String newtitle ) { title = newtitle; }

/** Returns the edition name of this {@link Book}.
  * This may identify a revision ("Third Edition"), a variant ("East Coast Edition"), a custom
  * edition ("Georgia State University Edition"), or whatever the publisher deems appropriate.
@return The edition name of this {@link Book}
*/	public String getEdition() { return edition; }

/** Returns the (common era) year in which this {@link Book} was printed.
@return The year in which this {@link Book} was printed
*/	public int getYear() { return year; }

/** Replaces the (common era) year in which this {@link Book} was printed.
*/	public void setYear( int newyear ) { year = newyear; } //TODO: validation

/** Returns the name of the author of this {@link Book}.
@return The name of the author of this {@link Book}
*/	public String getAuthor() { return author; }

/** Replaces the name of the author of this {@link Book}.
*/	public void setAuthor( String newauthor ) { author = newauthor; }

/** Returns the name of the publisher of this {@link Book}.
@return The name of the publisher of this {@link Book}
*/	public String getPublisher() { return publisher; }

/** Returns this {@link Book}'s 10-digit ISBN conforming to the 1970 standard.
@return This {@link Book}'s 10-digit ISBN conforming to the 1970 standard
*/	public String getISBN10() { return ISBN10; }

/** Returns this {@link Book}'s 13-digit ISBN conforming to the 2007 standard.
@return This {@link Book}'s 13-digit ISBN conforming to the 2007 standard
*/	public String getISBN13() { return ISBN13; }

/** Replace this {@link Book}'s 13-digit ISBN conforming to the 2007 standard.
*/	public void setISBN13( String newISBN13 ) { ISBN13 = newISBN13; } //TODO: validation; derive ISBN10

/** Returns this {@link Book}'s description.
@return This {@link Book}'s description
*/	public String getDescription() { return description; }

/** Replace this {@link Book}'s description.
@return This {@link Book}'s previous description
*/	public void setDescription( String newdescription ) { description = newdescription; }

/** Generate a multiline string representation of this {@link Book}.
@return A multiline string representation of this {@link Book}
*/	public String toString() {
		String s = "";
		if( null != title ) { s = "title: "+getTitle()+"\n"; }
		if( null != edition ) { s += "edition: "+getEdition()+"\n"; }
		if( 0 != year ) { s += "year: "+getYear()+"\n"; }
		if( null != author ) { s += "author: "+getAuthor()+"\n"; }
		if( null != publisher ) { s += "publisher: "+getPublisher()+"\n"; }
		if( null != ISBN10 ) { s += "ISBN10: "+getISBN10()+"\n"; }
		if( null != ISBN13 ) { s += "ISBN13: "+getISBN13()+"\n"; }
		if( null != description ) { s += "description: "+getDescription()+"\n"; }
		return s;
	}

	private String title;
	private String edition;
	private int year;
	private String author;
	private String publisher;
	private String ISBN10; //exactly 10 chars
	private String ISBN13; //exactly 13 chars
	private String description;
}

