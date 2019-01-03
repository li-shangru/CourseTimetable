package Main;

import Utility.NumEditor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class GPA_CALC extends JPanel implements ActionListener {


    public static final String[] GPA_Scale = {"Select Scale", "A-F          0-4", "A-F(+)     0-9", "A-F(+/-)   0-12", "60-100    1-5", "Custom Scale"};
    public static List<String> Grades_Scale = new ArrayList<String>();
    public static List<Double> Grades_GPA = new ArrayList<Double>();
    public static List<GPA_Course> courseList = new ArrayList<GPA_Course>();
    public static double User_GPA = -1;
    public static String selected_index = "Select Scale";
    private static boolean editable = false;
    private Scaletable scaleTable = new Scaletable("Select Scale");
    private Coursetable courseTable = new Coursetable();
    private JTable scaletable = new JTable(scaleTable);
    private JTable coursetable = new JTable(courseTable);
    private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    private JTextField userGPA = new JTextField();

    // ================================================================================

    public GPA_CALC() {
        super(new BorderLayout());

        add(splitPane, BorderLayout.CENTER);

        TableSetUp(scaletable, "Scaletable");
        TableSetUp(coursetable, "Coursetable");

        splitPane.setDividerLocation(Frame.APP_WIDTH / 2);
    }

    // ================================================================================

    public static boolean checkCourses() {
        if (courseList.isEmpty()) {
            return false;
        } else {
            for (int i = 0; i < courseList.size(); i++) {
                if (courseList.get(i).getGrade() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    // ================================================================================

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().getClass().equals(JButton.class)) {
            JButton button = (JButton) e.getSource();
            if (button.getActionCommand().equals("AddRow")) {
                scaleTable.addRow("");
            } else if (button.getActionCommand().equals("RemoveRow")) {
                if (scaleTable.getRowCount() > 1) {
                    scaleTable.removeRow(scaleTable.getRowCount() - 1);
                }
            } else if (button.getActionCommand().equals("AddCourse")) {
                courseTable.add_defaultRow();
            } else if (button.getActionCommand().equals("RemoveCourse")) {
                coursetable.editingCanceled(null);
                if (courseTable.getRowCount() > 1) {
                    courseTable.removeRow(courseTable.getRowCount() - 1);
                }
            } else if (button.getActionCommand().equals("CalcGPA")) {
                CalcGPA();
                TableSetUp(coursetable, "Coursetable_update");
            } else if (button.getActionCommand().equals("SetScale")) {
                User_GPA = -1;
                courseTable.updateGrades(scaleTable);
                TableSetUp(coursetable, "Coursetable_update");
            }
        } else if (e.getSource().getClass().equals(JComboBox.class)) {
            JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
            if (comboBox.getActionCommand().equals("GPAScale")) {
                splitPane.remove(splitPane.getLeftComponent());
                Scaletable.gpa_scale = (String) comboBox.getSelectedItem();
                selected_index = (String) comboBox.getSelectedItem();

                TableSetUp(scaletable, "Scaletable");
                if (comboBox.getSelectedIndex() != 0) {
                    courseTable.updateGrades(scaleTable);
                }
                User_GPA = -1;
                TableSetUp(coursetable, "Coursetable_update");

                splitPane.setDividerLocation(Frame.APP_WIDTH / 2);
            }
        }

    }

    // ================================================================================

    private void TableSetUp(JTable table, String name) {
        if (name == "Scaletable") {
            scaleTable = new Scaletable(Scaletable.gpa_scale);
            scaletable = new JTable(scaleTable);
            table = scaletable;
            JScrollPane scaletable_scrollPane = new JScrollPane(scaletable);
            JTableHeader tableHeader = scaletable.getTableHeader();
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

            JPanel leftHalf = new JPanel(new BorderLayout());

            tableHeader.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
            tableHeader.setBackground(SettingsFrame.themeColor);

            if (Scaletable.gpa_scale.equals("Custom Scale")) {
                editable = true;

                JComboBox<String> GPAScaleSelect = new JComboBox<>(GPA_Scale);
                GPAScaleSelect.setSelectedItem(GPA_Scale[5]);
                GPAScaleSelect.addActionListener(this);
                GPAScaleSelect.setActionCommand("GPAScale");
                GPAScaleSelect.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
                GPAScaleSelect.setBackground(SettingsFrame.themeColor);

                JButton addRowButton = new JButton("Add Row");
                addRowButton.addActionListener(this);
                addRowButton.setActionCommand("AddRow");
                addRowButton.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
                addRowButton.setBackground(SettingsFrame.themeColor);

                JButton removeRowButton = new JButton("Remove Row");
                removeRowButton.addActionListener(this);
                removeRowButton.setActionCommand("RemoveRow");
                removeRowButton.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
                removeRowButton.setBackground(SettingsFrame.themeColor);

                JButton calcGPAButton = new JButton("Calculate GPA");
                calcGPAButton.addActionListener(this);
                calcGPAButton.setActionCommand("CalcGPA");
                calcGPAButton.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
                calcGPAButton.setBackground(SettingsFrame.themeColor);

                JButton setScaleButton = new JButton("Set Scale");
                setScaleButton.addActionListener(this);
                setScaleButton.setActionCommand("SetScale");
                setScaleButton.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
                setScaleButton.setBackground(SettingsFrame.themeColor);

                JPanel botPanel = new JPanel(new BorderLayout());
                JPanel scalePanel = new JPanel(new GridLayout(2, 0));

                scalePanel.add(addRowButton);
                scalePanel.add(removeRowButton);
                scalePanel.add(setScaleButton);
                scalePanel.add(calcGPAButton);

                botPanel.add(GPAScaleSelect, BorderLayout.NORTH);
                botPanel.add(scalePanel, BorderLayout.SOUTH);

                leftHalf.add(botPanel, BorderLayout.SOUTH);
            }

            // ================================================================================

            else if (Scaletable.gpa_scale.equals("Select Scale")) {
                editable = false;

                JComboBox<String> GPAScaleSelect = new JComboBox<>(GPA_Scale);
                GPAScaleSelect.setSelectedItem(GPA_Scale[0]);
                GPAScaleSelect.addActionListener(this);
                GPAScaleSelect.setActionCommand("GPAScale");
                GPAScaleSelect.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
                GPAScaleSelect.setBackground(SettingsFrame.themeColor);

                JPanel scalePanel = new JPanel(new GridLayout(1, 0));
                scalePanel.add(GPAScaleSelect);

                leftHalf.add(scalePanel, BorderLayout.SOUTH);
            }

            // ================================================================================

            else {
                editable = false;

                JComboBox<String> GPAScaleSelect = new JComboBox<>(GPA_Scale);
                for (int i = 0; i < GPA_Scale.length; i++) {
                    if (GPA_Scale[i] == Scaletable.gpa_scale) {
                        GPAScaleSelect.setSelectedItem(GPA_Scale[i]);
                        selected_index = GPA_Scale[i];
                    }
                }
                GPAScaleSelect.addActionListener(this);
                GPAScaleSelect.setActionCommand("GPAScale");
                GPAScaleSelect.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
                GPAScaleSelect.setBackground(SettingsFrame.themeColor);

                JButton calcGPAButton = new JButton("Calculate GPA");
                calcGPAButton.addActionListener(this);
                calcGPAButton.setActionCommand("CalcGPA");
                calcGPAButton.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
                calcGPAButton.setBackground(SettingsFrame.themeColor);


                JPanel scalePanel = new JPanel(new GridLayout(1, 0));
                scalePanel.add(GPAScaleSelect);
                scalePanel.add(calcGPAButton);

                leftHalf.add(scalePanel, BorderLayout.SOUTH);
            }

            // ================================================================================

            leftHalf.add(scaletable_scrollPane, BorderLayout.CENTER);

            leftHalf.setMinimumSize(new Dimension(Frame.APP_WIDTH / 10, Frame.APP_HEIGHT / 10));
            leftHalf.setPreferredSize(new Dimension(Frame.APP_WIDTH / 2, (int) (scaletable_scrollPane.getViewport().getViewSize().height + tableHeader.getPreferredSize().getHeight())));

            splitPane.add(leftHalf);

            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            table.setRowHeight(Frame.APP_HEIGHT / 20);
            table.setDefaultRenderer(Object.class, centerRenderer);

            tableHeader.setPreferredSize(new Dimension(Frame.APP_WIDTH / 5, Frame.APP_HEIGHT / 30));
            tableHeader.setFont(new Font("Header", Font.BOLD, 15));
            tableHeader.setResizingAllowed(false);
            tableHeader.setReorderingAllowed(false);

            TableColumn column;
            for (int i = 0; i < Scaletable.columnNames.length; i++) {
                column = table.getColumnModel().getColumn(i);
                if (i == 1) {
                    column.setPreferredWidth(Frame.APP_WIDTH / 5);
                } else {
                    column.setPreferredWidth(Frame.APP_WIDTH / 6);
                }
            }

            splitPane.setDividerLocation(425);
        }

        // ================================================================================

        else if (name == "Coursetable") {
            JScrollPane coursetable_scrollPane = new JScrollPane(coursetable);
            JPanel rightHalf = new JPanel(new BorderLayout());

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

            JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
            buttonPanel.add(addCourseButton);
            buttonPanel.add(removeCourseButton);

            JPanel GPAPanel = new JPanel(new BorderLayout());

            userGPA.setEditable(false);
            userGPA.setHorizontalAlignment(JTextField.CENTER);
            userGPA.setFont(new Font(Font.SERIF, Font.CENTER_BASELINE, 30));
            userGPA.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
            userGPA.setBackground(SettingsFrame.themeColor);

            GPAPanel.add(userGPA, BorderLayout.NORTH);
            GPAPanel.add(buttonPanel, BorderLayout.SOUTH);

            // ================================================================================

            TableColumn column = null;
            JTableHeader tableHeader = table.getTableHeader();
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

            tableHeader.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
            tableHeader.setBackground(SettingsFrame.themeColor);
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            table.setRowHeight(Frame.APP_HEIGHT / 20);
            table.setDefaultRenderer(Object.class, centerRenderer);

            tableHeader.setPreferredSize(new Dimension(Frame.APP_WIDTH / 5, Frame.APP_HEIGHT / 30));
            tableHeader.setFont(new Font("Header", Font.BOLD, 15));
            tableHeader.setResizingAllowed(false);
            tableHeader.setReorderingAllowed(false);

            for (int i = 0; i < Coursetable.columnNames.length; i++) {
                column = table.getColumnModel().getColumn(i);

                if (i == 0) {
                    column.setPreferredWidth(Frame.APP_HEIGHT / 4);
                } else if (i == 1) {
                    column.setCellEditor(new NumEditor(0, 100));
                    column.setCellRenderer(centerRenderer);
                    column.setPreferredWidth(Frame.APP_HEIGHT / 5);
                } else if (i == 2) {
                    column.setPreferredWidth(Frame.APP_HEIGHT / 5);
                }
            }

            rightHalf.add(coursetable_scrollPane, BorderLayout.CENTER);
            rightHalf.add(GPAPanel, BorderLayout.SOUTH);
            rightHalf.setMinimumSize(new Dimension(Frame.APP_WIDTH / 10, Frame.APP_HEIGHT / 10));
            rightHalf.setPreferredSize(new Dimension(Frame.APP_WIDTH / 2, (int) (coursetable_scrollPane.getViewport().getViewSize().height + tableHeader.getPreferredSize().getHeight())));

            splitPane.add(rightHalf);
        }

        // ================================================================================

        else if (name == "Coursetable_update") {
            if (User_GPA >= 0) {
                DecimalFormat formatter = new DecimalFormat("#0.00");
                userGPA.setText("GPA   " + formatter.format(User_GPA));
            } else if (User_GPA == -2) {
                userGPA.setText("Data Error");
            } else {
                TableColumn column = table.getColumnModel().getColumn(2);
                JComboBox<String> comboBox = new JComboBox<String>();
                for (int j = 0; j < Grades_Scale.size(); j++) {
                    comboBox.addItem(Grades_Scale.get(j));
                }
                column.setCellEditor(new DefaultCellEditor(comboBox));
            }
        }

        // ================================================================================

        else {
            throw new Error("Table type Error");
        }
        table.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        table.setBackground(SettingsFrame.themeColor.brighter());
        table.setRowSelectionAllowed(false);
    }

    // ================================================================================

    private void CalcGPA() {
        courseTable.updateGrades(scaleTable);
        double Total_Credits = 0;
        double Total_Grade = 0;
        for (int i = 0; i < courseTable.getRowCount(); i++) {
            double User_Credits = (Double) courseTable.getValueAt(i, 1);
            if (User_Credits != 0 && courseTable.getValueAt(i, 2) != null && !courseTable.getValueAt(i, 2).toString().isEmpty()) {
                double User_Grade;
                try {
                    User_Grade = courseTable.getUserGrade((String) courseTable.getValueAt(i, 2));
                } catch (Exception e) {
                    userGPA.setText("Data Error");
                    return;
                }
                Total_Credits = Total_Credits + User_Credits;
                Total_Grade = Total_Grade + User_Grade * User_Credits;
                User_GPA = -1;
            } else {
                User_GPA = -2;
                break;
            }
        }
        if (User_GPA != -2) {
            User_GPA = Total_Grade / Total_Credits;
        }
    }

    // ================================================================================

    public Coursetable getCoursetable() {
        return courseTable;
    }

    public Scaletable getScaletable() {
        return scaleTable;
    }

    // ================================================================================

    public static class Scaletable extends DefaultTableModel {
        public static final String[] columnNames = {"Letter Grade", "Percent Grade", "GPA"};
        public static String gpa_scale = null;

        public Scaletable(String gpa_scale) {
            super(columnNames, 0);
            Scaletable.gpa_scale = gpa_scale;
            addRow(gpa_scale);
        }

        public Class<?> getColumnClass(int columnIndex) {
            if (getRowCount() > 0 && getValueAt(0, columnIndex) != null) {
                return getValueAt(0, columnIndex).getClass();
            }
            return super.getColumnClass(columnIndex);
        }

        public void addRow(String gpa_scale) {
            if (gpa_scale.equals("A-F          0-4")) {
                Vector<Object> rowVectorA = new Vector<>();
                Vector<Object> rowVectorB = new Vector<>();
                Vector<Object> rowVectorC = new Vector<>();
                Vector<Object> rowVectorD = new Vector<>();
                Vector<Object> rowVectorF = new Vector<>();

                rowVectorA.add("A");
                rowVectorA.add("90-100%");
                rowVectorA.add("4.0");

                rowVectorB.add("B");
                rowVectorB.add("80-89%");
                rowVectorB.add("3.0");

                rowVectorC.add("C");
                rowVectorC.add("70-79%");
                rowVectorC.add("2.0");

                rowVectorD.add("D");
                rowVectorD.add("60-49%");
                rowVectorD.add("1.0");

                rowVectorF.add("F");
                rowVectorF.add("0-59%");
                rowVectorF.add("0.0");

                super.addRow(rowVectorA);
                super.addRow(rowVectorB);
                super.addRow(rowVectorC);
                super.addRow(rowVectorD);
                super.addRow(rowVectorF);
            } else if (gpa_scale.equals("A-F(+)     0-9")) {
                Vector<Object> rowVectorAP = new Vector<>();
                Vector<Object> rowVectorA = new Vector<>();
                Vector<Object> rowVectorBP = new Vector<>();
                Vector<Object> rowVectorB = new Vector<>();
                Vector<Object> rowVectorCP = new Vector<>();
                Vector<Object> rowVectorC = new Vector<>();
                Vector<Object> rowVectorDP = new Vector<>();
                Vector<Object> rowVectorD = new Vector<>();
                Vector<Object> rowVectorE = new Vector<>();
                Vector<Object> rowVectorF = new Vector<>();

                rowVectorAP.add("A+");
                rowVectorAP.add("90-100%");
                rowVectorAP.add("9.0");
                rowVectorA.add("A");
                rowVectorA.add("80-89%");
                rowVectorA.add("8.0");

                rowVectorBP.add("B+");
                rowVectorBP.add("75-79%");
                rowVectorBP.add("7.0");
                rowVectorB.add("B");
                rowVectorB.add("70-74%");
                rowVectorB.add("6.0");

                rowVectorCP.add("C+");
                rowVectorCP.add("65-69%");
                rowVectorCP.add("5.0");
                rowVectorC.add("C");
                rowVectorC.add("60-64%");
                rowVectorC.add("4.0");

                rowVectorDP.add("D+");
                rowVectorDP.add("55-59%");
                rowVectorDP.add("3.0");
                rowVectorD.add("D");
                rowVectorD.add("50-54%");
                rowVectorD.add("2.0");

                rowVectorE.add("E");
                rowVectorE.add("45-49%");
                rowVectorE.add("1.0");

                rowVectorF.add("F");
                rowVectorF.add("0-45%");
                rowVectorF.add("0.0");

                super.addRow(rowVectorAP);
                super.addRow(rowVectorA);
                super.addRow(rowVectorBP);
                super.addRow(rowVectorB);
                super.addRow(rowVectorCP);
                super.addRow(rowVectorC);
                super.addRow(rowVectorDP);
                super.addRow(rowVectorD);
                super.addRow(rowVectorE);
                super.addRow(rowVectorF);
            } else if (gpa_scale.equals("A-F(+/-)   0-12")) {
                Vector<Object> rowVectorAP = new Vector<>();
                Vector<Object> rowVectorA = new Vector<>();
                Vector<Object> rowVectorAM = new Vector<>();
                Vector<Object> rowVectorBP = new Vector<>();
                Vector<Object> rowVectorB = new Vector<>();
                Vector<Object> rowVectorBM = new Vector<>();
                Vector<Object> rowVectorCP = new Vector<>();
                Vector<Object> rowVectorC = new Vector<>();
                Vector<Object> rowVectorCM = new Vector<>();
                Vector<Object> rowVectorDP = new Vector<>();
                Vector<Object> rowVectorD = new Vector<>();
                Vector<Object> rowVectorDM = new Vector<>();
                Vector<Object> rowVectorF = new Vector<>();

                rowVectorAP.add("A+");
                rowVectorAP.add("90-100%");
                rowVectorAP.add("12.0");
                rowVectorA.add("A");
                rowVectorA.add("85-89%");
                rowVectorA.add("11.0");
                rowVectorAM.add("A-");
                rowVectorAM.add("80-84%");
                rowVectorAM.add("10.0");

                rowVectorBP.add("B+");
                rowVectorBP.add("77-79%");
                rowVectorBP.add("9.0");
                rowVectorB.add("B");
                rowVectorB.add("73-76%");
                rowVectorB.add("8.0");
                rowVectorBM.add("B-");
                rowVectorBM.add("70-72%");
                rowVectorBM.add("7.0");

                rowVectorCP.add("C+");
                rowVectorCP.add("67-69%");
                rowVectorCP.add("6.0");
                rowVectorC.add("C");
                rowVectorC.add("63-66%");
                rowVectorC.add("5.0");
                rowVectorCM.add("C-");
                rowVectorCM.add("60-62%");
                rowVectorCM.add("4.0");

                rowVectorDP.add("D+");
                rowVectorDP.add("57-59%");
                rowVectorDP.add("3.0");
                rowVectorD.add("D");
                rowVectorD.add("53-56%");
                rowVectorD.add("2.0");
                rowVectorDM.add("D-");
                rowVectorDM.add("50-52%");
                rowVectorDM.add("1.0");

                rowVectorF.add("F");
                rowVectorF.add("0-49%");
                rowVectorF.add("0.0");

                super.addRow(rowVectorAP);
                super.addRow(rowVectorA);
                super.addRow(rowVectorAM);
                super.addRow(rowVectorBP);
                super.addRow(rowVectorB);
                super.addRow(rowVectorBM);
                super.addRow(rowVectorCP);
                super.addRow(rowVectorC);
                super.addRow(rowVectorCM);
                super.addRow(rowVectorDP);
                super.addRow(rowVectorD);
                super.addRow(rowVectorDM);
                super.addRow(rowVectorF);
            } else if (gpa_scale.equals("60-100    1-5")) {
                Vector<Object> rowVector100 = new Vector<>();
                Vector<Object> rowVector99 = new Vector<>();
                Vector<Object> rowVector98 = new Vector<>();
                Vector<Object> rowVector97 = new Vector<>();
                Vector<Object> rowVector96 = new Vector<>();
                Vector<Object> rowVector95 = new Vector<>();
                Vector<Object> rowVector94 = new Vector<>();
                Vector<Object> rowVector93 = new Vector<>();
                Vector<Object> rowVector92 = new Vector<>();
                Vector<Object> rowVector91 = new Vector<>();
                Vector<Object> rowVector90 = new Vector<>();
                Vector<Object> rowVector89 = new Vector<>();
                Vector<Object> rowVector88 = new Vector<>();
                Vector<Object> rowVector87 = new Vector<>();
                Vector<Object> rowVector86 = new Vector<>();
                Vector<Object> rowVector85 = new Vector<>();
                Vector<Object> rowVector84 = new Vector<>();
                Vector<Object> rowVector83 = new Vector<>();
                Vector<Object> rowVector82 = new Vector<>();
                Vector<Object> rowVector81 = new Vector<>();
                Vector<Object> rowVector80 = new Vector<>();
                Vector<Object> rowVector79 = new Vector<>();
                Vector<Object> rowVector78 = new Vector<>();
                Vector<Object> rowVector77 = new Vector<>();
                Vector<Object> rowVector76 = new Vector<>();
                Vector<Object> rowVector75 = new Vector<>();
                Vector<Object> rowVector74 = new Vector<>();
                Vector<Object> rowVector73 = new Vector<>();
                Vector<Object> rowVector72 = new Vector<>();
                Vector<Object> rowVector71 = new Vector<>();
                Vector<Object> rowVector70 = new Vector<>();
                Vector<Object> rowVector69 = new Vector<>();
                Vector<Object> rowVector68 = new Vector<>();
                Vector<Object> rowVector67 = new Vector<>();
                Vector<Object> rowVector66 = new Vector<>();
                Vector<Object> rowVector65 = new Vector<>();
                Vector<Object> rowVector64 = new Vector<>();
                Vector<Object> rowVector63 = new Vector<>();
                Vector<Object> rowVector62 = new Vector<>();
                Vector<Object> rowVector61 = new Vector<>();
                Vector<Object> rowVector60 = new Vector<>();

                rowVector100.add("100");
                rowVector100.add("100%");
                rowVector100.add("5.0");

                rowVector99.add("99");
                rowVector99.add("99%");
                rowVector99.add("4.9");
                rowVector98.add("98");
                rowVector98.add("98%");
                rowVector98.add("4.8");
                rowVector97.add("97");
                rowVector97.add("97%");
                rowVector97.add("4.7");
                rowVector96.add("96");
                rowVector96.add("96%");
                rowVector96.add("4.6");
                rowVector95.add("95");
                rowVector95.add("95%");
                rowVector95.add("4.5");
                rowVector94.add("94");
                rowVector94.add("94%");
                rowVector94.add("4.4");
                rowVector93.add("93");
                rowVector93.add("93%");
                rowVector93.add("4.3");
                rowVector92.add("92");
                rowVector92.add("92%");
                rowVector92.add("4.2");
                rowVector91.add("91");
                rowVector91.add("91%");
                rowVector91.add("4.1");
                rowVector90.add("90");
                rowVector90.add("90%");
                rowVector90.add("4.0");

                rowVector89.add("89");
                rowVector89.add("89%");
                rowVector89.add("3.9");
                rowVector88.add("88");
                rowVector88.add("88%");
                rowVector88.add("3.8");
                rowVector87.add("87");
                rowVector87.add("87%");
                rowVector87.add("3.7");
                rowVector86.add("86");
                rowVector86.add("86%");
                rowVector86.add("3.6");
                rowVector85.add("85");
                rowVector85.add("85%");
                rowVector85.add("3.5");
                rowVector84.add("84");
                rowVector84.add("84%");
                rowVector84.add("3.4");
                rowVector83.add("83");
                rowVector83.add("83%");
                rowVector83.add("3.3");
                rowVector82.add("82");
                rowVector82.add("82%");
                rowVector82.add("3.2");
                rowVector81.add("81");
                rowVector81.add("81%");
                rowVector81.add("3.1");
                rowVector80.add("80");
                rowVector80.add("80%");
                rowVector80.add("3.0");

                rowVector79.add("79");
                rowVector79.add("79%");
                rowVector79.add("2.9");
                rowVector78.add("78");
                rowVector78.add("78%");
                rowVector78.add("2.8");
                rowVector77.add("77");
                rowVector77.add("77%");
                rowVector77.add("2.7");
                rowVector76.add("76");
                rowVector76.add("76%");
                rowVector76.add("2.6");
                rowVector75.add("75");
                rowVector75.add("75%");
                rowVector75.add("2.5");
                rowVector74.add("74");
                rowVector74.add("74%");
                rowVector74.add("2.4");
                rowVector73.add("73");
                rowVector73.add("73%");
                rowVector73.add("2.3");
                rowVector72.add("72");
                rowVector72.add("72%");
                rowVector72.add("2.2");
                rowVector71.add("71");
                rowVector71.add("71%");
                rowVector71.add("2.1");
                rowVector70.add("70");
                rowVector70.add("70%");
                rowVector70.add("2.0");

                rowVector69.add("69");
                rowVector69.add("69%");
                rowVector69.add("1.9");
                rowVector68.add("68");
                rowVector68.add("68%");
                rowVector68.add("1.8");
                rowVector67.add("67");
                rowVector67.add("67%");
                rowVector67.add("1.7");
                rowVector66.add("66");
                rowVector66.add("66%");
                rowVector66.add("1.6");
                rowVector65.add("65");
                rowVector65.add("65%");
                rowVector65.add("1.5");
                rowVector64.add("64");
                rowVector64.add("64%");
                rowVector64.add("1.4");
                rowVector63.add("63");
                rowVector63.add("63%");
                rowVector63.add("1.3");
                rowVector62.add("62");
                rowVector62.add("62%");
                rowVector62.add("1.2");
                rowVector61.add("61");
                rowVector61.add("61%");
                rowVector61.add("1.1");
                rowVector60.add("60");
                rowVector60.add("60%");
                rowVector60.add("1.0");

                super.addRow(rowVector100);
                super.addRow(rowVector99);
                super.addRow(rowVector98);
                super.addRow(rowVector97);
                super.addRow(rowVector96);
                super.addRow(rowVector95);
                super.addRow(rowVector94);
                super.addRow(rowVector93);
                super.addRow(rowVector92);
                super.addRow(rowVector91);
                super.addRow(rowVector90);

                super.addRow(rowVector89);
                super.addRow(rowVector88);
                super.addRow(rowVector87);
                super.addRow(rowVector86);
                super.addRow(rowVector85);
                super.addRow(rowVector84);
                super.addRow(rowVector83);
                super.addRow(rowVector82);
                super.addRow(rowVector81);
                super.addRow(rowVector80);

                super.addRow(rowVector79);
                super.addRow(rowVector78);
                super.addRow(rowVector77);
                super.addRow(rowVector76);
                super.addRow(rowVector75);
                super.addRow(rowVector74);
                super.addRow(rowVector73);
                super.addRow(rowVector72);
                super.addRow(rowVector71);
                super.addRow(rowVector70);

                super.addRow(rowVector69);
                super.addRow(rowVector68);
                super.addRow(rowVector67);
                super.addRow(rowVector66);
                super.addRow(rowVector65);
                super.addRow(rowVector64);
                super.addRow(rowVector63);
                super.addRow(rowVector62);
                super.addRow(rowVector61);
                super.addRow(rowVector60);

            } else if (gpa_scale.equals("Select Scale")) {
                Vector<Object> rowVector = new Vector<>();

                rowVector.add("Please");
                rowVector.add("Select");
                rowVector.add("Scale");

                super.addRow(rowVector);
            } else if (gpa_scale.equals("Custom Scale")) {
                for (int i = 0; i < 4; i++) {
                    Vector<Object> rowVector = new Vector<>();
                    rowVector.add("");
                    rowVector.add("");
                    rowVector.add("");
                    super.addRow(rowVector);
                }
            } else {
                Vector<Object> rowVector = new Vector<>();
                rowVector.add("");
                rowVector.add("");
                rowVector.add("");
                super.addRow(rowVector);
            }
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
            return editable;
        }

        public void setValueAt(Object aValue, int row, int column) {
            super.setValueAt(aValue, row, column);
        }

    }

    // ================================================================================

    public static class Coursetable extends DefaultTableModel {
        public static final String[] columnNames = {"Name", "Credits", "Grade"};

        public Coursetable() {
            super(columnNames, 0);
            add_defaultRow();
            add_defaultRow();
            add_defaultRow();
        }

        public Class<?> getColumnClass(int columnIndex) {
            if (getRowCount() > 0 && getValueAt(0, columnIndex) != null) {
                return getValueAt(0, columnIndex).getClass();
            }
            return super.getColumnClass(columnIndex);
        }

        public void updateGrades(Scaletable scaletable) {
            Grades_Scale.clear();
            Grades_GPA.clear();
            courseList.clear();
            for (int i = 0; i < scaletable.getRowCount(); i++) {
                String grade = (String) scaletable.getValueAt(i, 0);
                if (!grade.equals(null)) {
                    if (!grade.isEmpty()) {
                        Grades_Scale.add(grade);
                        Grades_GPA.add(Double.parseDouble(scaletable.getValueAt(i, 2).toString()));
                    }
                }
            }
            for (int i = 0; i < this.getRowCount(); i++) {
                GPA_Course course = new GPA_Course((String) this.getValueAt(i, 0), (Double) this.getValueAt(i, 1), (String) this.getValueAt(i, 2));
                courseList.add(course);
            }
        }

        public double getUserGrade(String user_grade) {
            for (int i = 0; i < Grades_Scale.size(); i++) {
                if (Grades_Scale.get(i).equals(user_grade)) {
                    return Grades_GPA.get(i);
                }
            }
            throw new IllegalArgumentException("Grade not found");
        }

        public void add_defaultRow() {
            addRow(new GPA_Course("", 0, null));
        }

        public void addRow(GPA_Course rowData) {
            if (rowData == null) {
                throw new IllegalArgumentException("rowData cannot be null");
            }
            Vector<Object> rowVector = new Vector<>();
            rowVector.add(rowData.getName());
            rowVector.add(rowData.getCredits());
            rowVector.add(rowData.getGrade());
            super.addRow(rowVector);

            courseList.add(rowData);
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

    // ================================================================================

    public static class GPA_Course {

        private String name = null;
        private double credits = 0;
        private String grade = null;

        public GPA_Course(String name, double credits, String grade) {
            this.name = name;
            this.grade = grade;
            this.credits = credits;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public double getCredits() {
            return credits;
        }

        public void setCredits(double credits) {
            this.credits = credits;
        }

    }

}
