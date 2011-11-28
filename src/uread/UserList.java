
package uRead;

import java.lang.String;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.Collection;

import org.yaml.snakeyaml.Yaml;

/** Represents a list of users. */
public class UserList {

/** Identifies the {@link User#type} as a patron or customer.
  * Patrons can modify only their own {@link BookList}
*/	public static final int U_PATRON = 1;
/** Identifies the {@link User#type} as a regular employee.
  * Regular employees can modify the {@link BookDatabase}.
*/	public static final int U_EMPLOYEE = 2;
/** Identifies the {@link User#type} as an administrator.
  * Administrators can modify the {@link BookDatabase} and the {@link UserList}
*/	public static final int U_ADMINISTRATOR = 4;

/** Constructs an empty {@link UserList}.
*/	public UserList() { users = new ArrayList<User>(); }

/** Reconstructs a {@link UserList} previously {@link #save}d to a file
@param filename The file containing the previously {@link #save}d {@link UserList}
*/	public UserList( String filename ) throws java.io.FileNotFoundException
		{ users = new ArrayList<User>(); load(filename); }

/** Returns the number of {@link User}s on the list.
@return The number of {@link User}s on the list
*/	public int count() { return users.size(); }

/** Returns the {@link User} object for the given user name
@param username the username to get
@return The {@link User} object for the given user name, or null if not on list
*/	public User get( String username ) {
		for( User user : users ) { if( user.getName().equals( username ) ) { return user; } }
		return null; 
	}

/** Adds a {@link User} to the list.
@param user The {@link User} to add
@return Returns true (to match {@link Collection#add(E)})
*/	public boolean add( User user ) { return users.add( user ); } //TODO: prevent duplicate usernames

/** Removes a {@link User} from the list.
@param username The username of the {@link User} to remove
@return The {@link User} which has been removed
*/	public User remove( String username ) {
		int i=0;
		while( i<users.size() ) {
			if( users.get(i).getName().equals( username ) ) { break; };
			i++;
		}
		return i<users.size() ? users.remove(i) : null;
	}

/** Removes a {@link User} from the list.
@param user The {@link User} to remove
@return The {@link User} which has been removed
@return True if the {@link Book} was found and removed, false if the {@link Book} was not in this database
*/	public boolean remove( User user ) { return users.remove( user ); }

/** Generates a String representation of this list.
@return A String representation of this list
*/	public String toString() {
		if( 0 == users.size() ) { return "List is empty"; }
		User u=users.get(0);
		String s="--- User 1:\n"+u.toString();
		int i=1;
		while( i <users.size() ) { u=users.get(i); i++; s+= "\n--- User "+i+":\n"+u.toString();  }
		return s;
	}

/** Generates a YAML representation of this list
@return A YAML representation of this list
*/	public String toYaml() { return (new Yaml()).dumpAll( users.iterator() ); }

/** Saves this {@link UserList} to a file.
  * Overwrites any existing file.
@param filename The name of the file to overwrite with this {@link UserList}
@throws java.io.IOException If there is a problem writing the file
*/	public void save( String filename ) throws java.io.IOException {
		Yaml y = new Yaml();
		FileWriter w = new FileWriter(filename);
		y.dumpAll( users.iterator(), w );
	}

/** Loads users from a previously {@link #save}d list and adds them to this list.
@param filename The file containing the previously {@link #save}d {@link UserList}
@throws java.io.FileNotFoundException If the file is not found
*/	public int load( String filename ) throws java.io.FileNotFoundException {
	Yaml y = new Yaml();
	Iterable add = y.loadAll( new FileReader(filename) );
	int r=0;
	for( Object u : add ) { r++; users.add( (User) u ); }
	return r;
	}


	private ArrayList<User> users;
}

