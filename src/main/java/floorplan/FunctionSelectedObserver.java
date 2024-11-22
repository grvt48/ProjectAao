package floorplan;

import functions.*;

/*
 * @author Wahad Latif
 */
public interface FunctionSelectedObserver {
    //DrawingPanel will know which function was selected from BarFunction
    void onFunctionSelected(ManipulationFunction function);
}
