
package uRead;

import java.lang.String;
import java.util.Random; //for password hashing
import java.math.BigInteger; //for password hashing
import java.security.spec.KeySpec; //for password hashing
import javax.crypto.spec.PBEKeySpec; //for password hashing
import javax.crypto.SecretKeyFactory; //for password hashing

/** Represents a particular user */
public class User {

/** Empty constructor creates a {@link User} with no properties.  You shouldn't use this.
  * If used, follow immediately by setters for name and type, and changePassword.
*/	public User() { name = hash = null; salt = null; type = -1; }

/** Normal constructor, creates a {@link User} with all fields set.
@param username The username for this {@link User}
@param password The password for this {@link User}
@param type The type of user this is - one of {@link UserList#U_PATRON}, {@link UserList#U_EMPLOYEE}, or {@link UserList#U_ADMINISTRATOR}.
@throws RuntimeException If you have an obsolete version of Java
*/	public User( String username, String password, int newtype )
	{ setName( username ); changePassword( password ); setType( newtype ); }

/** Returns this {@link User}'s username
@return The user name of this {@link User}
*/	public String getName() { return name; }

/** Replaces the username of this {@link User}.
@param newname The new username for this {@link User}
*/	public void setName( String newname ) { name = newname; }

/** Returns the int representing this {@link User}s type, which determines their privilege level.
  * One of {@link UserList#U_PATRON}, {@link UserList#U_EMPLOYEE},
  * or {@link UserList#U_ADMINISTRATOR}.
@return One of {@link UserList#U_PATRON}, {@link UserList#U_EMPLOYEE}, or {@link UserList#U_ADMINISTRATOR}.
*/	public int getType() { return type; }

/** Replaces this {@link User}s type, which changes their privilege level.
@param newtype the new type of this {@link User} - one of {@link UserList#U_PATRON}, {@link UserList#U_EMPLOYEE}, or {@link UserList#U_ADMINISTRATOR}
*/	public void setType( int newtype ) { type = newtype; }

/** Tests weather the provided password is valid to authenticate this {@link User}
@param password The password to test
@return True if the password is valid, false if it is not.
@throws RuntimeException If you have an obsolete version of Java
*/	public boolean checkPassword( String password )
	{
		KeySpec spec = new PBEKeySpec( password.toCharArray(), salt, 2048, 160 );
		SecretKeyFactory f;
		byte[] raw;
		try { f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); }
		catch( java.security.NoSuchAlgorithmException oops )
		{ throw new RuntimeException( "Java library error generating password hash: No Such Algorithm \"PBKDF2WithHmacSHA1\"" ); }
		try { raw = f.generateSecret(spec).getEncoded(); }
		catch( java.security.spec.InvalidKeySpecException oops )
		{ throw new RuntimeException( "Java library error generating password hash: Invalid Key Spec generated from password" ); }
		String test = new BigInteger(1, raw).toString(16);
		return hash.equals( test );
	}

/** Changes the password accepted to authenticate this user
@param password The new password
@throws RuntimeException If you have an obsolete version of Java
*/	public void changePassword( String password )
	{
		salt = new byte[16];
		new Random().nextBytes(salt);
		KeySpec spec = new PBEKeySpec( password.toCharArray(), salt, 2048, 160 );
		SecretKeyFactory f;
		byte[] raw;
		try { f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); }
		catch( java.security.NoSuchAlgorithmException oops )
		{ throw new RuntimeException( "Java library error generating password hash: No Such Algorithm \"PBKDF2WithHmacSHA1\"" ); }
		try { raw = f.generateSecret(spec).getEncoded(); }
		catch( java.security.spec.InvalidKeySpecException oops )
		{ throw new RuntimeException( "Java library error generating password hash: Invalid Key Spec generated from password" ); }
		hash = new BigInteger(1, raw).toString(16);
	}

/** Returns this {@link User}'s password hash.  You should use checkPassword.
@return The password hash of this {@link User}
*/	public String getHash() { return hash; }

/** Replaces the password hash of this {@link User}.  You should use changePassword.
@param newhash The new password hash for this {@link User}
*/	public void setHash( String newhash ) { hash = newhash; }

/** Returns this {@link User}'s password salt.  You should use checkPassword.
@return The password salt of this {@link User}
*/	public byte[] getSalt() { return salt; }

/** Replaces the password salt of this {@link User}.  You should use changePassword.
@param newhash The new password salt for this {@link User}
*/	public void setSalt( byte[] newsalt ) { salt = newsalt; }

/** Generate a multiline string representation of this {@link Book}.
@return A multiline string representation of this {@link Book}
*/	public String toString() {
		String s = "";
		if( null != name ) { s = "name: "+getName()+"\n"; }
		if( null != hash ) { s += "hash: "+getHash()+"\n"; }
		if( null != salt ) { s += "salt: "+getSalt()+"\n"; }
		if( 0 != type ) { s += "type: "+getType()+"\n"; }
		return s;
	}

	private String name;
	private String hash; //of the password
	private byte[] salt; //used for the hash
	private int type;
}

