package Main;

import Utility.ButtonTabComponent;
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

    public Frame() {
        super(APP_TITLE);
        setPreferredSize(new Dimension(APP_WIDTH, APP_HEIGHT));
        this.setMinimumSize(new Dimension(APP_WIDTH / 3, APP_HEIGHT / 3));
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            SettingsFrame.startUpCheck();
        } catch (Exception e) {
            e.printStackTrace();
        }

        tabbedPane.removeAll();

        if (SettingsFrame.saved_tab_load.isSelected()) {
            try {
                Frame.importing = true;
                importData("startup");
                Frame.importing = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            for (int i = 0; i < 2; i++) {
                String title = "Term " + (i + 1);
                Panel pane = new Panel();
                add_tab(pane, title);
            }
        }

        TabTitleEditListener l = new TabTitleEditListener(tabbedPane);
        tabbedPane.addChangeListener(l);
        tabbedPane.addMouseListener(l);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        tabbedPane.setOpaque(true);
        tabbedPane.setSelectedIndex(0);

        tabbedPane.setBorder(null);
        tabbedPane.setBackground(SettingsFrame.themeColor);

        SetMenu();

        setContentPane(tabbedPane);
        pack();
        this.setLocationRelativeTo(null);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Timetable.png")));
        setVisible(true);
    }

    // ================================================================================

    public void add_tab(JComponent component, String title) {
        if (component.getClass().equals(Panel.class)) {
            Panel pane = (Panel) component;
            Courses courses = new Courses(pane);
            Controller controller = new Controller(pane, courses);
            pane.setController(controller);
            pane.setCourse(courses);
            tabbedPaneList.add(pane);
            tabbedPane.addTab(title, pane);
            tabbedPane.setTabComponentAt(tabbedPaneList.size() - 1, new ButtonTabComponent(tabbedPane));
            tabbedPane.setSelectedIndex(tabbedPaneList.size() - 1);
            tabbedPane.setBackgroundAt(tabbedPaneList.size() - 1, Color.WHITE);
            Panel panel = (Panel) tabbedPane.getSelectedComponent();
            if (!importing) {
                panel.getCourse().updateTable();
            }
        } else if (component.getClass().equals(GPA_CALC.class)) {
            GPA_CALC pane = (GPA_CALC) component;
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

    public static void remove_tab(int i) {
        if (i >= 0) {
            tabbedPaneList.remove(i);
            tabbedPane.remove(i);
        }
    }

    private void SetMenu() {
        ActionListener listener = new Listener(this);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        menuBar.setBackground(SettingsFrame.themeColor);
        menuBar.setBorder(null);

        // ================================================================================

        JMenu Option = new JMenu("Options");
        Option.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        Option.setBackground(SettingsFrame.themeColor);

        JMenuItem NewTab = new JMenuItem(NEW_TAB);
        NewTab.setActionCommand(NEW_TAB);
        NewTab.addActionListener(listener);
        NewTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, SHORTCUT_MASK));
        NewTab.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        NewTab.setBackground(SettingsFrame.themeColor.brighter());

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

        Option.add(NewTab);
        Option.add(CloseTab);
        Option.add(ExportTab);
        Option.add(ImportTab);
        Option.addSeparator();
        Option.add(Settings);
        Option.addSeparator();
        Option.add(Exit);

        menuBar.add(Option);

        // ================================================================================

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

        setJMenuBar(menuBar);
    }

    // ================================================================================
    // ================================================================================

    public void exportData(String FILE_PATH) {
        // TODO: Detect OS
        // For windowns only
        BufferedWriter writer = null;
        File file = new File(FILE_PATH);
        if (file.exists()) {
            if (SettingsFrame.sound_effect_check.isSelected()) {
                Toolkit.getDefaultToolkit().beep();
            }
            if (JOptionPane.showConfirmDialog(this, "File exists, Override?", "Please Confirm", JOptionPane.YES_NO_OPTION) == 1) {
                return;
            }
        }
        if (tabbedPaneList.get(tabbedPane.getSelectedIndex()).getClass().equals(GPA_CALC.class)) {
            try {
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
                } else if (!GPA_CALC.checkCourses()) {
                    if (SettingsFrame.sound_effect_check.isSelected()) {
                        Toolkit.getDefaultToolkit().beep();
                    }
                    JOptionPane.showMessageDialog(this, "Please finish empty grades and calculate before exporting", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    writer = new BufferedWriter(new FileWriter(file));
                    writer.write(APP_TITLE + "||" + tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
                    writer.newLine();
                    writer.write(GPA_CALC.selected_index);
                    writer.newLine();
                    for (int i = 0; i < GPA_CALC.courseList.size(); i++) {
                        GPA_CALC.GPA_Course course = GPA_CALC.courseList.get(i);
                        writer.write(course.getName() + "---" + course.getCredits() + "---" + course.getGrade());
                        writer.newLine();
                    }
                    Object message = "\"" + tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()) + "\" was successfully exported to " + FILE_PATH;
                    if (SettingsFrame.sound_effect_check.isSelected()) {
                        Toolkit.getDefaultToolkit().beep();
                    }
                    JOptionPane.showMessageDialog(this, message, "Export Data", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException ex) {
            } finally {
                try {
                    writer.close();
                } catch (Exception ex) {
                }
            }
        } else {
            try {
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(APP_TITLE + "|" + tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
                writer.newLine();
                for (int i = 0; i < Courses.getCourseList().size(); i++) {
                    Courses course = Courses.getCourseList().get(i);
                    writer.write(course.getName() + "---" + course.getType() + "---" + course.getDay() + "---" + course.getStartTime() + "---" + course.getDuration() + "---" + course.
                            getLocation() + "---" + course.getCat() + "---" + course.getInstructor() + "---" + course.getSection() + "---" + course.getCredits() + "---" + course.getColor().getRGB());
                    writer.newLine();
                }
                Object message = "\"" + tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()) + "\" was successfully exported to " + FILE_PATH;
                if (SettingsFrame.sound_effect_check.isSelected()) {
                    Toolkit.getDefaultToolkit().beep();
                }
                JOptionPane.showMessageDialog(this, message, "Export Data", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
            } finally {
                try {
                    writer.close();
                } catch (Exception ex) {
                }
            }
        }

    }

    public void importData(String mode) throws Exception {
        File folder = new File(FILE_PATH);
        File[] listOfFiles = folder.listFiles();
        BufferedReader reader = null;
        int check = 0;
        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if (file.isFile() && file.getName().endsWith(".txt")) {
                reader = new BufferedReader(new FileReader(file));
                String s = reader.readLine();
                if ((s.contains(APP_TITLE))) {
                    if (s.contains("||")) {
                        String title = s.substring(APP_TITLE.length() + 2);
                        String scale_index = reader.readLine();

                        GPA_CALC gpa_CALC = new GPA_CALC();
                        add_tab(gpa_CALC, title);

                        GPA_CALC.Coursetable coursetable = gpa_CALC.getCoursetable();

                        JComboBox<String> GPAScaleSelect = new JComboBox<>(GPA_Scale);
                        GPAScaleSelect.setSelectedItem(scale_index);
                        GPAScaleSelect.setActionCommand("GPAScale");

                        gpa_CALC.actionPerformed(new ActionEvent(GPAScaleSelect, 0, "GPAScale"));

                        while ((s = reader.readLine()) != null) {
                            String[] data = s.split("---");
                            GPA_CALC.GPA_Course course = new GPA_CALC.GPA_Course(data[0], Double.parseDouble(data[1]), data[2]);
                            coursetable.addRow(course);
                        }
                        for (int j = 0; j < 3; j++) {
                            coursetable.removeRow(0);
                        }
                    } else {
                        String title = s.substring(APP_TITLE.length() + 1);
                        Panel panel = new Panel();
                        add_tab(panel, title);
                        while ((s = reader.readLine()) != null) {
                            String[] data = s.split("---");
                            Color color = new Color(Integer.parseInt(data[10]));
                            Courses course = new Courses(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], Double.parseDouble(data[9]), color);
                            course = panel.getCourse().revalidate(course);
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
        if (check == 0 && mode.equals("normal")) {
            Object message = "No data file found, please place saved data files and this program in the same directory";
            if (SettingsFrame.sound_effect_check.isSelected()) {
                Toolkit.getDefaultToolkit().beep();
            }
            JOptionPane.showMessageDialog(this, message, "Import Data", JOptionPane.ERROR_MESSAGE);
        } else if (check == 0 && mode.equals("startup")) {
            for (int i = 0; i < 2; i++) {
                String title = "Term " + (i + 1);
                Panel pane = new Panel();
                add_tab(pane, title);
            }
        }
    }
}
