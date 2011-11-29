/*
 * UReadApp.java
 */

package uRead;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * The main class of the application.
 */
public class UReadApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
		// Load DataBase and UserList
		File resourcePath;
		try {
			//resourcePath = new File( getClass().getResource("").toURI().getPath() );
			resourcePath = new File( getClass().getResource("").toURI().getPath(), "resources" );
			userFile = new File( resourcePath, "UserList.yaml" ).toString();
			bookFile = new File( resourcePath, "BookDatabase.yaml" ).toString();
		} catch( java.net.URISyntaxException oops ) {
			JOptionPane.showMessageDialog( null, "Java library error handling "+getClass().getResource(""),
                                     "uLearn: Fatal Error!",
									 JOptionPane.ERROR_MESSAGE );
			exit();
		}
		try {
			ul = new UserList( userFile );
		} catch( java.io.FileNotFoundException oops ) {
			JOptionPane.showMessageDialog( null, "Creating default User List - Unable to load from "+userFile,
                                     "uLearn - Error!",
									 JOptionPane.ERROR_MESSAGE );
			ul = new UserList();
			ul.add( new User( "iWork", "kRowi", UserList.U_EMPLOYEE ) );
			ul.add( new User( "uRead", "dAeru", UserList.U_ADMINISTRATOR ) );
		}
		try {
			db = new BookDatabase( bookFile );
		} catch( java.io.FileNotFoundException oops ) {
			JOptionPane.showMessageDialog( null, "Creating empty Book Database - Unable to load from "+bookFile,
                                     "uLearn - Error!",
                                     JOptionPane.ERROR_MESSAGE );
			db = new BookDatabase();
		}
		// Setup onExit procedure to save DataBase and UserList
		// from http://stackoverflow.com/questions/2467070/onexit-event-for-a-swing-application
		Runtime.getRuntime().addShutdownHook( new Thread() {
			@Override public void run() { save(); }
		} );
		// Finally, start the UI
        show(new UReadView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of UReadApp
     */
    public static UReadApp getApplication() {
        return Application.getInstance(UReadApp.class);
    }
	
	/* Save Database and Userlist */
	private void save() {
		try {
			ul.save( userFile );
		} catch( java.io.IOException oops ) {
			JOptionPane.showMessageDialog( null, "Failed to update User List - Unable to save to "+userFile,
							 "uLearn - Error!",
							 JOptionPane.ERROR_MESSAGE );
		}
		try {
			db.save( bookFile );
		} catch( java.io.IOException oops ) {
			JOptionPane.showMessageDialog( null, "Failed to update Book Database - Unable to save to "+bookFile,
							 "uLearn - Error!",
							 JOptionPane.ERROR_MESSAGE );
		}
	}
	/* Search database (called from UReadView) */
	SearchResults search( int fields, String query ) { return db.search( fields, query ); }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(UReadApp.class, args);
    }

	private String bookFile;
	private String userFile;
	private BookDatabase db;
	private UserList ul;

}
