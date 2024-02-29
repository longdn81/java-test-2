

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class Student implements Serializable {
    private String name;
    private String id;
    private double gpa;

    public Student(String name, String id, double gpa) {
        this.name = name;
        this.id = id;
        this.gpa = gpa;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public double getGpa() {
        return gpa;
    }
}

public class bai9 extends JFrame {
    private ArrayList<Student> students;
    private JTextField nameField, idField, gpaField;
    private JTextArea displayArea;

    public bai9() {
        students = new ArrayList<>();

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);
        inputPanel.add(new JLabel("GPA:"));
        gpaField = new JTextField();
        inputPanel.add(gpaField);

        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });
        inputPanel.add(addButton);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });
        inputPanel.add(searchButton);

        displayArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);


        loadStudents();


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Student Manager");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addStudent() {
        String name = nameField.getText();
        String id = idField.getText();
        double gpa = Double.parseDouble(gpaField.getText());
        Student student = new Student(name, id, gpa);
        students.add(student);
        displayStudents();
        nameField.setText("");
        idField.setText("");
        gpaField.setText("");
    }

    private void searchStudent() {
        String searchTerm = JOptionPane.showInputDialog(this, "Enter student name:");
        if (searchTerm != null) {
            for (Student student : students) {
                if (student.getName().equalsIgnoreCase(searchTerm)) {
                    displayArea.setText("ID: " + student.getId() + "\nGPA: " + student.getGpa());
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Student not found!");
        }
    }

    private void displayStudents() {
        Collections.sort(students, Comparator.comparing(Student::getGpa));
        displayArea.setText("");
        for (Student student : students) {
            displayArea.append("Name: " + student.getName() + "\nID: " + student.getId() + "\nGPA: " + student.getGpa() + "\n\n");
        }
    }

    private void loadStudents() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("students.dat"))) {
            students = (ArrayList<Student>) ois.readObject();
            displayStudents();
        } catch (FileNotFoundException e) {
            displayArea.setText("No previous data found.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("students.dat"))) {
            oos.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new bai9();
            }
        });
    }

    @Override
    public void dispose() {
        saveStudents();
        super.dispose();
    }
}