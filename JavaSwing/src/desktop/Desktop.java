package desktop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Desktop
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Daily Task Book");
        StartButtonWindow startButtonWindow = new StartButtonWindow();
    }
}

class StartButtonWindow extends JFrame
{
    private JButton startButton = new JButton("Start");

    public StartButtonWindow()
    {
        setTitle("Daily Task Book");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame selectTask = new JFrame("Select Task");
                selectTask.setSize(800, 600);
                selectTask.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                selectTask.setLocationRelativeTo(null);

                JPanel buttonPanel = new JPanel(new GridLayout(5, 8));

                JButton addPersonalTasks = new JButton("Добавить свои задачи");
                JButton currentTasks = new JButton("Текущие задачи");
                JButton addSharedTasks = new JButton("Добавить совместные задачи");
                JButton sharedTasks = new JButton("Совместные задачи");

                buttonPanel.add(addPersonalTasks);
                buttonPanel.add(currentTasks);
                buttonPanel.add(addSharedTasks);
                buttonPanel.add(sharedTasks);

                currentTasks.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        JFrame currentTasks = new JFrame("Текущие задачи");
                        currentTasks.setSize(700, 500);
                        currentTasks.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        currentTasks.setLocationRelativeTo(null); //центрирование
                        currentTasks.setVisible(true);
                    }
                });

                sharedTasks.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        JFrame sharedTasks = new JFrame("Текущие задачи");
                        sharedTasks.setSize(700, 500);
                        sharedTasks.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        sharedTasks.setLocationRelativeTo(null); //центрирование
                        sharedTasks.setVisible(true);
                    }
                });

                addSharedTasks.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        JFrame inputFrame = new JFrame("Введите совместную задачу");
                        inputFrame.setSize(400, 200);
                        inputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        inputFrame.setLocationRelativeTo(null);

                        JPanel inputPanel = new JPanel(new BorderLayout());
                        JTextArea textArea = new JTextArea();
                        JScrollPane scrollPane = new JScrollPane(textArea); //add scrollbar
                        JButton okButton = new JButton("OK");

                        okButton.addActionListener(new ActionListener()
                        {
                            @Override
                            public void actionPerformed(ActionEvent e)
                            {
                                String task = textArea.getText();
                                //Do something with the task, for example:
                                JOptionPane.showMessageDialog(inputFrame, "Совместная задача добавлена: \n" + task);
                                inputFrame.dispose(); //Close the input window
                            }
                        });

                        inputPanel.add(scrollPane, BorderLayout.CENTER);
                        inputPanel.add(okButton, BorderLayout.SOUTH);
                        inputFrame.add(inputPanel);
                        inputFrame.setVisible(true);
                    }
                });

                addPersonalTasks.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        JFrame inputFrame = new JFrame("Введите задачу");
                        inputFrame.setSize(400, 200);
                        inputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        inputFrame.setLocationRelativeTo(null);

                        JPanel inputPanel = new JPanel(new BorderLayout());
                        JTextArea textArea = new JTextArea();
                        JScrollPane scrollPane = new JScrollPane(textArea); //add scrollbar
                        JButton okButton = new JButton("OK");

                        okButton.addActionListener(new ActionListener()
                        {
                            @Override
                            public void actionPerformed(ActionEvent e)
                            {
                                String task = textArea.getText();
                                //Do something with the task, for example:
                                JOptionPane.showMessageDialog(inputFrame, "Задача добавлена: \n" + task);
                                inputFrame.dispose(); //Close the input window
                            }
                        });

                        inputPanel.add(scrollPane, BorderLayout.CENTER);
                        inputPanel.add(okButton, BorderLayout.SOUTH);
                        inputFrame.add(inputPanel);
                        inputFrame.setVisible(true);
                    }
                });

                selectTask.add(buttonPanel);
                selectTask.setVisible(true);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(startButton, gbc);

        add(panel);
        setVisible(true);
    }
}