package Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Controller {
    private static int index;
    private static boolean row_Is_Selected = false;
    private JTable coursetable;
    private JTable timetable;
    private Courses course;

    // ================================================================================

    public Controller(Panel panel, Courses course) {
        this.course = course;
        coursetable = panel.getCoursetable();
        timetable = panel.getTimetable();
    }

    // ================================================================================

    public void UpdateTimetable(String actionName) {
        DefaultTableModel model = (DefaultTableModel) coursetable.getModel();

        if (actionName.equals("AddCourse")) {
            course.add_course(new Courses(null, null, null, null, null, null, null, null, 0, null, Color.WHITE));
            course.add_row(new Courses(null, null, null, null, null, null, null, null, 0, null, Color.WHITE));
            course.updateTable();
        }

        // ================================================================================

        else if (actionName.equals("RemoveCourse")) {
            coursetable.editingCanceled(null);
            if (coursetable.getRowCount() > 1) {
                Courses.remove_course(Courses.getCourseList().get(coursetable.getRowCount() - 1));
                model.removeRow(model.getRowCount() - 1);
            } else if (coursetable.getRowCount() == 1) {
                Courses.getCourseList().clear();
                course.paint_table();
                course.add_row(new Courses(null, null, null, null, null, null, null, null, 0, null, Color.WHITE));
                model.removeRow(0);
            }
            course.updateTable();
        }

        // ================================================================================

        else if (actionName.equals("CopyCourse")) {
            course.updateTable();
            if (Courses.getCourseList().size() != 0 && Courses.getCourseList().get(coursetable.getRowCount() - 1).getName() != null) {
                course.add_row(Courses.getCourseList().get(Courses.getCourseList().size() - 1));
                course.add_course(Courses.getCourseList().get(Courses.getCourseList().size() - 1));
                course.updateTable();
            }
        }

        // ================================================================================

        else if (actionName.equals("MoveUp")) {
            if (!row_Is_Selected) {
                row_Is_Selected = true;
            }
            index = coursetable.getSelectedRow();
            if (index > 0) {
                model.moveRow(index, index, index - 1);
                coursetable.setRowSelectionInterval(index - 1, index - 1);
                course.updateTable();
            }
        }

        // ================================================================================

        else if (actionName.equals("MoveDown")) {
            if (!row_Is_Selected) {
                row_Is_Selected = true;
            }
            index = coursetable.getSelectedRow();
            if (index < model.getRowCount() - 1 && index != -1) {
                model.moveRow(index, index, index + 1);
                coursetable.setRowSelectionInterval(index + 1, index + 1);
                course.updateTable();
            }
        }

        // ================================================================================

        else if (actionName.equals("UpdateTable")) {
            coursetable.clearSelection();
            timetable.clearSelection();
            course.updateTable();
        }
    }
}
