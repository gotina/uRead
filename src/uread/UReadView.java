/*
 * UReadView.java
 */
package uRead;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.print.PrinterException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * The application's main frame.
 */
public class UReadView extends FrameView {

    public UReadView(SingleFrameApplication app) {
        super(app);

        initComponents();



        this.btnAddBook.setVisible(false);
        this.btnRemove.setVisible(false);
        this.btnEdit.setVisible(false);
        this.btnLogout.setVisible(false);

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void chooseField() {
        int f = this.cbxSearchCriteria.getSelectedIndex(); //Better keep this in sync...
        switch (f) {
            case 1:
                field = BookDatabase.S_TITLE;
                break;
            case 2:
                field = BookDatabase.S_AUTHOR;
                break;
            case 3:
                field = BookDatabase.S_ISBN;
                break;
            case 4:
                field = BookDatabase.S_DESCRIPTION;
                break;
            case 5:
                field = BookDatabase.S_LOCATION;
                break;
            default:
                field = -1;
                break;
        }
        System.out.println("Selected field " + field + " (menu item " + f + ")");
    }
    SearchResults results;

    @Action
    public void searchBooks() {
        String bookText = this.txtSearchField.getText();
        results = UReadApp.getApplication().search(-1, bookText); //hopefully -1 will match all fields
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
        tableModel.setRowCount(0);

        int i = 0;
        for (SearchResult r : results.toList()) {
            tableModel.insertRow(i++, new Object[]{r.book().getTitle(), r.book().getAuthor(), r.book().getYear(), r.book().getISBN10(), r.book().getISBN13(), r.book().getDescription(), r.book().getLocation()});
        }

    }
    JTable table = new JTable();
    DefaultTableModel myListModel = (DefaultTableModel) table.getModel();

    
    @Action
    public void addBooks() {
        JFrame frame = new JFrame();
        frame.setBounds(250, 250, 500, 500);


        frame.add(new AddBook());

        frame.setVisible(true);
    }
    
    
    @Action
    public void Logout(){
        this.btnAddBook.setVisible(false);
        this.btnRemove.setVisible(false);
        this.btnEdit.setVisible(false);
        this.btnLogout.setVisible(false);
        this.txtUserName.setVisible(true);
        this.txtPassword.setVisible(true);
        this.btnLogin.setVisible(true);
    }
    
    @Action
    public void myList() {
        JFrame frame = new JFrame();
        frame.setBounds(250, 250, 500, 500);
        Container content = frame.getContentPane();
        content.setBackground(Color.white);
        content.setLayout(new FlowLayout());

        if (table.getColumnCount() == 0) {
            myListModel.addColumn("Title");
            myListModel.addColumn("Author");
            myListModel.addColumn("Year");
            myListModel.addColumn("ISBN10");
            myListModel.addColumn("ISBN13");
            myListModel.addColumn("Description");
            myListModel.addColumn("Location");
        }
        JButton button1 = new JButton("print this list");
        JButton button2 = new JButton("clear this list");
        button1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    table.print();
                } catch (PrinterException ex) {
                    Logger.getLogger(UReadView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                myListModel.setRowCount(0);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        content.add(scrollPane);
        content.add(button1);
        content.add(button2);

        frame.setVisible(true);
    }


    @Action
    public void addToList() {
        for (int r : this.jTable1.getSelectedRows()) {
            int i = 0;
            myListModel.insertRow(i++, new Object[]{results.get(r).book().getTitle(), results.get(r).book().getAuthor(), results.get(r).book().getYear(), results.get(r).book().getISBN10(), results.get(r).book().getISBN13(), results.get(r).book().getDescription(), results.get(r).book().getLocation()});
        }
    }
    
    @Action
    public void RemoveBook() {
        for (int r : this.jTable1.getSelectedRows()) {
        
        System.out.println(r);    
            UReadApp.getApplication().removeBook( UReadApp.getApplication().resultsEntry(r).book() );
            DefaultTableModel newModel = (DefaultTableModel) this.jTable1.getModel();
            newModel.removeRow(r);
            //int i = 0;
            //myListModel.insertRow(i++, new Object[]{results.get(r).book().getTitle(), results.get(r).book().getAuthor(), results.get(r).book().getYear(), results.get(r).book().getISBN10(), results.get(r).book().getISBN13(), results.get(r).book().getDescription(), results.get(r).book().getLocation()});
        }
    }
    @Action
    public void login() {
        String username = this.txtUserName.getText();
        char[] password = this.txtPassword.getPassword();
        if (username.equals("iWork")) {
            this.btnLogin.setVisible(false);
            this.btnLogout.setVisible(true);
            this.btnAddBook.setVisible(true);
            this.btnRemove.setVisible(true);
            this.txtUserName.setVisible(false);
            this.txtPassword.setVisible(false);
        }
    }

    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = UReadApp.getApplication().getMainFrame();
            aboutBox = new UReadAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        UReadApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        txtSearchField = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        cbxSearchCriteria = new javax.swing.JComboBox();
        btnLogin = new javax.swing.JButton();
        btnMyList = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        spnSearchResults = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        btnAddBook = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();
        txtUserName = new javax.swing.JTextField();
        btnRemove = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        jFrame1 = new javax.swing.JFrame();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(uRead.UReadApp.class).getContext().getResourceMap(UReadView.class);
        mainPanel.setBackground(resourceMap.getColor("mainPanel.background")); // NOI18N
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(900, 500));
        mainPanel.setSize(new java.awt.Dimension(900, 500));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtSearchField.setText(resourceMap.getString("txtSearchField.text")); // NOI18N
        txtSearchField.setName("txtSearchField"); // NOI18N
        mainPanel.add(txtSearchField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 320, 530, -1));

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(uRead.UReadApp.class).getContext().getActionMap(UReadView.class, this);
        btnSearch.setAction(actionMap.get("searchBooks")); // NOI18N
        btnSearch.setText(resourceMap.getString("btnSearch.text")); // NOI18N
        btnSearch.setName("btnSearch"); // NOI18N
        mainPanel.add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 320, -1, -1));

        cbxSearchCriteria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "(Match Any)", "Title", "Author", "ISBN", "Description", "Location" }));
        cbxSearchCriteria.setAction(actionMap.get("chooseField")); // NOI18N
        cbxSearchCriteria.setName("cbxSearchCriteria"); // NOI18N
        mainPanel.add(cbxSearchCriteria, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 164, -1));

        btnLogin.setAction(actionMap.get("login")); // NOI18N
        btnLogin.setText(resourceMap.getString("btnLogin.text")); // NOI18N
        btnLogin.setName("btnLogin"); // NOI18N
        mainPanel.add(btnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 20, -1, -1));

        btnMyList.setAction(actionMap.get("myList")); // NOI18N
        btnMyList.setText(resourceMap.getString("btnMyList.text")); // NOI18N
        btnMyList.setName("btnMyList"); // NOI18N
        mainPanel.add(btnMyList, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 21, -1, -1));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        mainPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 90, -1, -1));

        spnSearchResults.setBorder(null);
        spnSearchResults.setViewportBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        spnSearchResults.setAutoscrolls(true);
        spnSearchResults.setName("spnSearchResults"); // NOI18N

        jTable1.setForeground(resourceMap.getColor("jTable1.foreground")); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Title", "Author", "Year", "ISBN10", "ISBN13", "Description", "Location"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setName("jTable1"); // NOI18N
        spnSearchResults.setViewportView(jTable1);

        mainPanel.add(spnSearchResults, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 360, 780, 216));

        jButton1.setAction(actionMap.get("addToList")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        mainPanel.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 580, -1, -1));

        btnAddBook.setAction(actionMap.get("addBooks")); // NOI18N
        btnAddBook.setText(resourceMap.getString("btnAddBook.text")); // NOI18N
        btnAddBook.setName("btnAddBook"); // NOI18N
        mainPanel.add(btnAddBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 21, -1, -1));

        txtPassword.setText(resourceMap.getString("txtPassword.text")); // NOI18N
        txtPassword.setName("txtPassword"); // NOI18N
        mainPanel.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 20, 117, -1));

        txtUserName.setText(resourceMap.getString("txtUserName.text")); // NOI18N
        txtUserName.setName("txtUserName"); // NOI18N
        mainPanel.add(txtUserName, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 121, -1));

        btnRemove.setAction(actionMap.get("RemoveBook")); // NOI18N
        btnRemove.setText(resourceMap.getString("btnRemove.text")); // NOI18N
        btnRemove.setName("btnRemove"); // NOI18N
        mainPanel.add(btnRemove, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 580, -1, -1));

        btnEdit.setText(resourceMap.getString("btnEdit.text")); // NOI18N
        btnEdit.setName("btnEdit"); // NOI18N
        mainPanel.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 580, -1, -1));

        btnLogout.setAction(actionMap.get("Logout")); // NOI18N
        btnLogout.setText(resourceMap.getString("btnLogout.text")); // NOI18N
        btnLogout.setName("btnLogout"); // NOI18N
        mainPanel.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 20, -1, -1));

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 849, Short.MAX_VALUE)
            .add(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(statusMessageLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 653, Short.MAX_VALUE)
                .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelLayout.createSequentialGroup()
                .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(statusMessageLabel)
                    .add(statusAnimationLabel)
                    .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(3, 3, 3))
        );

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jFrame1.setName("jFrame1"); // NOI18N

        org.jdesktop.layout.GroupLayout jFrame1Layout = new org.jdesktop.layout.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddBook;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnMyList;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox cbxSearchCriteria;
    private javax.swing.JButton jButton1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JScrollPane spnSearchResults;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtSearchField;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private int field; // the search field selector
}
