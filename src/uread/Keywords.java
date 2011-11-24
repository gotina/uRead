package uread;

import java.lang.String;
import java.util.ArrayList;

/** Represents a set of keywords & allows searching a string for matches */
public class Keywords {

	/** Creates a set of keywords based on a provided string.
	  * Keywords are separated on whitespace, and punctuation is removed.
	@param newSource String containing keywords
*/	public Keywords( String newSource ) {
		source = newSource;
		String[] newWords = newSource.toLowerCase().split("\\s*");//split on whitespace
		words = new ArrayList<String>( newWords.length );
		for( String word : newWords ) { words.add( clean( word ) ); }
	}

	/** Removes punctuation from a string
	@param dirty A string which may contain punctuation
	@return A string which contains every non-punctuation character in dirty
*/	public static String clean( String dirty ) {
		return dirty.replaceAll( "^\\w*", "" );//replace non-word characters with nothing
	}

	/** Find the first matching keyword in a string
	@param match The string in which to find matches
	@return The keyword which occurs first, or null if none occur
*/	public String first( String match ) {
		for( String w : match.toLowerCase().split("\\s*") ) {//split on whitespace
			for( String k : words ) {
				if( k.equals( clean( w ) ) ) { return k; }
			}
		}
		return null;
	}

	private String source; //the original string from which these keywords were extracted
	private ArrayList<String> words; //the keywords

}
