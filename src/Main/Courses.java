package Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Courses {

    private static List<Courses> courseList;
    private Panel panel;
    private JTable coursetable;
    private JTable timetable;
    private String name = null;
    private String type = null;
    private String day = null;
    private String startTime = null;
    private String duration = null;
    private String location = null;
    private String cat = null;
    private String instructor = null;
    private String section = null;
    private double credits = 0;
    private Color color = Color.WHITE;

    // ================================================================================

    public Courses(Panel panel) {
        this.panel = panel;
        this.coursetable = panel.getCoursetable();
        this.timetable = panel.getTimetable();
        courseList = new ArrayList<Courses>();
        if (!Frame.importing) {
            add_course(new Courses(null, null, null, null, null, null, null, null, null, 0, Color.WHITE));
        }
    }

    // ================================================================================

    public Courses(String name, String type, String day, String startTime, String duration, String location, String cat, String instructor, String section, double credits, Color color) {
        this.name = name;
        this.type = type;
        this.day = day;
        this.startTime = startTime;
        this.duration = duration;
        this.location = location;
        this.cat = cat;
        this.instructor = instructor;
        this.section = section;
        this.credits = credits;
        this.color = color;
    }

    // ================================================================================

    public static List<Courses> getCourseList() {
        return courseList;
    }

    public static void remove_course(Courses course) {
        courseList.remove(course);
    }

    // ================================================================================

    public static int getRow(String timeString) {
        if (timeString == null) {
            return -1;
        } else if (timeString.equals("8:00")) {
            return 0;
        } else if (timeString.equals("8:30")) {
            return 1;
        } else if (timeString.equals("9:00")) {
            return 2;
        } else if (timeString.equals("9:30")) {
            return 3;
        } else if (timeString.equals("10:00")) {
            return 4;
        } else if (timeString.equals("10:30")) {
            return 5;
        } else if (timeString.equals("11:00")) {
            return 6;
        } else if (timeString.equals("11:30")) {
            return 7;
        } else if (timeString.equals("12:00")) {
            return 8;
        } else if (timeString.equals("12:30")) {
            return 9;
        } else if (timeString.equals("13:00")) {
            return 10;
        } else if (timeString.equals("13:30")) {
            return 11;
        } else if (timeString.equals("14:00")) {
            return 12;
        } else if (timeString.equals("14:30")) {
            return 13;
        } else if (timeString.equals("15:00")) {
            return 14;
        } else if (timeString.equals("15:30")) {
            return 15;
        } else if (timeString.equals("16:00")) {
            return 16;
        } else if (timeString.equals("16:30")) {
            return 17;
        } else if (timeString.equals("17:00")) {
            return 18;
        } else if (timeString.equals("17:30")) {
            return 19;
        } else if (timeString.equals("18:00")) {
            return 20;
        } else if (timeString.equals("18:30")) {
            return 21;
        } else if (timeString.equals("19:00")) {
            return 22;
        } else if (timeString.equals("19:30")) {
            return 23;
        } else if (timeString.equals("20:00")) {
            return 24;
        } else if (timeString.equals("20:30")) {
            return 25;
        } else if (timeString.equals("21:00")) {
            return 26;
        } else if (timeString.equals("21:30")) {
            return 27;
        } else if (timeString.equals("22:00")) {
            return 28;
        } else {
            return -1;
        }
    }

    public static int getColumn(String dayString) {
        if (dayString == null) {
            return -1;
        } else if (dayString.equals("Monday(M)")) {
            return 1;
        } else if (dayString.equals("Tuesday(T)")) {
            return 2;
        } else if (dayString.equals("Wednesday(W)")) {
            return 3;
        } else if (dayString.equals("Thrusday(R)")) {
            return 4;
        } else if (dayString.equals("Friday(F)")) {
            return 5;
        } else {
            return -1;
        }
    }

    public static int getDurationNum(String durationString) {
        if (durationString == null) {
            return -1;
        } else if (durationString.equals("30min")) {
            return 0;
        } else if (durationString.equals("60min")) {
            return 1;
        } else if (durationString.equals("90min")) {
            return 2;
        } else if (durationString.equals("120min")) {
            return 3;
        } else if (durationString.equals("150min")) {
            return 4;
        } else if (durationString.equals("180min")) {
            return 5;
        } else if (durationString.equals("210min")) {
            return 6;
        } else if (durationString.equals("240min")) {
            return 7;
        } else if (durationString.equals("270min")) {
            return 8;
        } else if (durationString.equals("300min")) {
            return 9;
        } else {
            return -1;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    // ================================================================================

    public Courses getCourseFromRow(int i) {
        String new_name = (String) coursetable.getValueAt(i, 0);
        String new_type = (String) coursetable.getValueAt(i, 1);
        String new_day = (String) coursetable.getValueAt(i, 2);
        String new_startTime = (String) coursetable.getValueAt(i, 3);
        String new_duration = (String) coursetable.getValueAt(i, 4);
        String new_location = (String) coursetable.getValueAt(i, 5);
        String new_cat = (String) coursetable.getValueAt(i, 6);
        String new_instructor = (String) coursetable.getValueAt(i, 7);
        String new_section = (String) coursetable.getValueAt(i, 8);
        double new_credits = (double) coursetable.getValueAt(i, 9);
        Color new_color = (Color) coursetable.getValueAt(i, 10);

        if (new_name != null) {
            if (new_name.isEmpty()) {
                new_name = null;
            }
        }
        if (new_type != null) {
            if (new_type.isEmpty()) {
                new_type = null;
            }
        }
        if (new_day != null) {
            if (new_day.isEmpty()) {
                new_day = null;
            }
        }
        if (new_startTime != null) {
            if (new_startTime.isEmpty()) {
                new_startTime = null;
            }
        }
        if (new_duration != null) {
            if (new_duration.isEmpty()) {
                new_duration = null;
            }
        }
        if (new_location != null) {
            if (new_location.isEmpty()) {
                new_location = null;
            }
        }
        if (new_cat != null) {
            if (new_cat.isEmpty()) {
                new_cat = null;
            }
        }
        if (new_instructor != null) {
            if (new_instructor.isEmpty()) {
                new_instructor = null;
            }
        }
        if (new_section != null) {
            if (new_section.isEmpty()) {
                new_section = null;
            }
        }
        Courses newcourse = new Courses(new_name, new_type, new_day, new_startTime, new_duration, new_location, new_cat, new_instructor, new_section, new_credits, new_color);
        return newcourse;
    }

    public void setCourse(Courses newCourse) {
        this.name = newCourse.getName();
        this.type = newCourse.getType();
        this.day = newCourse.getDay();
        this.startTime = newCourse.getStartTime();
        this.duration = newCourse.getDuration();
        this.location = newCourse.getLocation();
        this.cat = newCourse.getCat();
        this.instructor = newCourse.getInstructor();
        this.section = newCourse.getSection();
        this.credits = newCourse.getCredits();
        this.color = newCourse.getColor();
    }

    public Courses revalidate(Courses course) {
        String new_name = course.getName();
        String new_type = course.getType();
        String new_day = course.getDay();
        String new_startTime = course.getStartTime();
        String new_duration = course.getDuration();
        String new_location = course.getLocation();
        String new_cat = course.getCat();
        String new_instructor = course.getInstructor();
        String new_section = course.getSection();

        if (new_name.contains("null")) {
            new_name = null;
        }
        if (new_type.contains("null")) {
            new_type = null;
        }
        if (new_day.contains("null")) {
            new_day = null;
        }
        if (new_startTime.contains("null")) {
            new_startTime = null;
        }
        if (new_duration.contains("null")) {
            new_duration = null;
        }
        if (new_location.contains("null")) {
            new_location = null;
        }
        if (new_cat.contains("null")) {
            new_cat = null;
        }
        if (new_instructor.contains("null")) {
            new_instructor = null;
        }
        if (new_section.contains("null")) {
            new_section = null;
        }
        return new Courses(new_name, new_type, new_day, new_startTime, new_duration, new_location, new_cat, new_instructor, new_section, course.getCredits(), course.getColor());
    }

    public void add_row(Courses course) {
        String new_name = course.getName();
        String new_type = course.getType();
        String new_day = course.getDay();
        String new_startTime = course.getStartTime();
        String new_duration = course.getDuration();
        String new_location = course.getLocation();
        String new_cat = course.getCat();
        String new_instructor = course.getInstructor();
        String new_section = course.getSection();
        double new_credits = course.getCredits();
        Color new_color = course.getColor();

        DefaultTableModel model = (DefaultTableModel) coursetable.getModel();
        Vector<Object> rowVector = new Vector<>();
        rowVector.add(new_name);
        rowVector.add(new_type);
        rowVector.add(new_day);
        rowVector.add(new_startTime);
        rowVector.add(new_duration);
        rowVector.add(new_location);
        rowVector.add(new_cat);
        rowVector.add(new_instructor);
        rowVector.add(new_section);
        rowVector.add(new_credits);
        rowVector.add(new_color);
        model.addRow(rowVector);
    }

    // ================================================================================

    public void switchPosition(int c1, int c2) {
        Courses course1 = courseList.get(c1);
        Courses course2 = courseList.get(c2);
        courseList.get(c1).setCourse(course2);
        courseList.get(c2).setCourse(course1);
    }

    // ================================================================================

    public void change_course(Courses course) {
        remove_course(course);
        add_course(course);
    }

    public void add_course(Courses course) {
        courseList.add(course);
    }


    // ================================================================================

    public void updateTable() {
        courseList.clear();
        for (int i = 0; i < coursetable.getRowCount(); i++) {
            String name = (String) coursetable.getValueAt(i, 0);
            String type = (String) coursetable.getValueAt(i, 1);
            String day = (String) coursetable.getValueAt(i, 2);
            String startTime = (String) coursetable.getValueAt(i, 3);
            String duration = (String) coursetable.getValueAt(i, 4);
            String location = (String) coursetable.getValueAt(i, 5);
            String cat = (String) coursetable.getValueAt(i, 6);
            String instructor = (String) coursetable.getValueAt(i, 7);
            String section = (String) coursetable.getValueAt(i, 8);
            double credits = (Double) coursetable.getValueAt(i, 9);
            Color color = (Color) coursetable.getValueAt(i, 10);
            if (name != null) {
                if (name.length() == 0) {
                    name = null;
                }
            }
            if (type != null) {
                if (type.length() == 0) {
                    type = null;
                }
            }
            if (day != null) {
                if (day.length() == 0) {
                    day = null;
                }
            }
            if (startTime != null) {
                if (startTime.length() == 0) {
                    startTime = null;
                }
            }
            if (duration != null) {
                if (duration.length() == 0) {
                    duration = null;
                }
            }
            if (location != null) {
                if (location.length() == 0) {
                    location = null;
                }
            }
            if (cat != null) {
                if (cat.length() == 0) {
                    cat = null;
                }
            }
            if (instructor != null) {
                if (instructor.length() == 0) {
                    instructor = null;
                }
            }
            if (section != null) {
                if (section.length() == 0) {
                    section = null;
                }
            }
            courseList.add(new Courses(name, type, day, startTime, duration, location, cat, instructor, section, credits, color));
        }
        paint_table();
    }

    public void rePaint() {
        for (int i = 0; i < timetable.getRowCount(); i++) {
            for (int j = 1; j < 6; j++) {
                timetable.setValueAt(" ", i, j);
            }
        }
    }

    public void paint_table() {
        rePaint();
        for (int i = 0; i < courseList.size(); i++) {
            Courses course = courseList.get(i);
            int row = getRow(course.getStartTime());
            int col = getColumn(course.getDay());
            int dur = getDurationNum(course.getDuration());
            int offset = 0;
            if (dur != -1 && row != -1 && col != -1 && course.getName() != null && checkTime(row, dur)) {
                for (int j = 0; j < dur + 1; j++) {
                    if (timetable.getValueAt(row + j, col) != " ") {
                        timetable.setValueAt("CONFILT!", row + j, col);
                    } else {
                        if (dur <= 2) {
                            offset = 0;
                        } else {
                            offset = dur / 3;
                        }
                        if (j == offset) {
                            timetable.setValueAt(getIdentifier(course, "name"), row + j, col);
                        } else if (j == offset + 1) {
                            if (course.getType() != null && course.getLocation() == null) {
                                timetable.setValueAt(getIdentifier(course, "type"), row + j, col);
                            } else if (course.getLocation() != null && course.getType() == null) {
                                timetable.setValueAt(getIdentifier(course, "loc"), row + j, col);
                            } else if (course.getLocation() != null && course.getType() != null) {
                                timetable.setValueAt(getIdentifier(course, "type_loc"), row + j, col);
                            } else if (course.getType() == null && course.getLocation() == null && j == dur) {
                                timetable.setValueAt(getIdentifier(course, "bot"), row + j, col);
                            } else if (course.getType() == null && course.getLocation() == null && j < dur) {
                                timetable.setValueAt(getIdentifier(course, "mid"), row + j, col);
                            } else {
                                timetable.setValueAt(getIdentifier(course, "top"), row + j, col);
                            }
                        } else if (j == offset + 2) {
                            if (course.getSection() != null && course.getCredits() == 0) {
                                timetable.setValueAt(getIdentifier(course, "sec"), row + j, col);
                            } else if (course.getSection() == null && course.getCredits() != 0) {
                                timetable.setValueAt(getIdentifier(course, "cre"), row + j, col);
                            } else if (course.getSection() != null && course.getCredits() != 0) {
                                timetable.setValueAt(getIdentifier(course, "sec_cre"), row + j, col);
                            } else if (course.getSection() == null && course.getCredits() == 0 && j == dur) {
                                timetable.setValueAt(getIdentifier(course, "bot"), row + j, col);
                            } else if (course.getSection() == null && course.getCredits() == 0 && j < dur) {
                                timetable.setValueAt(getIdentifier(course, "mid"), row + j, col);
                            } else {
                                timetable.setValueAt(getIdentifier(course, "top"), row + j, col);
                            }
                        } else if (offset != 0 && j == 0) {
                            timetable.setValueAt(getIdentifier(course, "top"), row + j, col);
                        } else if (offset != 0 && j == dur) {
                            timetable.setValueAt(getIdentifier(course, "bot"), row + j, col);
                        } else {
                            timetable.setValueAt(getIdentifier(course, "mid"), row + j, col);
                        }

                    }
                }
            }
        }
    }

    public boolean checkTime(int row, int dur) {
        if (row + dur > 28) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(panel, "Course time exceeds table border.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    public String getIdentifier(Courses course, String type) {
        String topBorder = "top";
        String botBorder = "bot";
        String midBorder = "mid";
        String ID = course.getName() + course.getStartTime() + course.getDuration() + course.getDay();
        String result;

        if (type.equals("top")) {
            result = ID + topBorder;
            return result;
        } else if (type.equals("bot")) {
            result = ID + botBorder;
            return result;
        } else if (type.equals("mid")) {
            result = ID + midBorder;
            return result;
        } else if (type.equals("name")) {
            result = ID + course.getName();
            return result;
        } else if (type.equals("type")) {
            result = ID + course.getType();
            return result;
        } else if (type.equals("loc")) {
            result = ID + course.getLocation();
            return result;
        } else if (type.equals("type_loc")) {
            result = ID + course.getType() + course.getLocation();
            return result;
        } else if (type.equals("sec")) {
            result = ID + course.getSection();
            return result;
        } else if (type.equals("cre")) {
            result = ID + course.getCredits();
            return result;
        } else if (type.equals("sec_cre")) {
            result = ID + course.getSection() + course.getCredits();
            return result;
        } else {
            throw new IllegalArgumentException("Wrong type");
        }

    }
}
