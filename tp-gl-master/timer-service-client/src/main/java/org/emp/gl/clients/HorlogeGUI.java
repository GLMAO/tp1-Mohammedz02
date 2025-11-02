package org.emp.gl.clients;

import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Modern GUI interface for displaying time using Swing with countdown timers
 */
public class HorlogeGUI extends JFrame implements TimerChangeListener {
    
    // Modern color palette
    private static final Color PRIMARY_BG = new Color(26, 32, 44);
    private static final Color SECONDARY_BG = new Color(45, 55, 72);
    private static final Color CARD_BG = new Color(54, 67, 88);
    private static final Color ACCENT_COLOR = new Color(99, 179, 237);
    private static final Color TEXT_PRIMARY = new Color(237, 242, 247);
    @SuppressWarnings("unused")
    private static final Color TEXT_SECONDARY = new Color(160, 174, 192);
    private static final Color SUCCESS_COLOR = new Color(72, 187, 120);
    private static final Color DANGER_COLOR = new Color(245, 101, 101);
    private static final Color WARNING_COLOR = new Color(237, 137, 54);
    
    private TimerService timerService;
    private JLabel timeLabel;
    private JLabel titleLabel;
    private Font timeFont;
    private JPanel countdownPanel;
    private List<CountdownTimer> countdownTimers;
    private JPanel mainPanel;
    
    public HorlogeGUI(String title, TimerService timerService) {
        this.timerService = timerService;
        this.countdownTimers = new ArrayList<>();
        
        if (timerService != null) {
            timerService.addTimeChangeListener(this);
        }
        
        initializeGUI(title);
        updateTime();
    }
    
    private void initializeGUI(String title) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(true);
        getContentPane().setBackground(PRIMARY_BG);
        
        // Create modern title label
        titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setBorder(new EmptyBorder(25, 20, 15, 20));
        titleLabel.setOpaque(false);
        
        // Create main panel
        mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(PRIMARY_BG);
        mainPanel.setBorder(new EmptyBorder(0, 30, 30, 30));
        
        // Create modern time display card
        JPanel timeCard = new JPanel(new BorderLayout());
        timeCard.setBackground(SECONDARY_BG);
        timeCard.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(20, ACCENT_COLOR.darker()),
            new EmptyBorder(30, 40, 30, 40)
        ));
        
        timeFont = new Font("SF Mono", Font.BOLD, 56);
        timeLabel = new JLabel("00:00:00.0", SwingConstants.CENTER);
        timeLabel.setFont(timeFont);
        timeLabel.setForeground(ACCENT_COLOR);
        timeLabel.setOpaque(false);
        
        timeCard.add(timeLabel, BorderLayout.CENTER);
        
        // Create countdown section
        JPanel countdownSection = new JPanel(new BorderLayout(0, 15));
        countdownSection.setBackground(PRIMARY_BG);
        countdownSection.setOpaque(false);
        
        JLabel countdownTitle = new JLabel("Compteurs à Rebours");
        countdownTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        countdownTitle.setForeground(TEXT_PRIMARY);
        countdownTitle.setBorder(new EmptyBorder(10, 5, 10, 5));
        
        countdownPanel = new JPanel();
        countdownPanel.setLayout(new BoxLayout(countdownPanel, BoxLayout.Y_AXIS));
        countdownPanel.setBackground(PRIMARY_BG);
        countdownPanel.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(countdownPanel);
        scrollPane.setBackground(PRIMARY_BG);
        scrollPane.getViewport().setBackground(PRIMARY_BG);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(500, 280));
        
        // Customize scrollbar
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        
        countdownSection.add(countdownTitle, BorderLayout.NORTH);
        countdownSection.add(scrollPane, BorderLayout.CENTER);
        
        // Create modern control panel
        JPanel controlPanel = createCountdownControlPanel();
        
        // Assemble layout
        add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(timeCard, BorderLayout.NORTH);
        mainPanel.add(countdownSection, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
                System.exit(0);
            }
        });
        
        setSize(650, 750);
        setLocationRelativeTo(null);
    }
    
    private JPanel createCountdownControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        panel.setBackground(SECONDARY_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, ACCENT_COLOR.darker()),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel hLabel = new JLabel("H:");
        hLabel.setForeground(TEXT_PRIMARY);
        hLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JSpinner hoursSpinner = createModernSpinner(0, 0, 23, 1);
        
        JLabel mLabel = new JLabel("M:");
        mLabel.setForeground(TEXT_PRIMARY);
        mLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JSpinner minutesSpinner = createModernSpinner(0, 0, 59, 1);
        
        JLabel sLabel = new JLabel("S:");
        sLabel.setForeground(TEXT_PRIMARY);
        sLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JSpinner secondsSpinner = createModernSpinner(0, 0, 59, 1);
        
        JButton addButton = createModernButton("Ajouter", ACCENT_COLOR);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int hours = (Integer) hoursSpinner.getValue();
                int minutes = (Integer) minutesSpinner.getValue();
                int seconds = (Integer) secondsSpinner.getValue();
                
                if (hours > 0 || minutes > 0 || seconds > 0) {
                    addCountdownTimer(hours, minutes, seconds);
                } else {
                    JOptionPane.showMessageDialog(HorlogeGUI.this, 
                        "Veuillez entrer une durée valide!", 
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        panel.add(hLabel);
        panel.add(hoursSpinner);
        panel.add(mLabel);
        panel.add(minutesSpinner);
        panel.add(sLabel);
        panel.add(secondsSpinner);
        panel.add(addButton);
        
        return panel;
    }
    
    private JSpinner createModernSpinner(int value, int min, int max, int step) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, min, max, step));
        spinner.setPreferredSize(new Dimension(70, 35));
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) editor;
            spinnerEditor.getTextField().setBackground(CARD_BG);
            spinnerEditor.getTextField().setForeground(TEXT_PRIMARY);
            spinnerEditor.getTextField().setCaretColor(TEXT_PRIMARY);
            spinnerEditor.getTextField().setBorder(new EmptyBorder(5, 10, 5, 10));
        }
        
        spinner.setBorder(new RoundedBorder(8, ACCENT_COLOR.darker()));
        return spinner;
    }
    
    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(100, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new RoundedBorder(8, bgColor));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void addCountdownTimer(int hours, int minutes, int seconds) {
        CountdownTimer countdown = new CountdownTimer(hours, minutes, seconds);
        countdownTimers.add(countdown);
        
        JPanel timerPanel = new JPanel(new BorderLayout(15, 0));
        timerPanel.setBackground(CARD_BG);
        timerPanel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, ACCENT_COLOR.darker().darker()),
            new EmptyBorder(20, 25, 20, 25)
        ));
        timerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JLabel timerLabel = new JLabel();
        timerLabel.setFont(new Font("SF Mono", Font.BOLD, 28));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setForeground(SUCCESS_COLOR);
        countdown.setDisplayLabel(timerLabel);
        updateCountdownDisplay(countdown);
        
        JButton removeButton = createModernButton("Supprimer", DANGER_COLOR);
        removeButton.setPreferredSize(new Dimension(110, 35));
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countdownTimers.remove(countdown);
                countdownPanel.remove(timerPanel);
                countdownPanel.revalidate();
                countdownPanel.repaint();
            }
        });
        
        timerPanel.add(timerLabel, BorderLayout.CENTER);
        timerPanel.add(removeButton, BorderLayout.EAST);
        
        countdownPanel.add(timerPanel);
        countdownPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        countdownPanel.revalidate();
        countdownPanel.repaint();
    }
    
    private void updateCountdownDisplay(CountdownTimer countdown) {
        if (countdown.isFinished()) {
            countdown.getDisplayLabel().setText("TERMINÉ!");
            countdown.getDisplayLabel().setForeground(DANGER_COLOR);
        } else {
            int remaining = countdown.getRemainingTenths();
            int hours = remaining / 36000;
            int minutes = (remaining % 36000) / 600;
            int seconds = (remaining % 600) / 10;
            int tenths = remaining % 10;
            
            String timeStr = String.format("%02d:%02d:%02d.%d", hours, minutes, seconds, tenths);
            countdown.getDisplayLabel().setText(timeStr);
            
            if (remaining < 100) {
                countdown.getDisplayLabel().setForeground(WARNING_COLOR);
            } else {
                countdown.getDisplayLabel().setForeground(SUCCESS_COLOR);
            }
        }
    }
    
    private void updateTime() {
        if (timerService != null && timeLabel != null) {
            SwingUtilities.invokeLater(() -> {
                String timeString = String.format("%02d:%02d:%02d.%d",
                        timerService.getHeures(),
                        timerService.getMinutes(),
                        timerService.getSecondes(),
                        timerService.getDixiemeDeSeconde());
                timeLabel.setText(timeString);
            });
        }
    }
    
    @Override
    public void propertyChange(String prop, Object oldValue, Object newValue) {
        updateTime();
        
        SwingUtilities.invokeLater(() -> {
            for (CountdownTimer countdown : countdownTimers) {
                if (!countdown.isFinished() && 
                    TimerChangeListener.DIXEME_DE_SECONDE_PROP.equals(prop)) {
                    countdown.decrement();
                    updateCountdownDisplay(countdown);
                }
            }
        });
    }
    
    public void disconnect() {
        if (timerService != null) {
            timerService.removeTimeChangeListener(this);
        }
    }
    
    public void showWindow() {
        setVisible(true);
    }
    
    /**
     * Inner class for rounded borders
     */
    private static class RoundedBorder extends EmptyBorder {
        private Color borderColor;
        private int radius;
        
        public RoundedBorder(int radius, Color color) {
            super(2, 2, 2, 2);
            this.radius = radius;
            this.borderColor = color;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(borderColor);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }
    
    /**
     * Modern scrollbar UI
     */
    private static class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(99, 179, 237, 100);
            this.trackColor = new Color(45, 55, 72);
        }
        
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }
        
        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }
        
        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            return button;
        }
        
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, 
                thumbBounds.width - 4, thumbBounds.height - 4, 10, 10);
            g2.dispose();
        }
    }
    
    /**
     * Inner class to represent a countdown timer
     */
    private static class CountdownTimer {
        private int remainingTenths;
        private JLabel displayLabel;
        
        public CountdownTimer(int hours, int minutes, int seconds) {
            this.remainingTenths = (hours * 3600 + minutes * 60 + seconds) * 10;
        }
        
        public void decrement() {
            if (remainingTenths > 0) {
                remainingTenths--;
            }
        }
        
        public boolean isFinished() {
            return remainingTenths <= 0;
        }
        
        public int getRemainingTenths() {
            return remainingTenths;
        }
        
        public JLabel getDisplayLabel() {
            return displayLabel;
        }
        
        public void setDisplayLabel(JLabel displayLabel) {
            this.displayLabel = displayLabel;
        }
    }
}