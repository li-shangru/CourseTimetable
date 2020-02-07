package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PrinterException;

import static Main.Frame.FILE_PATH;


public class Listener implements KeyListener, ActionListener {

    private Frame frame;

    public Listener(Frame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if (command.equals(Frame.NEW_TAB)) {
            frame.add_tab(new Panel(), ("Term " + (Frame.tabbedPaneList.size() + 1)));
        } else if (command.equals(Frame.CLOSE_TAB)) {
            if (Frame.tabbedPaneList.size() == 1) {
                if (SettingsFrame.sound_effect_check.isSelected()) {
                    Toolkit.getDefaultToolkit().beep();
                }
                if (JOptionPane.showConfirmDialog(frame, "Are you sure to exit?", "Please Confirm", JOptionPane.YES_NO_OPTION) == 0) {
                    System.exit(0);
                }
            } else {
                if (frame.checkSaved()) {
                    Frame.remove_tab(Frame.tabbedPane.getSelectedIndex());
                }
            }
        } else if (command.equals(Frame.EXPORT)) {
            String file = FILE_PATH + Frame.tabbedPane.getTitleAt(Frame.tabbedPane.getSelectedIndex()) + ".txt";
            frame.exportData(file);
        } else if (command.equals(Frame.IMPORT)) {
            try {
                Frame.importing = true;
                frame.importData("normal");
                Frame.importing = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (command.equals(Frame.GPA_CAL)) {
            GPA_CALC gpa_CALC = new GPA_CALC();
            frame.add_tab(gpa_CALC, Frame.GPA_CAL);
        } else if (command.equals(Frame.CHANGE)) {
            Changelog changelog = new Changelog();
            frame.add_tab(changelog, Frame.CHANGE);
        } else if (command.equals(Frame.EXIT)) {
            System.exit(0);
        } else if (command.equals(Frame.SETTINGS)) {
            new SettingsFrame();
        } else if (command.equals(Frame.PRINT)) {
            try {
                boolean complete = ((Panel) Frame.tabbedPane.getSelectedComponent()).getTimetable().print();
                if (complete) {
                    if (SettingsFrame.sound_effect_check.isSelected()) {
                        Toolkit.getDefaultToolkit().beep();
                    }
                    JOptionPane.showMessageDialog(frame, "Printing Complete", "Printing Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    if (SettingsFrame.sound_effect_check.isSelected()) {
                        Toolkit.getDefaultToolkit().beep();
                    }
                    JOptionPane.showMessageDialog(frame, "Printing Cancelled", "Printing Result", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (PrinterException pe) {
                if (SettingsFrame.sound_effect_check.isSelected()) {
                    Toolkit.getDefaultToolkit().beep();
                }
                JOptionPane.showMessageDialog(frame, "Printing Failed: " + pe.getMessage(), "Printing Result", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
