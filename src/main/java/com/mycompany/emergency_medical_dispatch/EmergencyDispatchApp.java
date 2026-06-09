package com.mycompany.emergency_medical_dispatch;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple Swing GUI for the Emergency Medical Dispatch System.
 */
public class EmergencyDispatchApp {

    // Core Data Structures
    private Graph cityMap = new Graph();
    private PriorityQueueClass urgentCalls = new PriorityQueueClass();
    private Queue<Call> nonUrgentCalls = new Queue<>();
    private List<String> availableAmbulances = new ArrayList<>();
    private List<String> busyAmbulances = new ArrayList<>();
    private int dispatchCount = 0;

    // UI Components
    private JComboBox<String> locationBox;
    private JTextField typeField;
    private JComboBox<String> severityBox;
    private JTextArea logArea;
    private JLabel statsLabel;

    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new EmergencyDispatchApp().createAndShowGUI());
    }

    public void createAndShowGUI() {
        setupSystemData();

        JFrame frame = new JFrame("Emergency Medical Dispatch System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 550);
        frame.setLayout(new BorderLayout(10, 0));

        // Redirect System.out and System.err to the logArea
        logArea = new JTextArea(12, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        java.io.PrintStream printStream = new java.io.PrintStream(new java.io.OutputStream() {
            @Override
            public void write(int b) {
                logArea.append(String.valueOf((char) b));
                logArea.setCaretPosition(logArea.getDocument().getLength());
            }
            @Override
            public void write(byte[] b, int off, int len) {
                logArea.append(new String(b, off, len));
                logArea.setCaretPosition(logArea.getDocument().getLength());
            }
        });
        System.setOut(printStream);
        System.setErr(printStream);

        // --- 1. Input Panel (Top) ---
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Emergency Type
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        inputPanel.add(new JLabel("Emergency Type:"), gbc);
        gbc.gridy = 1;
        typeField = new JTextField();
        inputPanel.add(typeField, gbc);

        // Location and Severity labels
        gbc.gridy = 2; gbc.gridwidth = 1;
        inputPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(new JLabel("Severity:"), gbc);

        // Location and Severity inputs
        gbc.gridx = 0; gbc.gridy = 3;
        locationBox = new JComboBox<>(cityMap.getLocations().toArray(new String[0]));
        inputPanel.add(locationBox, gbc);
        gbc.gridx = 1;
        severityBox = new JComboBox<>(new String[]{"Critical", "Medium", "Low"});
        inputPanel.add(severityBox, gbc);

        // Add Button
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JButton addCallButton = new JButton("Add Call to Queue");
        addCallButton.addActionListener(e -> handleAddCall());
        inputPanel.add(addCallButton, gbc);

        frame.add(inputPanel, BorderLayout.NORTH);

        // --- 2. Log Area (Center) ---
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("System Logs"));
        frame.add(scrollPane, BorderLayout.CENTER);

        // --- 3. Control Panel (Bottom) ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        statsLabel = new JLabel("Urgent Calls: 0 | Standard Calls: 0 | Ambulances Free: 3");
        statsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        statsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        JButton startWorkflowButton = new JButton("RUN EMERGENCY MEDICAL DISPATCH");
        startWorkflowButton.setBackground(new Color(76, 175, 80));
        startWorkflowButton.setForeground(Color.WHITE);
        startWorkflowButton.setOpaque(true);
        startWorkflowButton.setBorderPainted(false);
        startWorkflowButton.setFont(new Font("Arial", Font.BOLD, 14));
        startWorkflowButton.addActionListener(e -> handleStartWorkflow());
        
        bottomPanel.add(statsLabel, BorderLayout.NORTH);
        bottomPanel.add(startWorkflowButton, BorderLayout.SOUTH);
        
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        System.out.println("=== SYSTEM READY ===");
        updateStats();
    }

    private void updateStats() {
        int urgent = urgentCalls.size();
        int standard = nonUrgentCalls.size();
        int free = availableAmbulances.size();
        statsLabel.setText("Urgent Calls: " + urgent + " | Standard Calls: " + standard + " | Ambulances Free: " + free);
    }

    private void handleAddCall() {
        String loc = (String) locationBox.getSelectedItem();
        String type = typeField.getText().trim();
        String sevLabel = (String) severityBox.getSelectedItem();

        if (type.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter an Emergency Type!");
            return;
        }

        // Map visual labels back to numeric logic
        int sev = 3;
        if (sevLabel.equals("Critical")) sev = 1;
        else if (sevLabel.equals("Medium")) sev = 2;

        if (sev == 3) {
            nonUrgentCalls.enqueue(new Call(sev, type, loc));
            System.out.println("Added to Standard Queue: " + type + " at " + loc + " (Severity " + sevLabel + ")");
        } else {
            urgentCalls.insert(new Call(sev, type, loc));
        }
        updateStats();
        typeField.setText("");
    }

    private void handleStartWorkflow() {
        if (urgentCalls.isEmpty() && nonUrgentCalls.isEmpty()) {
            System.out.println("[SYSTEM] No calls in queue.");
            return;
        }

        System.out.println("\n=== STARTING DISPATCH WORKFLOW ===");

        // Use a Swing Timer to process calls one by one with a 1.5s delay
        Timer timer = new Timer(1500, null);
        timer.addActionListener(e -> {
            if (urgentCalls.isEmpty() && nonUrgentCalls.isEmpty()) {
                System.out.println("\n=== WORKFLOW COMPLETE ===");
                System.out.println("AMOUNT OF AMBULANCE DISPATCHED: " + dispatchCount);
                updateStats();
                timer.stop();
                return;
            }

            // Handle busy ambulances
            if (availableAmbulances.isEmpty()) {
                System.out.println("\n[SYSTEM NOTICE] All ambulances are currently busy.");
                System.out.println("Remaining calls are waiting in their respective queues...");
                String freed = busyAmbulances.remove(0);
                availableAmbulances.add(freed);
                System.out.println("[SYSTEM UPDATE] Ambulance at " + freed + " is now FREE and returning to base.");
                System.out.println("------------------------------------------------------");
                updateStats();
                return;
            }

            Call current = null;
            if (!urgentCalls.isEmpty()) {
                current = urgentCalls.extractMin();
            } else if (!nonUrgentCalls.isEmpty()) {
                current = nonUrgentCalls.dequeue();
            }

            if (current != null) {
                System.out.println("\nProcessing: " + current);
                String dispatched = cityMap.dispatchClosestAmbulance(current.location, availableAmbulances);
                
                if (dispatched != null) {
                    availableAmbulances.remove(dispatched);
                    busyAmbulances.add(dispatched);
                    dispatchCount++;
                    System.out.println("Status: Ambulance dispatched from " + dispatched + ". (Ambulance is now BUSY)");
                } else {
                    System.out.println("Status: FAILED. No available ambulance can reach this location.");
                }
                System.out.println("------------------------------------------------------");
                updateStats();
            }
        });

        timer.start();
    }

    private void setupSystemData() {
        cityMap.addLocation("Shah Alam");
        cityMap.addLocation("Elmina");
        cityMap.addLocation("Batu Caves");
        cityMap.addLocation("Universiti Malaya Medical Centre");
        cityMap.addLocation("Subang Jaya");
        cityMap.addLocation("Putrajaya");
        cityMap.addLocation("Hospital Tengku Ampuan Rahimah");
        cityMap.addLocation("Taman Sentosa");
        cityMap.addLocation("Klang");
        cityMap.addLocation("Ara Damansara Medical Centre");

        cityMap.addRoad("Klang", "Hospital Tengku Ampuan Rahimah", 3);
        cityMap.addRoad("Klang", "Taman Sentosa", 9);
        cityMap.addRoad("Klang", "Shah Alam", 12);
        cityMap.addRoad("Klang", "Ara Damansara Medical Centre", 27);
        cityMap.addRoad("Ara Damansara Medical Centre", "Elmina", 21);
        cityMap.addRoad("Elmina", "Shah Alam", 24);
        cityMap.addRoad("Shah Alam", "Hospital Tengku Ampuan Rahimah", 15);
        cityMap.addRoad("Shah Alam", "Universiti Malaya Medical Centre", 18);
        cityMap.addRoad("Hospital Tengku Ampuan Rahimah", "Taman Sentosa", 8);
        cityMap.addRoad("Hospital Tengku Ampuan Rahimah", "Subang Jaya", 26);
        cityMap.addRoad("Taman Sentosa", "Subang Jaya", 18);
        cityMap.addRoad("Subang Jaya", "Universiti Malaya Medical Centre", 16);
        cityMap.addRoad("Subang Jaya", "Putrajaya", 29);
        cityMap.addRoad("Putrajaya", "Universiti Malaya Medical Centre", 36);
        cityMap.addRoad("Universiti Malaya Medical Centre", "Batu Caves", 19);
        cityMap.addRoad("Batu Caves", "Elmina", 26);

        availableAmbulances.add("Universiti Malaya Medical Centre");
        availableAmbulances.add("Hospital Tengku Ampuan Rahimah");
        availableAmbulances.add("Ara Damansara Medical Centre");
    }
}
