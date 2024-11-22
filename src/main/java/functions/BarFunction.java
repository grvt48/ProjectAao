package functions;

import javax.swing.*;

import floorplan.FunctionSelectedObserver;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Class for sidebar
 * @author Wahad Latif and ChatGPT
 *
 */
public class BarFunction extends JPanel {
    private List<FunctionSelectedObserver> observers = new ArrayList<>();

    public BarFunction() {
        setLayout(new GridLayout(1, 5));
    }

    public void addFunction(ManipulationFunction function) {
        //Custom Button Names with Keyboard Shortcut Hints
        String buttonName;
        switch (function.getClass().getSimpleName()) {
            case "Select":
                buttonName = "Select   (S)";
                break;
            case "Move":
                buttonName = "Move   (M)";
                break;
            case "Remove":
                buttonName = "Remove   (R)";
                break;
            case "Resize":
                buttonName = "Resize   (Z)";
                break;
            case "Rotate":
                buttonName = "Rotate   (T)";
                break;
            default:
                buttonName = "Unknown";
                break;
        }
        JButton button = new JButton(buttonName);

        //Whenever button is clicked, observers (drawingPanel) know what function was picked
        button.addActionListener(e -> notifyObservers(function));
        
        add(button);
    }

    public void addObserver(FunctionSelectedObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(ManipulationFunction function) {
        for (FunctionSelectedObserver observer : observers) {
            observer.onFunctionSelected(function);
        }
    }
}