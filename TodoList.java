import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TodoList {

    private DefaultListModel<String> listModel;
    private JList<String> taskList;
    private JTextField taskField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TodoList().createAndShowGUI());
    }

    private void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Task Manager Pro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(480, 700);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(248, 250, 252));

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        mainPanel.setOpaque(false);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("My Tasks");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(new Color(15, 23, 42));
        
        JLabel subtitleLabel = new JLabel("Stay organized, stay focused.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 116, 139));
        subtitleLabel.setBorder(new EmptyBorder(5, 0, 10, 0));
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center List
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setFixedCellHeight(60);
        taskList.setBackground(Color.WHITE);
        taskList.setForeground(new Color(51, 65, 85));
        
        taskList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(241, 245, 249)),
                    new EmptyBorder(10, 20, 10, 20)
                ));
                if (isSelected) {
                    label.setBackground(new Color(239, 246, 255));
                    label.setForeground(new Color(37, 99, 235));
                    label.setFont(new Font("Segoe UI", Font.BOLD, 18));
                } else {
                    label.setBackground(Color.WHITE);
                }
                label.setText("  " + value.toString());
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        JPanel listContainer = new JPanel(new BorderLayout());
        listContainer.setBackground(Color.WHITE);
        listContainer.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        listContainer.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(listContainer, BorderLayout.CENTER);

        // Bottom Controls
        JPanel bottomPanel = new JPanel(new BorderLayout(15, 15));
        bottomPanel.setOpaque(false);

        taskField = new JTextField();
        taskField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        taskField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                new EmptyBorder(15, 15, 15, 15)));
        taskField.addActionListener(e -> addTask());
        
        CustomButton addButton = new CustomButton("Add Task", new Color(16, 185, 129), new Color(5, 150, 105));
        addButton.setPreferredSize(new Dimension(110, 50));
        addButton.addActionListener(e -> addTask());

        JPanel inputPanel = new JPanel(new BorderLayout(15, 0));
        inputPanel.setOpaque(false);
        inputPanel.add(taskField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

        CustomButton removeButton = new CustomButton("Remove Selected", new Color(239, 68, 68), new Color(220, 38, 38));
        removeButton.setPreferredSize(new Dimension(200, 50));
        removeButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                listModel.remove(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a task to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(removeButton, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
        taskField.requestFocus();
    }

    private void addTask() {
        String task = taskField.getText().trim();
        if (!task.isEmpty()) {
            listModel.addElement(task);
            taskField.setText("");
            taskField.requestFocus();
        }
    }

    class CustomButton extends JButton {
        private Color bgColor;
        private Color hoverColor;

        public CustomButton(String text, Color bgColor, Color hoverColor) {
            super(text);
            this.bgColor = bgColor;
            this.hoverColor = hoverColor;
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 15));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent evt) {
                    setBackground(hoverColor);
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent evt) {
                    setBackground(bgColor);
                    repaint();
                }
                @Override
                public void mousePressed(MouseEvent evt) {
                    setBackground(hoverColor.darker());
                    repaint();
                }
                @Override
                public void mouseReleased(MouseEvent evt) {
                    setBackground(hoverColor);
                    repaint();
                }
            });
            setBackground(bgColor);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            super.paintComponent(g2);
            g2.dispose();
        }
    }
}