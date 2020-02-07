package Main;

import Utility.ButtonTabComponent;
import Utility.OSValidator;
import Utility.TabTitleEditListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static Main.GPA_CALC.GPA_Scale;

public class Frame extends JFrame {

    public static final int APP_WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2.1);
    public static final int APP_HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 1.1);
    public static final String APP_TITLE = "Course Timetable";
    public static final String NEW_TAB = "New Tab";
    public static final String CLOSE_TAB = "Close Tab";
    public static final String GPA_CAL = "GPA Calculator";
    public static final String SETTINGS = "Settings";
    public static final String CHANGE = "Change Log";
    public static final String EXPORT = "Save Tab Data";
    public static final String IMPORT = "Load Tab Data";
    public static final String EXIT = "Exit";
    public static final String PRINT = "Print";
    public static final String FILE_PATH = System.getProperty("user.dir") + "\\";
    public static JMenuItem ExportTab = new JMenuItem(EXPORT);
    public static JMenuItem ImportTab = new JMenuItem(IMPORT);
    public static JMenuItem PrintTab = new JMenuItem(PRINT);
    public static JTabbedPane tabbedPane = new JTabbedPane();
    public static List<JComponent> tabbedPaneList = new ArrayList<>();
    public static boolean importing = false;
    private final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    // ================================================================================
    // ================================================================================
    // ================================================================================
    public static void main(String[] args) {
        new Frame();
    }
    // ================================================================================
    // ================================================================================
    // ================================================================================

    // Initialize the main application frame
    public Frame() {
        // Set title of this application
        super(APP_TITLE);
        // Set the preferred frame size
        this.setPreferredSize(new Dimension(APP_WIDTH, APP_HEIGHT));
        // Set the minimum frame size
        this.setMinimumSize(new Dimension(APP_WIDTH / 3, APP_HEIGHT / 3));
        // Allow the user to resize the application frame
        this.setResizable(true);
        // Make sure the application terminates after exit
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Try to check for a saved setting file
        try {
            SettingsFrame.startUpCheck();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Remove all items items in the list in case it's not empty
        tabbedPane.removeAll();

        // Check for load save tab data option in the setting file
        if (SettingsFrame.saved_tab_load.isSelected()) {
            try {
                // Try to import the saved tabs
                Frame.importing = true;
                importData("startup");
                Frame.importing = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Initialize two default tabs if user does not wish to load saved tabs
            for (int i = 0; i < 2; i++) {
                String title = "Term " + (i + 1);
                Panel pane = new Panel();
                add_tab(pane, title);
            }
        }

        // Add listeners for this frame
        TabTitleEditListener l = new TabTitleEditListener(tabbedPane);
        tabbedPane.addChangeListener(l);
        tabbedPane.addMouseListener(l);

        // Set the tab layout
        tabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        // Make tabbedPane visible
        tabbedPane.setOpaque(true);
        // Select the first tab by default
        tabbedPane.setSelectedIndex(0);
        // No border, would spoil the looks
        tabbedPane.setBorder(null);
        // Set background color to match the setting
        tabbedPane.setBackground(SettingsFrame.themeColor);
        // Initialize the too menu bar
        SetMenu();
        // Add the main tab panel
        setContentPane(tabbedPane);
        // Compile
        pack();
        // Set the start position relative to the centre of the screen
        this.setLocationRelativeTo(null);
        // Set the application icon
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Timetable.png")));
        // Make sure the user can see the application
        setVisible(true);
    }

    // ================================================================================

    // Method to remove a tab at index `i` from the tabbedPane
    public static void remove_tab(int i) {
        // Remove the tab from both the list, and pane. if there is one to remove
        if (i >= 0) {
            tabbedPaneList.remove(i);
            tabbedPane.remove(i);
        }
    }

    // Method  to add tab(of type JComponent) to the main frame of title `title`
    public void add_tab(JComponent component, String title) {
        // Check for different types of component we are adding
        if (component.getClass().equals(Panel.class)) {
            // Cast component type as Panel for further use
            Panel pane = (Panel) component;
            // Create default Courses for pane
            Courses courses = new Courses(pane);
            // Create controller for `pane` and `courses`
            Controller controller = new Controller(pane, courses);
            // Set controller and courses for pane
            pane.setController(controller);
            pane.setCourse(courses);
            // Add newly created `pane` to the tabbedPaneList
            tabbedPaneList.add(pane);
            // Add newly created `pane` as a tab titled `title`
            tabbedPane.addTab(title, pane);
            // Add a close button to the newly created pane
            tabbedPane.setTabComponentAt(tabbedPaneList.size() - 1, new ButtonTabComponent(tabbedPane));
            // Set the newly created pane as the active tab
            tabbedPane.setSelectedIndex(tabbedPaneList.size() - 1);
            // Set the background for this pane
            tabbedPane.setBackgroundAt(tabbedPaneList.size() - 1, Color.WHITE);
            // Get the active pane on the tabbedPane
            Panel panel = (Panel) tabbedPane.getSelectedComponent();
            // If we are importing tab we don't need to update table
            if (!importing) {
                panel.getCourse().updateTable();
            }
        } else if (component.getClass().equals(GPA_CALC.class)) {
            // Cast the component as GPA_CALC
            GPA_CALC pane = (GPA_CALC) component;
            // Basic setup is the same as above
            tabbedPaneList.add(pane);
            tabbedPane.addTab(title, pane);
            tabbedPane.setTabComponentAt(tabbedPaneList.size() - 1, new ButtonTabComponent(tabbedPane));
            tabbedPane.setSelectedIndex(tabbedPaneList.size() - 1);
            tabbedPane.setBackgroundAt(tabbedPaneList.size() - 1, Color.WHITE);
        } else if (component.getClass().equals(Changelog.class)) {
            Changelog changelog = (Changelog) component;
            tabbedPaneList.add(changelog);
            tabbedPane.addTab(title, changelog);
            tabbedPane.setTabComponentAt(tabbedPaneList.size() - 1, new ButtonTabComponent(tabbedPane));
            tabbedPane.setSelectedIndex(tabbedPaneList.size() - 1);
            tabbedPane.setBackgroundAt(tabbedPaneList.size() - 1, Color.WHITE);
        }
    }

    // Method to set the menu bar and it's components for this frame
    private void SetMenu() {
        /// Add listener for menus
        ActionListener listener = new Listener(this);

        // Create a new menuBar
        JMenuBar menuBar = new JMenuBar();
        // Set the background and foreground color of menuBar to match the theme
        menuBar.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        menuBar.setBackground(SettingsFrame.themeColor);
        menuBar.setBorder(null);

        // ================================================================================

        // Create a new menu and set it's color to match the theme
        JMenu Option = new JMenu("Options");
        Option.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        Option.setBackground(SettingsFrame.themeColor);

        // Create a new menu item, titled `NEW_TAB`
        JMenuItem NewTab = new JMenuItem(NEW_TAB);
        // Set the action command for this menu item
        NewTab.setActionCommand(NEW_TAB);
        // Set the listener for this item
        NewTab.addActionListener(listener);
        // Add a new short cut key for this item
        NewTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, SHORTCUT_MASK));
        // Set the color to match the application theme
        NewTab.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        NewTab.setBackground(SettingsFrame.themeColor.brighter());

        // Setup the remaining menu items
        JMenuItem CloseTab = new JMenuItem(CLOSE_TAB);
        CloseTab.setActionCommand(CLOSE_TAB);
        CloseTab.addActionListener(listener);
        CloseTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, SHORTCUT_MASK));
        CloseTab.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        CloseTab.setBackground(SettingsFrame.themeColor.brighter());

        ExportTab.setActionCommand(EXPORT);
        ExportTab.addActionListener(listener);
        ExportTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SHORTCUT_MASK));
        ExportTab.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        ExportTab.setBackground(SettingsFrame.themeColor.brighter());

        ImportTab.setActionCommand(IMPORT);
        ImportTab.addActionListener(listener);
        ImportTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, SHORTCUT_MASK));
        ImportTab.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        ImportTab.setBackground(SettingsFrame.themeColor.brighter());

        JMenuItem Settings = new JMenuItem(SETTINGS);
        Settings.setActionCommand(SETTINGS);
        Settings.addActionListener(listener);
        Settings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, SHORTCUT_MASK));
        Settings.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        Settings.setBackground(SettingsFrame.themeColor.brighter());

        JMenuItem Exit = new JMenuItem(EXIT);
        Exit.setActionCommand(EXIT);
        Exit.addActionListener(listener);
        Exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
        Exit.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        Exit.setBackground(SettingsFrame.themeColor.brighter());

        // Add the menu items to the menu bar 
        Option.add(NewTab);
        Option.add(CloseTab);
        // TODO: implement following functions for Mac OS
        // if (OSValidator.isWindows()) {
            Option.add(ExportTab);
            Option.add(ImportTab);
            Option.addSeparator();
            Option.add(Settings);
        // }
        Option.addSeparator();
        Option.add(Exit);

        menuBar.add(Option);

        // ================================================================================

        // Create another menu and add the corresponding menu items
        JMenu Tools = new JMenu("Tools");
        Tools.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        Tools.setBackground(SettingsFrame.themeColor);

        JMenuItem GPA = new JMenuItem(GPA_CAL);
        GPA.setActionCommand(GPA_CAL);
        GPA.addActionListener(listener);
        GPA.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, SHORTCUT_MASK));
        GPA.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        GPA.setBackground(SettingsFrame.themeColor.brighter());

        PrintTab.setActionCommand(PRINT);
        PrintTab.addActionListener(listener);
        PrintTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, SHORTCUT_MASK));
        PrintTab.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        PrintTab.setBackground(SettingsFrame.themeColor.brighter());

        Tools.add(GPA);
        Tools.add(PrintTab);

        menuBar.add(Tools);

        // ================================================================================

        // Create another menu and add the corresponding menu item
        JMenu About = new JMenu("About");
        About.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        About.setBackground(SettingsFrame.themeColor);

        JMenuItem Change = new JMenuItem(CHANGE);
        Change.setActionCommand(CHANGE);
        Change.addActionListener(listener);
        Change.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, SHORTCUT_MASK));
        Change.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        Change.setBackground(SettingsFrame.themeColor.brighter());

        About.add(Change);

        menuBar.add(About);

        // ================================================================================

        // Add the menu bar to the frame
        setJMenuBar(menuBar);
    }

    // ================================================================================
    // ================================================================================

    // Method to export the current active tab as a `.txt` file
    public void exportData(String FILE_PATH) {
        Boolean result = false;
        // For windows only
        BufferedWriter writer = null;
        File file = new File(FILE_PATH);
        // Check for existed file
        if (file.exists()) {
            if (SettingsFrame.sound_effect_check.isSelected()) {
                Toolkit.getDefaultToolkit().beep();
            }
            if (JOptionPane.showConfirmDialog(this, "File exists, Override?", "Please Confirm", JOptionPane.YES_NO_OPTION) == 1) {
                return;
            }
        }
        // Check for different types of tab data exporting
        if (tabbedPaneList.get(tabbedPane.getSelectedIndex()).getClass().equals(GPA_CALC.class)) {
            try {
                // Check for export precondition
                if (GPA_CALC.selected_index.equals("Select Scale")) {
                    if (SettingsFrame.sound_effect_check.isSelected()) {
                        Toolkit.getDefaultToolkit().beep();
                    }
                    JOptionPane.showMessageDialog(this, "Please select a scale", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (GPA_CALC.selected_index.equals("Custom Scale")) {
                    if (SettingsFrame.sound_effect_check.isSelected()) {
                        Toolkit.getDefaultToolkit().beep();
                    }
                    JOptionPane.showMessageDialog(this, "Sorry, exporting custom scale is currently not supported", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Precondition passed, preceded to export file
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    writer = new BufferedWriter(new FileWriter(file));
                    // Write on the first line of the output file:
                    // `APP_TITLE` to serve as an identifier for the setting file, followed by
                    // `||` as a separator, current tab pane title
                    writer.write(APP_TITLE + "||" + tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
                    // Write a new line, /br , then followed by the selected index?
                    writer.newLine();
                    writer.write(GPA_CALC.selected_index);
                    writer.newLine();
                    // Update grades
                    GPA_CALC gpa_calc = (GPA_CALC) tabbedPaneList.get(tabbedPane.getSelectedIndex());
                    gpa_calc.CalcGPA();
                    // Iteratively go through the course list, write the necessary information separated by `---`
                    for (int i = 0; i < GPA_CALC.courseList.size(); i++) {
                        GPA_CALC.GPA_Course course = GPA_CALC.courseList.get(i);
                        writer.write(course.getName() + "---" + course.getCredits() + "---" + course.getGrade());
                        writer.newLine();
                    }
                    result = true;
                }
                // Exception handler in case we cannot write file
            } catch (IOException ex) {
            } finally {
                try {
                    writer.close();
                } catch (Exception ex) {
                }
            }
        } else {
            // The case we are outputting course panel
            try {
                // Same procedure as above
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(APP_TITLE + "|" + tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
                writer.newLine();
                for (int i = 0; i < Courses.getCourseList().size(); i++) {
                    Courses course = Courses.getCourseList().get(i);
                    writer.write(course.getName() + "---" + course.getType() + "---" + course.getDay() + "---" + course.getStartTime() + "---" + course.getDuration() + "---" + course.
                            getLocation() + "---" + course.getInstructor() + "---" + course.getSection() + "---" + course.getCredits() + "---" + course.getNote() + "---" + course.getColor().getRGB());
                    writer.newLine();
                }
                result = true;
            } catch (IOException ex) {
            } finally {
                try {
                    writer.close();
                } catch (Exception ex) {
                }
            }
        }
        // Display the output result message
        if (result) {
            Object message = "\"" + tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()) + "\" was successfully exported to " + FILE_PATH;
            if (SettingsFrame.sound_effect_check.isSelected()) {
                Toolkit.getDefaultToolkit().beep();
            }
            JOptionPane.showMessageDialog(this, message, "Export Data", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Object message = "Export failed";
            if (SettingsFrame.sound_effect_check.isSelected()) {
                Toolkit.getDefaultToolkit().beep();
            }
            JOptionPane.showMessageDialog(this, message, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to import all saved tab files in the same folder as this application
    public void importData(String mode) throws Exception {
        File folder = new File(FILE_PATH);
        File[] listOfFiles = folder.listFiles();
        BufferedReader reader = null;
        int check = 0;
        // Get all the files in the current folder
        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];

            // Get the files with `txt` extension
            if (file.isFile() && file.getName().endsWith(".txt")) {
                reader = new BufferedReader(new FileReader(file));
                String s = reader.readLine();
                // Check for the identifier `APP_TITLE` in the file
                if ((s.contains(APP_TITLE))) {
                    // if s contains `||` then it is the type of GPA_CALC
                    if (s.contains("||")) {
                        // Retrieve the title and the selected scale table index
                        String title = s.substring(APP_TITLE.length() + 2);
                        String scale_index = reader.readLine();
                        // Create a new GPA_CALC tab and add it to the main tabbedPane
                        GPA_CALC gpa_CALC = new GPA_CALC();
                        add_tab(gpa_CALC, title);
                        // Set the saved value accordingly
                        GPA_CALC.Coursetable coursetable = gpa_CALC.getCoursetable();

                        JComboBox<String> GPAScaleSelect = new JComboBox<>(GPA_Scale);
                        GPAScaleSelect.setSelectedItem(scale_index);
                        GPAScaleSelect.setActionCommand("GPAScale");

                        gpa_CALC.actionPerformed(new ActionEvent(GPAScaleSelect, 0, "GPAScale"));

                        // Read and set tab data, which each data separated by `---`
                        while ((s = reader.readLine()) != null) {
                            String[] data = s.split("---");
                            GPA_CALC.GPA_Course course = new GPA_CALC.GPA_Course(data[0], Double.parseDouble(data[1]), data[2]);
                            coursetable.addRow(course);
                        }
                        for (int j = 0; j < 3; j++) {
                            coursetable.removeRow(0);
                        }
                    } else {
                        // The case when data type is Course
                        String title = s.substring(APP_TITLE.length() + 1);
                        Panel panel = new Panel();
                        add_tab(panel, title);
                        while ((s = reader.readLine()) != null) {
                            String[] data = s.split("---");
                            Color color = new Color(Integer.parseInt(data[10]));
                            Courses course = new Courses(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], Double.parseDouble(data[8]), data[9], color);
                            course = panel.getCourse().validate_course(course);
                            panel.getCourse().add_course(course);
                            panel.getCourse().add_row(course);
                        }
                        panel.getCourse().updateTable();
                    }

                    check = 1;
                    if (mode.equals("normal")) {
                        Object message = "\"" + tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()) + "\" was successfully imported";
                        if (SettingsFrame.sound_effect_check.isSelected()) {
                            Toolkit.getDefaultToolkit().beep();
                        }
                        JOptionPane.showMessageDialog(this, message, "Import Data", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }
        // when user click load tab in application
        if (check == 0 && mode.equals("normal")) {
            Object message = "No data file found, please place saved data files and this program in the same directory";
            if (SettingsFrame.sound_effect_check.isSelected()) {
                Toolkit.getDefaultToolkit().beep();
            }
            JOptionPane.showMessageDialog(this, message, "Import Data", JOptionPane.ERROR_MESSAGE);
        } else if (check == 0 && mode.equals("startup")) {
            // start up load procedure
            for (int i = 0; i < 2; i++) {
                String title = "Term " + (i + 1);
                Panel pane = new Panel();
                add_tab(pane, title);
            }
        }
    }

    // Method to check if the tab data has been saved
    public boolean checkSaved() {
        File file = new File(FILE_PATH);
        // Check for existed file
        if (file.exists()) {

        }
        else {
            return false;
        }

    }
}
