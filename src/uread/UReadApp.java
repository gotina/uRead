/*
 * UReadApp.java
 */

package uRead;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import java.io.File;
import javax.swing.JOptionPane;

/**
 * The main class of the application.
 */
public class UReadApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
		File resourcePath;
		try {
			//resourcePath = new File( getClass().getResource("").toURI().getPath() );
			resourcePath = new File( getClass().getResource("").toURI().getPath(), "resources" );
			bookFile = new File( resourcePath, "BookDatabase.yaml" ).toString();
			userFile = new File( resourcePath, "UserList.yaml" ).toString();
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
