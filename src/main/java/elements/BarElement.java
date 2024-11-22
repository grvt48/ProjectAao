package elements;

import javax.swing.*;
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
        // Use BorderLayout for more flexible positioning
        setLayout(new BorderLayout(5, 5)); // Added gap between components
        
        // Create main panel for elements (left-side)
        JPanel elementsPanel = new JPanel(new GridLayout(0, 1)); // Changed to 1 column
        elementsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Add right padding
        
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
        button.setPreferredSize(new Dimension(100, 40));
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
        JPanel panel = new JPanel(new GridLayout(10, 1,0,10)); // Changed to single column (vertical layout)
        panel.setBorder(BorderFactory.createTitledBorder("Room Types"));
        panel.setPreferredSize(new Dimension(150, 250)); // Increased height to accommodate vertical layout
        
        // Room type buttons with specific colors
        addRoomTypeButton(panel, "Bedroom", Room.RoomType.BEDROOM);
        addRoomTypeButton(panel, "Kitchen", Room.RoomType.KITCHEN);
        addRoomTypeButton(panel, "Dining Room", Room.RoomType.DINING_ROOM);
        addRoomTypeButton(panel, "Bathroom", Room.RoomType.BATHROOM);
        
        return panel;
    }
    private void addRoomTypeButton(JPanel panel, String roomType, Room.RoomType type) {
        JButton button = new JButton(roomType);
        button.setBackground(type.getColor());
        button.setOpaque(true);
        button.setBorderPainted(true);
        button.setForeground(Color.BLACK);
        button.addActionListener(e -> {
            Room room = new Room(new Point(0, 0), type);
            notifyObservers(room);
            
            // Hide room type panel after selection
            isRoomPanelVisible = false;
            roomTypePanel.setVisible(false);
            SwingUtilities.getWindowAncestor(this).validate();
        });
        
        panel.add(button);
    }

    private void addOtherElements(JPanel panel) {
        // Add all other design elements as before
        addElement(panel, new Wall());
        addElement(panel, new Door());
        addElement(panel, new Window());
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