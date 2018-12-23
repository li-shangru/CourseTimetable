package Main;


import Utility.ColorEditor;
import Utility.ColorRenderer;
import Utility.CustomTableCellRenderer;
import Utility.NumEditor;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;


public class Panel extends JPanel implements ActionListener {

    private Coursetable courseTable = new Coursetable();
    private JTable timetable = new JTable(new Timetable());
    private JTable coursetable = new JTable(courseTable);
    private JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private Controller controller;
    private Courses courses;

    // ################################################################################
    // ################################################################################
    // ################################################################################
    // ================================================================================

    public Panel() {
        super(new BorderLayout());

        add(splitPane, BorderLayout.CENTER);

        TableSetUp(timetable, "Timetable");
        TableSetUp(coursetable, "Coursetable");
    }


    // ================================================================================

    private void TableSetUp(JTable table, String name) {
        table.setRowSelectionAllowed(false);
        table.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        table.setBackground(SettingsFrame.themeColor.brighter());

        if (name.equals("Timetable")) {
            JScrollPane timetable_scrollPane = new JScrollPane(timetable);
            JPanel topHalf = new JPanel(new BorderLayout());

            JTableHeader tableHeader = table.getTableHeader();
            DefaultTableCellRenderer centerRenderer = new CustomTableCellRenderer();

            tableHeader.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
            tableHeader.setBackground(SettingsFrame.themeColor);

            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            table.setRowHeight(Frame.APP_HEIGHT / 40);
            table.setDefaultRenderer(Object.class, centerRenderer);

            tableHeader.setPreferredSize(new Dimension(Frame.APP_WIDTH / 5, Frame.APP_HEIGHT / 30));
            tableHeader.setFont(new Font("Header", Font.BOLD, 14));
            tableHeader.setResizingAllowed(false);
            tableHeader.setReorderingAllowed(false);

            for (int i = 0; i < Timetable.columnNames.length; i++) {
                TableColumn column = table.getColumnModel().getColumn(i);
                if (i == 0) {
                    column.setPreferredWidth(Frame.APP_WIDTH / 6);
                } else {
                    column.setPreferredWidth(Frame.APP_WIDTH / 5);
                }
            }

            topHalf.add(timetable_scrollPane);
            topHalf.setMinimumSize(new Dimension(Frame.APP_WIDTH / 10, Frame.APP_HEIGHT / 10));
            topHalf.setPreferredSize(new Dimension(Frame.APP_WIDTH, (int) (timetable_scrollPane.getViewport().getViewSize().height + tableHeader.getPreferredSize().getHeight())));

            splitPane.add(topHalf, JSplitPane.TOP);
            splitPane.setDividerSize(5);
            splitPane.setDividerLocation((int) (timetable_scrollPane.getViewport().getViewSize().height + tableHeader.getPreferredSize().getHeight() + splitPane.getDividerSize() - 1));

            // ================================================================================

        } else if (name.equals("Coursetable")) {
            JScrollPane coursetable_scrollPane = new JScrollPane(coursetable);
            JPanel bottomHalf = new JPanel(new BorderLayout());

            JButton addCourseButton = new JButton("Add Course");
            addCourseButton.addActionListener(this);
            addCourseButton.setActionCommand("AddCourse");
            addCourseButton.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
            addCourseButton.setBackground(SettingsFrame.themeColor);

            JButton removeCourseButton = new JButton("Remove Course");
            removeCourseButton.addActionListener(this);
            removeCourseButton.setActionCommand("RemoveCourse");
            removeCourseButton.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
            removeCourseButton.setBackground(SettingsFrame.themeColor);

            JButton copyCourseButton = new JButton("Copy Course");
            copyCourseButton.addActionListener(this);
            copyCourseButton.setActionCommand("CopyCourse");
            copyCourseButton.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
            copyCourseButton.setBackground(SettingsFrame.themeColor);

            JButton upButton = new JButton("Move Up");
            upButton.addActionListener(this);
            upButton.setActionCommand("MoveUp");
            upButton.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
            upButton.setBackground(SettingsFrame.themeColor);

            JButton downButton = new JButton("Move Down");
            downButton.addActionListener(this);
            downButton.setActionCommand("MoveDown");
            downButton.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
            downButton.setBackground(SettingsFrame.themeColor);

            JButton updateButton = new JButton("Update Table");
            updateButton.addActionListener(this);
            updateButton.setActionCommand("UpdateTable");
            updateButton.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
            updateButton.setBackground(SettingsFrame.themeColor);

            JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
            buttonPanel.add(addCourseButton);
            buttonPanel.add(removeCourseButton);
            buttonPanel.add(copyCourseButton);
            buttonPanel.add(upButton);
            buttonPanel.add(downButton);
            buttonPanel.add(updateButton);

            // ================================================================================

            TableColumn column = null;
            JTableHeader tableHeader = table.getTableHeader();
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

            tableHeader.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
            tableHeader.setBackground(SettingsFrame.themeColor);

            table.setRowHeight(Frame.APP_HEIGHT / 40);
            table.setDefaultRenderer(Color.class, new ColorRenderer(true));
            table.setDefaultEditor(Color.class, new ColorEditor());
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            tableHeader.setPreferredSize(new Dimension(Frame.APP_WIDTH / 5, Frame.APP_HEIGHT / 30));
            tableHeader.setFont(new Font("Header", Font.BOLD, 13));
            tableHeader.setResizingAllowed(false);
            tableHeader.setReorderingAllowed(false);

            for (int i = 0; i < Coursetable.columnNames.length; i++) {
                column = table.getColumnModel().getColumn(i);

                if (i != 10) {
                    column.setCellRenderer(centerRenderer);
                }
                if (i == 0) {
                    column.setPreferredWidth(40);
                } else if (i == 1) {
                    column.setPreferredWidth(5);
                } else if (i == 2) {
                    column.setPreferredWidth(35);
                    String[] Strings = {null, "Monday(M)", "Tuesday(T)", "Wednesday(W)", "Thrusday(R)", "Friday(F)"};
                    JComboBox<String> DaycomboBox = new JComboBox<String>(Strings);
                    DaycomboBox.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
                    DaycomboBox.setBackground(SettingsFrame.themeColor);
                    column.setCellEditor(new DefaultCellEditor(DaycomboBox));
                } else if (i == 3) {
                    column.setPreferredWidth(10);
                    String[] Strings = {null, "8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00"
                            , "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00"
                            , "19:30", "20:00", "20:30", "21:00", "21:30", "22:00"};
                    JComboBox<String> TimecomboBox = new JComboBox<String>(Strings);
                    TimecomboBox.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
                    TimecomboBox.setBackground(SettingsFrame.themeColor);
                    column.setCellEditor(new DefaultCellEditor(TimecomboBox));
                } else if (i == 4) {
                    column.setPreferredWidth(20);
                    String[] Strings = {null, "30min", "60min", "90min", "120min", "150min", "180min", "210min", "240min", "270min", "300min"};
                    JComboBox<String> DurcomboBox = new JComboBox<String>(Strings);
                    DurcomboBox.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
                    DurcomboBox.setBackground(SettingsFrame.themeColor);
                    column.setCellEditor(new DefaultCellEditor(DurcomboBox));
                } else if (i == 5) {
                    column.setPreferredWidth(20);
                } else if (i == 6) {
                    column.setPreferredWidth(20);
                } else if (i == 7) {
                    column.setPreferredWidth(50);
                } else if (i == 8) {
                    column.setPreferredWidth(5);
                } else if (i == 9) {
                    column.setCellEditor(new NumEditor(0, 100));
                    column.setPreferredWidth(5);
                } else if (i == 10) {
                    column.setPreferredWidth(10);
                }

                bottomHalf.add(coursetable_scrollPane, BorderLayout.CENTER);
                bottomHalf.add(buttonPanel, BorderLayout.SOUTH);
                bottomHalf.setMinimumSize(new Dimension(Frame.APP_WIDTH / 10, Frame.APP_HEIGHT / 10));
                bottomHalf.setPreferredSize(new Dimension(Frame.APP_WIDTH, (int) (coursetable_scrollPane.getViewport().getViewSize().height + tableHeader.getPreferredSize().getHeight())));

                splitPane.add(bottomHalf, JSplitPane.BOTTOM);
            }
        } else {
            throw new Error("Table Error");
        }
    }

    // ================================================================================

    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        controller.UpdateTimetable(button.getActionCommand());
    }

    // ================================================================================

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Courses getCourse() {
        return courses;
    }

    public void setCourse(Courses course) {
        this.courses = course;
    }

    public JTable getTimetable() {
        return timetable;
    }

    public JTable getCoursetable() {
        return coursetable;
    }

    public Coursetable getCourseTable() {
        return courseTable;
    }

    // ================================================================================

    public static class Timetable extends AbstractTableModel {
        public static final String[] columnNames = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        private Object[][] data = {
                {"8:00 - 8:30", " ", " ", " ", " ", " "},
                {"8:30 - 9:00", " ", " ", " ", " ", " "},
                {"9:00 - 9:30", " ", " ", " ", " ", " "},
                {"9:30 - 10:00", " ", " ", " ", " ", " "},
                {"10:00 - 10:30", " ", " ", " ", " ", " "},
                {"10:30 - 11:00", " ", " ", " ", " ", " "},
                {"11:00 - 11:30", " ", " ", " ", " ", " "},
                {"11:30 - 12:00", " ", " ", " ", " ", " "},
                {"12:00 - 12:30", " ", " ", " ", " ", " "},
                {"12:30 - 13:00", " ", " ", " ", " ", " "},
                {"13:00 - 13:30", " ", " ", " ", " ", " "},
                {"13:30 - 14:00", " ", " ", " ", " ", " "},
                {"14:00 - 14:30", " ", " ", " ", " ", " "},
                {"14:30 - 15:00", " ", " ", " ", " ", " "},
                {"15:00 - 15:30", " ", " ", " ", " ", " "},
                {"15:30 - 16:00", " ", " ", " ", " ", " "},
                {"16:00 - 16:30", " ", " ", " ", " ", " "},
                {"16:30 - 17:00", " ", " ", " ", " ", " "},
                {"17:00 - 17:30", " ", " ", " ", " ", " "},
                {"17:30 - 18:00", " ", " ", " ", " ", " "},
                {"18:00 - 18:30", " ", " ", " ", " ", " "},
                {"18:30 - 19:00", " ", " ", " ", " ", " "},
                {"19:00 - 19:30", " ", " ", " ", " ", " "},
                {"19:30 - 20:00", " ", " ", " ", " ", " "},
                {"20:00 - 20:30", " ", " ", " ", " ", " "},
                {"20:30 - 21:00", " ", " ", " ", " ", " "},
                {"21:00 - 21:30", " ", " ", " ", " ", " "},
                {"21:30 - 22:00", " ", " ", " ", " ", " "},
                {"22:00 - 22:30", " ", " ", " ", " ", " "},
        };

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }

        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }

    }

    // ================================================================================

    public static class Coursetable extends DefaultTableModel {
        public static final String[] columnNames = {"Name", "Type", "Day", "Start Time", "Duration", "Location", "Cat #", "Instructor", "Section", "Credits", "Colour"};

        public Coursetable() {
            super(columnNames, 0);
            if (!Frame.importing) {
                add_defaultRow();
            }
        }

        public Class<?> getColumnClass(int columnIndex) {
            if (getRowCount() > 0 && getValueAt(0, columnIndex) != null) {
                return getValueAt(0, columnIndex).getClass();
            }
            return super.getColumnClass(columnIndex);
        }

        public void add_defaultRow() {
            addRow(new Courses(null, null, null, null, null, null, null, null, null, 0, Color.WHITE));
        }

        public void addRow(Courses rowData) {
            if (rowData == null) {
                throw new IllegalArgumentException("rowData cannot be null");
            }
            Vector<Object> rowVector = new Vector<>();
            rowVector.add(rowData.getName());
            rowVector.add(rowData.getType());
            rowVector.add(rowData.getDay());
            rowVector.add(rowData.getStartTime());
            rowVector.add(rowData.getDuration());
            rowVector.add(rowData.getLocation());
            rowVector.add(rowData.getCat());
            rowVector.add(rowData.getInstructor());
            rowVector.add(rowData.getSection());
            rowVector.add(rowData.getCredits());
            rowVector.add(rowData.getColor());
            super.addRow(rowVector);
        }

        public void removeRow() {
            super.removeRow(getRowCount() - 1);
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return super.getRowCount();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int column) {
            return super.getValueAt(row, column);
        }

        public boolean isCellEditable(int row, int col) {
            return true;
        }

        public void setValueAt(Object aValue, int row, int column) {
            super.setValueAt(aValue, row, column);
        }

    }
}
