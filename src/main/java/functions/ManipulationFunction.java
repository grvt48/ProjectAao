package functions;

import java.awt.*;

/**
 * Abstract interface representing a manipulation function.
 *
 * @author Wahad Latif
 */
public interface ManipulationFunction {
    //Startegy Pattern
    public void performFunction(Point clickedPoint);
}
