package GUI;

import ClashDetectionKotlin.KotlinDetector;
import ClashDetectionScala.ScalaDetector;
import ClashDetectionScala.ScalaDetectorViaModels;
import scala.jdk.javaapi.CollectionConverters;

import Persistence.Entities.activity.ActivityModel;
import Persistence.Entities.activity_category.ActivityCategoryModel;
import Persistence.Entities.course_module.CourseModuleModel;
import Timetable.Timetable;
import Timetable.Week;
import Timetable.Day;
import Timetable.Activity;
import Timetable.Module;
import Utils.MultiLineCellRenderer;
import use_cases.activity.insert_activity.InsertActivity;
import use_cases.activity.insert_activity.InsertActivityResult;
import use_cases.activity.remove_activity.RemoveActivity;
import use_cases.course_module.insert_course_module.InsertCourseModule;
import use_cases.course_module.insert_course_module.InsertCourseModuleResult;
import use_cases.course_module.insert_course_module.UseCaseError;
import use_cases.course_module.remove_course_module.RemoveCourseModule;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

public class MainScreen extends JFrame{
    private DefaultTableModel TableModel;
    private final HashMap<Double, Integer> doubleTimeSlotToInt;
    public Timetable table;
    private JLabel courseNameLabel;
    private JButton insertActivityButton;
    private JButton removeActivityButton;
    private JList courseModuleJList;
    private JButton addCourseModuleButton;
    private JButton removeCourseModuleButton;
    private JRadioButton year1RadioButton;
    private JRadioButton year2RadioButton;
    private JRadioButton year3RadioButton;
    private JRadioButton Term1RadioButton;
    private JLabel yearLabel;
    private JLabel termLabel;
    private JLabel courseModulesLabel;
    private JLabel manageActivitesLabel;
    private JRadioButton Term2RadioButton;
    private JPanel panelMain;
    private JPanel yearAndTermPanel;
    private JPanel manageActivitiesPanel;
    private JPanel courseModulesPanel;
    private JPanel menuPanel;
    private JTable timeTable;
    private JPanel tablePanel;
    private JScrollPane ScrollPane;
    private JRadioButton Week1RadioButton;
    private JButton displayTableButton;
    private JLabel weekLabel;
    private JRadioButton Week2RadioButton;
    private JRadioButton kotlinClashDetectionRadioButton;
    private JRadioButton scalaClashDetectionRadioButton;

    private Logger logger = Logger.getLogger("MainScreen");

    public MainScreen(Timetable timetable){
        super("Main Page")
        this.doubleTimeSlotToInt = new HashMap<Double, Integer>();
        this.table = timetable;

        double counter = 9.0;
        int counter_= 0;
        while (counter < 21.5) {
            this.doubleTimeSlotToInt.put(counter, counter_);
            counter = counter +0.5;
            counter_ ++;
        }

        // GUI CODE
        this.courseNameLabel.setText(timetable.getDisplayLabel());
        this.yearAndTermPanel.setBorder(new MatteBorder(0, 0, 0, 1, Color.black));
        this.courseModulesPanel.setBorder(new MatteBorder(0, 0, 0, 1, Color.black));
        this.yearLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.black));
        this.termLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.black));
        this.courseModulesLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.black));
        this.manageActivitesLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.black));
        this.timeTable.setVisible(true);
        this.timeTable.setOpaque(false);
        String[] columns = new String[] {
                "Time Slot", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"
        };
        this.TableModel = new DefaultTableModel(25, 6);
        TableModel.setColumnIdentifiers(columns);
        this.timeTable.setRowHeight(75);
        this.panelMain.setPreferredSize(new Dimension(1000, 700));
        this.setContentPane(this.panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        // GUI CODE
        if (table.getCourseType().equals("postgraduate")){ //Postgraduate
            this.year2RadioButton.setEnabled(false);
            this.year3RadioButton.setEnabled(false);
        }

        // GUI CODE
        ButtonGroup yearGroupRadioGroup = new ButtonGroup();
        yearGroupRadioGroup.add(year1RadioButton);
        year1RadioButton.setActionCommand("1");
        yearGroupRadioGroup.add(year2RadioButton);
        year2RadioButton.setActionCommand("2");
        yearGroupRadioGroup.add(year3RadioButton);
        year3RadioButton.setActionCommand("3");
        ButtonGroup termGroupRadioGroup = new ButtonGroup();
        termGroupRadioGroup.add(Term1RadioButton);
        Term1RadioButton.setActionCommand("1");
        termGroupRadioGroup.add(Term2RadioButton);
        Term2RadioButton.setActionCommand("2");
        ButtonGroup weekGroupRadioGroup = new ButtonGroup();
        weekGroupRadioGroup.add(Week1RadioButton);
        Week1RadioButton.setActionCommand("1");
        weekGroupRadioGroup.add(Week2RadioButton);
        Week2RadioButton.setActionCommand("2");

        // UPDATES TABLE TO DISPLAY LOADED DATA (ASSUMING TABLE DATA EXISTS IN DATABASE)
        update(Integer.parseInt(yearGroupRadioGroup.getSelection().getActionCommand()), Integer.parseInt(termGroupRadioGroup.getSelection().getActionCommand()), Integer.parseInt(weekGroupRadioGroup.getSelection().getActionCommand()), table);

        // LISTENER FOR THE ADD COURSE MODULE BUTTON
        addCourseModuleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // GUI CODE
                JPanel panel = new JPanel(new GridLayout(5, 3));
                JLabel moduleIDLabel = new JLabel();
                JLabel moduleNameLabel = new JLabel();
                JLabel moduleIsOptionalLabel = new JLabel();
                JTextField moduleIDTextField = new JTextField(10);
                JTextField moduleNameTextField = new JTextField(10);
                moduleIDLabel.setText("Module ID:");
                moduleNameLabel.setText("Module Name");
                moduleIsOptionalLabel.setText("Is optional:");
                JRadioButton trueRadioButton = new JRadioButton();
                trueRadioButton.setActionCommand("True");
                JRadioButton falseRadioButton = new JRadioButton();
                falseRadioButton.setActionCommand("False");
                trueRadioButton.setText("True");
                falseRadioButton.setText("False");
                ButtonGroup optionalChoiceGroup = new ButtonGroup();
                optionalChoiceGroup.add(trueRadioButton);
                optionalChoiceGroup.add(falseRadioButton);
                panel.add(moduleNameLabel);
                panel.add(moduleNameTextField);
                panel.add(moduleIsOptionalLabel);
                panel.add(trueRadioButton);
                panel.add(falseRadioButton);

                Hashtable<String, Boolean> trueFalseDict = new Hashtable<String, Boolean>();
                trueFalseDict.put("True", true);
                trueFalseDict.put("False", false);

                // INPUT FORM PRESENTED
                JOptionPane.showMessageDialog(panelMain, panel);

                if (!moduleNameTextField.getText().isEmpty() && optionalChoiceGroup.getSelection() != null){ // INPUT VALIDATION
                    if (timetable.getID() != null) {
                        try {
                            // CREATES A NEW MODULE AND ITS IT TO THE COURSE
                            InsertCourseModule icm = new InsertCourseModule(timetable.getID(), moduleNameTextField.getText(), trueFalseDict.get(optionalChoiceGroup.getSelection().getActionCommand()));
                            InsertCourseModuleResult res = icm.insert();
                            CourseModuleModel courseModuleModel = res.getCourseModuleModel();
                            table.addModule(courseModuleModel.getId_course_module(), moduleNameTextField.getText(), trueFalseDict.get(optionalChoiceGroup.getSelection().getActionCommand()));
                            updateModulesList(table);
                        } catch (UseCaseError insertError) {
                            logger.warning(insertError.getMessage());
                            insertError.printStackTrace();
                            String messageToDisplay = insertError.getMessageToDisplay();
                            String title = insertError.getTitleToDisplay();
                            JOptionPane.showMessageDialog(panelMain, messageToDisplay, title, JOptionPane.ERROR_MESSAGE);
                        }


                    } else {
                        JOptionPane.showMessageDialog(panelMain, "Module ID already exists!", "Module ID Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(panelMain, "Please ensure all fields have correct input!", "Add Module Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // LISTENER FOR THE DISPLAY TABLE BUTTON
        displayTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // UPDATES TABLE TO DISPLAY LOADED DATA
                update(Integer.parseInt(yearGroupRadioGroup.getSelection().getActionCommand()), Integer.parseInt(termGroupRadioGroup.getSelection().getActionCommand()), Integer.parseInt(weekGroupRadioGroup.getSelection().getActionCommand()), table);
            }
        });

        // LISTENER FOR THE REMOVE COURSE MODULE BUTTON
        removeCourseModuleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (courseModuleJList.getSelectedValue() != null){ // THE COURSE THAT IS BEING REMOVED MUST BE SELECTED FROM A JLIST PANE
                    int result = JOptionPane.showConfirmDialog(panelMain,"Are you sure you will like to remove the module?", "Remove Module",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if(result == JOptionPane.YES_OPTION){
                        int moduleID = Integer.parseInt(String.valueOf(courseModuleJList.getSelectedValue().toString().charAt(4)));
                        try {
                            RemoveCourseModule removeCourseModule = new RemoveCourseModule();
                            removeCourseModule.remove(moduleID);
                            timetable.removeModule(moduleID);
                            update(Integer.parseInt(yearGroupRadioGroup.getSelection().getActionCommand()), Integer.parseInt(termGroupRadioGroup.getSelection().getActionCommand()), Integer.parseInt(weekGroupRadioGroup.getSelection().getActionCommand()), table);
                            JOptionPane.showMessageDialog(panelMain, "Module and all relevant activities have successfully been removed.", "Success", JOptionPane.PLAIN_MESSAGE);
                        } catch (UseCaseError removeError) {
                            removeError.printStackTrace();
                            logger.warning(removeError.getMessage());
                            String message = removeError.getMessageToDisplay();
                            JOptionPane.showMessageDialog(panelMain, message, "Remove Module Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else if (result == JOptionPane.NO_OPTION){
                        JOptionPane.showMessageDialog(panelMain, "Module has not been removed", "Module removal", JOptionPane.PLAIN_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(panelMain, "No Module has been selected", "Remove Module Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(panelMain, "No Module has been selected", "Remove Module Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        insertActivityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // GUI / POPUP MENU CODE
                JPanel panel = new JPanel(new GridLayout(12, 2));

                JLabel activityIDLabel = new JLabel();
                JLabel activityYearLabel = new JLabel();
                JLabel activityTermLabel = new JLabel();
                JLabel activityWeekLabel = new JLabel();
                JLabel activityDayLabel = new JLabel();
                JLabel moduleIDLabel = new JLabel();
                JLabel activityStartTimeLabel = new JLabel();
                JLabel activityDurationTimeLabel = new JLabel();
                JLabel activityTypeLabel = new JLabel();
                JTextField activityIDTextField = new JTextField(10);
                activityIDLabel.setText("Activity ID:");
                activityYearLabel.setText("Year:");
                activityTermLabel.setText("Term:");
                activityWeekLabel.setText("Week:");
                activityDayLabel.setText("Day:");
                moduleIDLabel.setText("Module ID:");
                activityStartTimeLabel.setText("Start Time:");
                activityDurationTimeLabel.setText("Duration:");
                activityTypeLabel.setText("Activity Type:");
                panel.add(activityYearLabel);

                List<Integer> years = new ArrayList<Integer>();
                if (table.getCourseType().equals("postgraduate")){ //Postgraduate
                    years.add(1);
                }
                else {
                    years.add(1);
                    years.add(2);
                    years.add(3);
                }

                JComboBox activityYearComboBox = new JComboBox(years.toArray());
                panel.add(activityYearComboBox);

                panel.add(activityTermLabel);
                Integer terms[] = { 1, 2 };
                JComboBox activityTermComboBox = new JComboBox(terms);
                panel.add(activityTermComboBox);

                panel.add(activityWeekLabel);
                Integer weeks[] = { 1, 2 };
                JComboBox activityWeekComboBox = new JComboBox(weeks);
                panel.add(activityWeekComboBox);

                panel.add(activityDayLabel);
                String days[] = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
                JComboBox activityDayComboBox = new JComboBox(days);
                panel.add(activityDayComboBox);

                panel.add(moduleIDLabel);
                List<Integer> moduleOptions = new ArrayList<Integer>();
                for (Integer mod : timetable.getModules().keySet()){
                    moduleOptions.add(mod);
                }
                JComboBox moduleIDComboBox = new JComboBox(moduleOptions.toArray());
                panel.add(moduleIDComboBox);

                panel.add(activityStartTimeLabel);
                List<Double> timeOptions = new ArrayList<Double>();
                double x = 9.0;
                while (x < 21.5){
                    timeOptions.add(x);
                    x += 0.5;
                }
                JComboBox activityStartTimeComboBox = new JComboBox(timeOptions.toArray());
                panel.add(activityStartTimeComboBox);

                panel.add(activityDurationTimeLabel);
                Double durations[] = { 1.0, 1.5, 2.0};
                JComboBox activityDurationComboBox = new JComboBox(durations);
                panel.add(activityDurationComboBox);

                panel.add(activityTypeLabel);

                ArrayList<ActivityCategoryModel> results = new ActivityCategoryModel().selectAll();
                List typesList = new ActivityCategoryModel().labelsToArray(results);

                String[] types = (String[]) typesList.toArray(new String[typesList.size()]);

                JComboBox activityTypeComboBox = new JComboBox(types);
                panel.add(activityTypeComboBox);

                JOptionPane.showMessageDialog(panelMain, panel);

                Double startTime = (Double) activityStartTimeComboBox.getSelectedItem();
                Double duration = (Double) activityDurationComboBox.getSelectedItem();
                Double endTime = startTime + duration;
                InsertActivity insertActivity = new InsertActivity();
                insertActivity.setYear((Integer) activityYearComboBox.getSelectedItem());
                insertActivity.setTerm((Integer) activityTermComboBox.getSelectedItem());
                insertActivity.setWeek((Integer) activityWeekComboBox.getSelectedItem());
                insertActivity.setWeekDay(activityDayComboBox.getSelectedIndex());
                insertActivity.setWeekDay(activityDayComboBox.getSelectedIndex());
                insertActivity.setCourseModuleId((Integer) moduleIDComboBox.getSelectedItem());
                insertActivity.setStartTime((Double) activityStartTimeComboBox.getSelectedItem());
                insertActivity.setEndTime(endTime);
                insertActivity.setActivityCategoryId(activityTypeComboBox.getSelectedIndex() + 1);
                InsertActivityResult newActivityResult = insertActivity.insert();

                ActivityModel newActivity = newActivityResult.getActivityModel();

                timetable.addActivity(newActivity.getId_activity(),
                        newActivity.getYear(),
                        newActivity.getTerm(),
                        newActivity.getWeek(),
                        newActivity.getDay_week(),
                        newActivity.getId_course_module(),
                        newActivity.getAct_starttime(),
                        newActivity.getAct_endtime() - newActivity.getAct_starttime(),
                        newActivity.getId_act_category());
                update(Integer.parseInt(yearGroupRadioGroup.getSelection().getActionCommand()), Integer.parseInt(termGroupRadioGroup.getSelection().getActionCommand()), Integer.parseInt(weekGroupRadioGroup.getSelection().getActionCommand()), table);
            }
        });

        removeActivityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel(new GridLayout(5, 3));
                JLabel activityIDLabel = new JLabel();
                activityIDLabel.setText("Activity ID:");

                panel.add(activityIDLabel);
                List<Integer> activityOptions = new ArrayList<Integer>();
                for (Integer act : timetable.getActivities().keySet()){
                    activityOptions.add(act);
                }
                JComboBox activityIDComboBox = new JComboBox(activityOptions.toArray());
                panel.add(activityIDComboBox);

                JOptionPane.showMessageDialog(panelMain, panel);

                int result = JOptionPane.showConfirmDialog(panelMain,"Are you sure you will like to remove the activity?", "Remove Activity",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if(result == JOptionPane.YES_OPTION){
                    int targetIdToRemove = (Integer) activityIDComboBox.getSelectedItem();
                    try {
                        RemoveActivity activityRemover = new RemoveActivity();
                        activityRemover.removeById(targetIdToRemove);
                        timetable.removeActivity((Integer) activityIDComboBox.getSelectedItem());
                        update(Integer.parseInt(yearGroupRadioGroup.getSelection().getActionCommand()), Integer.parseInt(termGroupRadioGroup.getSelection().getActionCommand()), Integer.parseInt(weekGroupRadioGroup.getSelection().getActionCommand()), table);
                        JOptionPane.showMessageDialog(panelMain, "Activity has successfully been removed.", "Success", JOptionPane.PLAIN_MESSAGE);
                    } catch (UseCaseError useCaseError) {
                        String title = useCaseError.getTitleToDisplay();
                        String message = useCaseError.getMessageToDisplay();


                        JOptionPane.showMessageDialog(panelMain, message, title, JOptionPane.ERROR_MESSAGE);
                    }

                }
                else if (result == JOptionPane.NO_OPTION){
                    JOptionPane.showMessageDialog(panelMain, "Activity has not been removed", "Activity removal", JOptionPane.PLAIN_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(panelMain, "No Activity has been removed", "Remove Activity Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    public void updateModulesList(Timetable Timetable){
        DefaultListModel listOfModules = new DefaultListModel();
        Collection<Module> modules = Timetable.getModules().values();
        for (Module mod : modules){
            listOfModules.addElement(mod.toString());
        }
        this.courseModuleJList.setModel(listOfModules);
    }

    public void update(int Year, int Term, int Week, Timetable Timetable){

        double time = 9.0;
        int row = 0;
        while(time < 21.5){
            this.TableModel.setValueAt(time, row, 0);
            time += 0.5;
            row += 1;
        }

        Week week = Timetable.getTable().get(Year).getTerms().get(Term).getWeeks().get(Week);
        for (Day day : week.getDays().values()){
            HashMap<Double, List<Activity>> timeslot = day.getTimeSlot();
            for (Map.Entry<Double, List<Activity>> slot : timeslot.entrySet()){
                if (slot.getValue() != null){
                    StringBuilder activities = new StringBuilder();
                    for (Activity act : slot.getValue()){
                        activities.append(act.toString()).append("\n");
                    }
                    this.TableModel.setValueAt(activities, this.doubleTimeSlotToInt.get(slot.getKey()), day.getDayNumber()+1);
                }
                else {
                    this.TableModel.setValueAt("", this.doubleTimeSlotToInt.get(slot.getKey()), day.getDayNumber()+1);
                }
            }
        }
        this.timeTable.setModel(this.TableModel);

        MultiLineCellRenderer renderer = new MultiLineCellRenderer();

        // CALLS EITHER KOTLIN CLASH DETECTION OR SCALA CLASH DETECTION BASED ON USERS CHOICE
        if(this.kotlinClashDetectionRadioButton.isSelected()){
            KotlinDetector kotlinDetector = new KotlinDetector(Timetable);
            renderer.clashDetectionInitiate(kotlinDetector.detect(), Year, Term, Week, 0);
        }
        else{
            ScalaDetectorViaModels scalaDetectorViaModels = new ScalaDetectorViaModels();
            Set<Integer> clashIds = scalaDetectorViaModels.simpleGetActivitiesIdsClashesSetAsJava(new ActivityModel().selectAll().stream().toList());
            System.out.println("clashIds: " + clashIds);
            java.util.List<java.util.List<Integer>> clashSlots = scalaDetectorViaModels.getTableSlotsAsJava(Timetable, clashIds);
            renderer.clashDetectionInitiate(clashSlots, Year, Term, Week, 1);
        }

        //SETS RELEVENT CLASH DETECTION SYSTEM RENDERER TO THE JTABLE OBJECT
        this.timeTable.setDefaultRenderer(Object.class, renderer);
        this.timeTable.getColumnModel().getColumn(0).setPreferredWidth(2);

        updateModulesList(Timetable);

    }

}

