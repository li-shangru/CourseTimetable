package Main;

import Utility.ColorEditor;
import Utility.ColorRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import static Main.Frame.FILE_PATH;

public class SettingsFrame extends JDialog implements ActionListener {
    public static final Color PURE_BLACK = new Color(30, 30, 30);
    private static final String OK = "OK";
    private static final String CANCEL = "Cancel";
    private static final String SAVED_TAB_LOAD = "Load saved tabs on start up";
    private static final String SOUND_EFFECT = "Enable sound effects";
    private static final String LIGHT = "Clear White";
    private static final String DARK = "Pure Black";
    private static final String CUSTOM = "Custom Color";
    public static Color themeColor = Color.WHITE;
    public static JCheckBox saved_tab_load = new JCheckBox(SAVED_TAB_LOAD);
    public static JCheckBox sound_effect_check = new JCheckBox(SOUND_EFFECT);
    private static JRadioButton lightButton = new JRadioButton(LIGHT);
    private static JRadioButton darkButton = new JRadioButton(DARK);
    private static JRadioButton customButton = new JRadioButton(CUSTOM);
    private static JTable customColorTable;
    private static JTextField noticeText = new JTextField("Please restart application to take effect");


    // TODO: change font size
    public SettingsFrame() {
        super.setTitle("Settings");
        super.setModal(true);

        setMinimumSize(new Dimension(200, 150));
        setResizable(false);

        SetPanel();

        pack();
        this.setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void startUpCheck() {
        try {
            File file = new File(FILE_PATH + "settings.ini");
            if (!file.exists()) {
                saved_tab_load.setSelected(false);
                lightButton.setSelected(true);
                darkButton.setSelected(false);
                return;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String s = reader.readLine();
            String[] data = s.split("---");
            sound_effect_check.setSelected(Boolean.valueOf(data[0]));
            saved_tab_load.setSelected(Boolean.valueOf(data[1]));
            themeColor = new Color(Integer.parseInt(data[2]));
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Color getContrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
        return y >= 128 ? Color.black : Color.white;
    }

    private void SetPanel() {
        JPanel buttonPanel = new JPanel();

        JButton OK_button = new JButton(OK);
        OK_button.setActionCommand(OK);
        OK_button.addActionListener(this);
        OK_button.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        OK_button.setBackground(SettingsFrame.themeColor);

        JButton Cancel_button = new JButton(CANCEL);
        Cancel_button.setActionCommand(CANCEL);
        Cancel_button.addActionListener(this);
        Cancel_button.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
        Cancel_button.setBackground(SettingsFrame.themeColor);

        buttonPanel.add(OK_button);
        buttonPanel.add(Cancel_button);

        // ================================================================================

        JPanel settingPanel = new JPanel();
        GroupLayout layout = new GroupLayout(settingPanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        settingPanel.setLayout(layout);

        saved_tab_load.setHorizontalAlignment(SwingConstants.RIGHT);
        saved_tab_load.setActionCommand(SAVED_TAB_LOAD);
        saved_tab_load.addActionListener(this);
        saved_tab_load.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));

        sound_effect_check.setHorizontalAlignment(SwingConstants.RIGHT);
        sound_effect_check.setActionCommand(SOUND_EFFECT);
        sound_effect_check.addActionListener(this);
        sound_effect_check.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));


        JTextField themeText = new JTextField("Choose an app theme");
        themeText.setEditable(false);
        themeText.setBorder(null);
        themeText.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        themeText.setBackground(this.getBackground());

        lightButton.setActionCommand(LIGHT);
        lightButton.addActionListener(this);
        lightButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        darkButton.setActionCommand(DARK);
        darkButton.addActionListener(this);
        darkButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        Object[][] row = new Object[][]{{"        Pick a color :", Color.WHITE}};
        Object[] col = new Object[]{"", ""};
        customColorTable = new JTable(row, col) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col != 0;
            }
        };

        customButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        customButton.setActionCommand(CUSTOM);
        customButton.addActionListener(this);
        customColorTable.setBorder(null);
        customColorTable.setShowGrid(false);
        customColorTable.setRowSelectionAllowed(false);
        customColorTable.setBackground(this.getBackground());
        customColorTable.setRowHeight(30);
        customColorTable.setFocusable(false);
        customColorTable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        customColorTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        customColorTable.getColumnModel().getColumn(1).setCellEditor(new ColorEditor());
        customColorTable.getColumnModel().getColumn(1).setCellRenderer(new ColorRenderer(true));

        noticeText.setEditable(false);
        noticeText.setHighlighter(null);
        noticeText.setBorder(null);
        noticeText.setFont(new Font(Font.DIALOG, Font.ITALIC, 11));
        noticeText.setForeground(this.getBackground());
        noticeText.setBackground(this.getBackground());

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(sound_effect_check)
                                .addComponent(saved_tab_load)
                                .addComponent(themeText)
                                .addComponent(lightButton)
                                .addComponent(darkButton)
                                .addComponent(customButton)
                                .addComponent(customColorTable)
                                .addComponent(noticeText))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(sound_effect_check)
                        .addComponent(saved_tab_load)
                        .addGap(10)
                        .addComponent(themeText)
                        .addComponent(lightButton)
                        .addComponent(darkButton)
                        .addComponent(customButton)
                        .addComponent(customColorTable)
                        .addGap(10)
                        .addComponent(noticeText)

        );

        // ================================================================================

        if (themeColor.equals(Color.WHITE)) {
            lightButton.setSelected(true);
            darkButton.setSelected(false);
            customButton.setSelected(false);
        } else if (themeColor.equals(PURE_BLACK)) {
            lightButton.setSelected(false);
            darkButton.setSelected(true);
            customButton.setSelected(false);
        } else {
            lightButton.setSelected(false);
            darkButton.setSelected(false);
            customButton.setSelected(true);
            customColorTable.setValueAt(themeColor, 0, 1);
        }

        this.add(settingPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setThemeColor(Color color) {
        themeColor = color;
    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if (command.equals(OK)) {
            if (lightButton.isSelected()) {
                setThemeColor(Color.WHITE);
            } else if (darkButton.isSelected()) {
                setThemeColor(PURE_BLACK);
            } else if (customButton.isSelected()) {
                setThemeColor((Color) customColorTable.getValueAt(0, 1));
            }
            // For windowns only
            BufferedWriter writer = null;
            File file = new File(FILE_PATH + "settings.ini");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(sound_effect_check.isSelected() + "---" + saved_tab_load.isSelected() + "---" + themeColor.getRGB());
                writer.newLine();
            } catch (IOException ex) {
            } finally {
                try {
                    writer.close();
                } catch (Exception ex) {
                }
            }
            super.dispose();

        } else if (command.equals(CANCEL)) {
            startUpCheck();
            super.dispose();
        } else if (command.equals(saved_tab_load)) {

        } else if (command.equals(LIGHT)) {
            lightButton.setSelected(true);
            darkButton.setSelected(false);
            customButton.setSelected(false);
            if (noticeText.getForeground().equals(this.getBackground())) {
                noticeText.setForeground(Color.RED);
            }
        } else if (command.equals(DARK)) {
            lightButton.setSelected(false);
            darkButton.setSelected(true);
            customButton.setSelected(false);
            if (noticeText.getForeground().equals(this.getBackground())) {
                noticeText.setForeground(Color.RED);
            }
        } else if (command.equals(CUSTOM)) {
            lightButton.setSelected(false);
            darkButton.setSelected(false);
            customButton.setSelected(true);
            if (noticeText.getForeground().equals(this.getBackground())) {
                noticeText.setForeground(Color.RED);
            }
        } else if (command.equals(SOUND_EFFECT)) {

        }
    }
}
