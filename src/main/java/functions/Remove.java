package functions;

import java.util.*;
import java.util.List;
import java.awt.*;

import floorplan.*;
import elements.*;

/**
 * Class representing a remove function
 *
 * @author Wahad Latif
 */
public class Remove implements ManipulationFunction {
    private DrawingPanel drawingPanel;
    private Select selectFunction;

    public Remove(DrawingPanel drawingPanel, Select selectFunction) {
        this.drawingPanel = drawingPanel;
        this.selectFunction = selectFunction;
    }

    @Override
    public void performFunction(Point point) {
        // Remove all selected items from design elements
        if (selectFunction.selectedElements != null) {
            List<DesignElement> elements = drawingPanel.getDesignElements();
            Iterator<DesignElement> iterator = elements.iterator();

            //Iterate and remove elements safely
            while (iterator.hasNext()) {
                DesignElement element = iterator.next();
                if (element.isSelected()) {
                    iterator.remove();
                }
            }
            

            //Clear the removed items from selected items too
            selectFunction.clearSelection();
        }

        // Redraw the canvas to reflect the changes
        drawingPanel.repaint();
    }
}
