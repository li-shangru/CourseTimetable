package Utility;

import Main.Frame;
import Main.GPA_CALC;
import Main.Panel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class TabTitleEditListener extends MouseAdapter implements ChangeListener, DocumentListener {
    protected final JTextField editor = new JTextField();
    protected final JTabbedPane tabbedPane;
    protected int editingIdx = -1;
    protected int len = -1;
    protected Dimension dim;
    protected Component tabComponent;
    protected final Action startEditing = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            editingIdx = tabbedPane.getSelectedIndex();
            tabComponent = tabbedPane.getTabComponentAt(editingIdx);
            tabbedPane.setTabComponentAt(editingIdx, editor);
            editor.setVisible(true);
            editor.setText(tabbedPane.getTitleAt(editingIdx));
            editor.selectAll();
            editor.requestFocusInWindow();
            len = editor.getText().length();
            dim = editor.getPreferredSize();
            editor.setMinimumSize(dim);
        }
    };
    protected final Action cancelEditing = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (editingIdx >= 0) {
                tabbedPane.setTabComponentAt(editingIdx, tabComponent);
                editor.setVisible(false);
                editingIdx = -1;
                len = -1;
                tabComponent = null;
                editor.setPreferredSize(null);
                tabbedPane.requestFocusInWindow();
            }
        }
    };
    protected final Action renameTabTitle = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = editor.getText().trim();
            if (editingIdx >= 0 && !title.isEmpty()) {
                tabbedPane.setTitleAt(editingIdx, title);
            }
            cancelEditing.actionPerformed(null);
        }
    };

    public TabTitleEditListener(JTabbedPane tabbedPane) {
        super();
        this.tabbedPane = tabbedPane;
        editor.setBorder(BorderFactory.createEmptyBorder());
        editor.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                renameTabTitle.actionPerformed(null);
            }
        });
        InputMap im = editor.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = editor.getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel-editing");
        am.put("cancel-editing", cancelEditing);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "rename-tab-title");
        am.put("rename-tab-title", renameTabTitle);
        editor.getDocument().addDocumentListener(this);
        tabbedPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "start-editing");
        tabbedPane.getActionMap().put("start-editing", startEditing);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        renameTabTitle.actionPerformed(null);
        JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
        int tab = tabbedPane.getSelectedIndex();
        JComponent currentComponent = Main.Frame.tabbedPaneList.get(tab);
        if (currentComponent.getClass().equals(Panel.class)) {
            if (OSValidator.isWindows()) {
                Frame.ExportTab.setVisible(true);
                Frame.ImportTab.setVisible(true);
            }
            Frame.PrintTab.setVisible(true);
            ((Panel) currentComponent).getCourse().updateTable();
        } else if (currentComponent.getClass().equals(GPA_CALC.class)) {
            if (OSValidator.isWindows()) {
                Frame.ExportTab.setVisible(true);
                Frame.ImportTab.setVisible(true);
            }
            Frame.PrintTab.setVisible(false);
        } else {
            if (OSValidator.isWindows()) {
                Frame.ExportTab.setVisible(false);
                Frame.ImportTab.setVisible(false);
            }
            Frame.PrintTab.setVisible(false);
        }
        for (int i = 0; i < Main.Frame.tabbedPaneList.size(); i++) {
            currentComponent = Main.Frame.tabbedPaneList.get(i);
            if (i == tab) {
                currentComponent.setEnabled(true);
            } else {
                currentComponent.setEnabled(false);
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateTabSize();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateTabSize();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Rectangle r = tabbedPane.getBoundsAt(tabbedPane.getSelectedIndex());
        boolean isDoubleClick = e.getClickCount() >= 2;
        if (isDoubleClick && r.contains(e.getPoint())) {
            startEditing.actionPerformed(null);
        } else {
            renameTabTitle.actionPerformed(null);
        }
    }

    protected void updateTabSize() {
        editor.setPreferredSize(editor.getText().length() > len ? null : dim);
        tabbedPane.revalidate();
    }
}
