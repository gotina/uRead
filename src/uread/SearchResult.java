
package uRead;

import java.lang.String;

/** Represents a particular result generated by a search query on a {@link BookDatabase}. */
public class SearchResult {

/** Never create this yourself, always use {@link BookDatabase#search}.
*/	public SearchResult() { book=null; field=0; string=null; }

/** Never create this yourself, always use {@link BookDatabase#search}.
*/	public SearchResult( Book newBook, int newField, String newString )
	{ book=newBook; field=newField; string=newString; }

/** Returns the {@link Book} that this result represents.
@return The {@link Book} in which {@link #string} in {@link #field} matched the query which generated this result.
*/	public Book book() { return book; }

/** Returns the information field which matched the query which generated this result.
  * One of {@link BookDatabase#S_TITLE}, {@link BookDatabase#S_AUTHOR},
  * or {@link BookDatabase#S_ISBN}.
@return One of {@link BookDatabase#S_TITLE}, {@link BookDatabase#S_AUTHOR}, or {@link BookDatabase#S_ISBN}.
*/	public int field() { return field; }

/** Returns the string in the {@link #field} which matched the query which generated this result.
@return The string in the {@link #field} which matched the query which generated this result.
*/	public String string() { return string; }

/** Generate a multiline string representation of this {@link Book}.
@return A multiline string representation of this {@link Book}
*/	public String toString() {
		return "field "+field+" matched \""+string+"\" on this book:\n"+book.toString();
	}

	private Book book;
	private int field;
	private String string;
}

