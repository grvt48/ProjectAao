package functions;

import java.awt.*;
import javax.swing.*;
import java.util.Hashtable; // Importing Hashtable for labels
import elements.DesignElement;
import floorplan.*;

/**
 * Class representing a rotate function
 *
 * @author ChatGPT, Wahad Latif
 */
public class Rotate extends JSlider implements ManipulationFunction {
    private DrawingPanel drawingPanel;
    private Select selectFunction;

    public Rotate(DrawingPanel drawingPanel, Select selectFunction) {
        super(JSlider.HORIZONTAL, 0, 360, 0); // Range from 0 to 360, starting at 0
        this.drawingPanel = drawingPanel;
        this.selectFunction = selectFunction;

        setMajorTickSpacing(90);  // Set major tick spacing to 90
        setMinorTickSpacing(90);  // Set minor tick spacing to 90
        setPaintTicks(true);
        setPaintLabels(true);

        // Set labels for the slider (multiple of 90 degrees)
        setLabelTable(createLabelTable());

        // Hide slider initially
        setVisible(false);

        // Add a listener to handle resizing and snapping the value to nearest multiple of 90
        addChangeListener(e -> {
            // Snap the slider value to the nearest multiple of 90
            int snappedValue = Math.round(getValue() / 90.0f) * 90;
            setValue(snappedValue); // Update the slider value

            // Perform the rotation operation based on the snapped value
            performFunction(new Point());
        });
    }

    private Hashtable<Integer, JLabel> createLabelTable() {
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel("0°"));
        labelTable.put(90, new JLabel("90°"));
        labelTable.put(180, new JLabel("180°"));
        labelTable.put(270, new JLabel("270°"));
        labelTable.put(360, new JLabel("360°"));
        return labelTable;
    }

    @Override
    public void performFunction(Point point) {
        // Get the snapped value to the nearest multiple of 90
        int rotateFactor = getValue();

        // Rotate the selected design element
        if (selectFunction.selectedElements != null) {
            for (DesignElement element : selectFunction.selectedElements) {
                element.rotate(rotateFactor);
            }
        }

        // Update drawing panel and make sure it still receives keyboard input (not this slider)
        drawingPanel.repaint();
        drawingPanel.requestFocusInWindow();
    }
}
