package elements;

import javax.swing.*;

import floorplan.DrawingPanel;
import floorplan.ElementSelectedObserver;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BarElement extends JPanel {
    private List<ElementSelectedObserver> observers = new ArrayList<>();
    private JPanel roomTypePanel;
    private JButton roomButton;
    private boolean isRoomPanelVisible = false;

    public BarElement() {
        // Use BorderLayout for flexible positioning
        setLayout(new BorderLayout(5, 5)); // Added gap between components

        // Create main panel for elements (left-side)
        JPanel elementsPanel = new JPanel(new GridLayout(0, 1)); // Changed to 1 column
        elementsPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5)); // Add right padding

        // Create room button and room type panel
        roomButton = createRoomButton();
        roomTypePanel = createRoomTypePanel();
        roomTypePanel.setVisible(false);

        // Add room button and other elements to elements panel
        elementsPanel.add(roomButton);
        addOtherElements(elementsPanel);

        // Add elements panel to the west (left)
        add(elementsPanel, BorderLayout.WEST);

        // Add room type panel to the center
        add(roomTypePanel, BorderLayout.CENTER);
    }

    private JButton createRoomButton() {
        JButton button = new JButton("Room");
        button.setPreferredSize(new Dimension(80, 40));
        button.setMargin(new Insets(3, 3, 3, 3));

        button.addActionListener(e -> {
            isRoomPanelVisible = !isRoomPanelVisible;
            roomTypePanel.setVisible(isRoomPanelVisible);

            // Notify parent container to revalidate and repaint
            SwingUtilities.getWindowAncestor(this).validate();
        });

        return button;
    }

    private JPanel createRoomTypePanel() {
        JPanel panel = new JPanel(new GridLayout(10, 1, 0, 10)); // Single column (vertical layout)
        panel.setBorder(BorderFactory.createTitledBorder("Room Types"));
        panel.setPreferredSize(new Dimension(120, 250)); // Increased height to accommodate vertical layout

        // Add room type buttons
        addRoomTypeButton(panel);

        return panel;
    }

    private void addRoomTypeButton(JPanel panel) {
        String[] roomTypes = {"Bedroom", "Kitchen", "Dining Room", "Bathroom"};
        char[] typeParams = {'a', 'b', 'c', 'd'};
    
        for (int i = 0; i < roomTypes.length; i++) {
            String roomType = roomTypes[i];
            char typeParam = typeParams[i];
    
            JButton button = new JButton(roomType);
            button.setPreferredSize(new Dimension(100, 40));
            button.setMargin(new Insets(3, 3, 3, 3));
    
            button.addActionListener(e -> {
    // First command: change room type
    DrawingPanel.changeroom(typeParam);

    // Second command: notify observers
    notifyObservers(new Room(new Point(0, 0), typeParam));

    // Third command: adjust position and size dynamically (example)
    });

    
            panel.add(button);
        }
    }
    

    private void addOtherElements(JPanel panel) {
        // Add other design elements as before
        addElement(panel, new Wall());
        addElement(panel, new Door());
        addElement(panel, new Windoww());
        addElement(panel, new Bath());
        addElement(panel, new Bed());
        addElement(panel, new Chair());
        addElement(panel, new Counter());
        addElement(panel, new Desk());
        addElement(panel, new Fridge());
        addElement(panel, new Lamp());
        addElement(panel, new Plant());
        addElement(panel, new Sink());
        addElement(panel, new Sofa());
        addElement(panel, new Stairs());
        addElement(panel, new Stove());
        addElement(panel, new Table());
        addElement(panel, new Toilet());
    }

    private void addElement(JPanel panel, DesignElement element) {
        String buttonName = element.getClass().getSimpleName();
        if (buttonName.equals("DoorRight")) {
            buttonName = "<html>Door<br />Right</html>";
        }
        if (buttonName.equals("Windoww")) {
            buttonName = "<html>Window</html>";
        }
        if (buttonName.equals("DoorLeft")) {
            buttonName = "<html>Door<br />Left</html>";
        }

        JButton button = new JButton(buttonName);
        button.setPreferredSize(new Dimension(100, 40));
        button.setMargin(new Insets(3, 3, 3, 3));
        button.addActionListener(e -> notifyObservers(element));

        panel.add(button);
    }

    public void addObserver(ElementSelectedObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(DesignElement element) {
        for (ElementSelectedObserver observer : observers) {
            observer.onElementSelected(element);
        }
    }
}
