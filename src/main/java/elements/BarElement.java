package elements;

import javax.swing.*;
import floorplan.ElementSelectedObserver;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Class for design elements sidebar
 * @author Wahad Latif and ChatGPT
 *
 */
public class BarElement extends JPanel {
    private List<ElementSelectedObserver> observers = new ArrayList<>();

    public BarElement() {
        setLayout(new GridLayout(9, 2)); // Increased rows to fit the Room button
        addRoomButton(); // Add the Room button here
        addElement(new Window());
        addElement(new DoorRight());
        addElement(new DoorLeft());
        addElement(new Bath());
        addElement(new Bed());
        addElement(new Chair());
        addElement(new Counter());
        addElement(new Desk());
        addElement(new Fridge());
        addElement(new Lamp());
        addElement(new Plant());
        addElement(new Sink());
        addElement(new Sofa());
        addElement(new Stairs());
        addElement(new Stove());
        addElement(new Table());
        addElement(new Toilet());
    }

    private void addElement(DesignElement element) {
        // Custom Button Names
        String buttonName = element.getClass().getSimpleName();
        if (buttonName.equals("DoorRight")) {
            buttonName = "<html>Door<br />Right</html>";
        }
        if (buttonName.equals("DoorLeft")) {
            buttonName = "<html>Door<br />Left</html>";
        }
        JButton button = new JButton(buttonName);

        // Set a preferred size for the button
        button.setPreferredSize(new Dimension(60, 40));

        // Set a margin to provide padding around the text
        button.setMargin(new Insets(3, 3, 3, 3));

        // Whenever button is clicked, observers (drawingPanel) know what design element was picked
        button.addActionListener(e -> notifyObservers(element));

        add(button);
    }

    //Nikka samajh ni aara
    
    private void addRoomButton() {
        JButton roomButton = new JButton("Room");

        // Set a preferred size for the button
        roomButton.setPreferredSize(new Dimension(60, 40));

        // Set a margin to provide padding around the text
        roomButton.setMargin(new Insets(3, 3, 3, 3));

        // Notify observers that the Room element is selected
        roomButton.addActionListener(e -> {
            // Room will default to a small rectangle; its size will be adjusted during placement
            notifyObservers(new Room(new Point(0, 0))); // Default room dimensions
        });

        add(roomButton);
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
