package functions;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import elements.DesignElement;
import elements.Room;
import floorplan.*;

/**
 * Class representing a move function
 *
 * @author ChatGPT, Wahad Latif
 */
public class Move implements ManipulationFunction {
    private DrawingPanel drawingPanel;
    private Select selectFunction;
    public Point startDragPoint;

    private Map<Room, Point[]> roomStartPositions = new HashMap<>();


    public Move(DrawingPanel drawingPanel, Select selectFunction) {
        this.drawingPanel = drawingPanel;
        this.selectFunction = selectFunction;
    }

    @Override
    public void performFunction(Point draggedPoint) {
        if (draggedPoint == null) {
            return;
        }

        if (selectFunction.selectedElements != null) {
            int dx = draggedPoint.x - startDragPoint.x;
            int dy = draggedPoint.y - startDragPoint.y;

            // For each selected element, move it
            for (DesignElement element : selectFunction.selectedElements) {
                Point start = element.getStartPoint();
                Point newStart = new Point(start.x + dx, start.y + dy);
                element.setStartPoint(newStart);

                // If the element is a room, check for overlap
                if (element instanceof Room) {
                    Room room = (Room) element;
                    Point end = room.getEndPoint();
                    Point newEnd = new Point(end.x + dx, end.y + dy);
                    room.setEndPoint(newEnd);

                    // Check for overlap with other rooms
                    for (DesignElement otherElement : drawingPanel.getDesignElements()) {
                        if (otherElement instanceof Room && otherElement != room) {
                            if (room.overlaps((Room) otherElement)) {
                                JOptionPane.showMessageDialog(drawingPanel, "Overlap error");

                                // Revert to the initial position (where the drag started)
                                Point[] originalPosition = roomStartPositions.get(room);
                                if (originalPosition != null) {
                                    room.setStartPoint(originalPosition[0]);
                                    room.setEndPoint(originalPosition[1]);  // Reset endPoint as well
                                }

                                drawingPanel.repaint();
                                return;
                            }
                        }
                    }
                }
            }

            // Update the starting point for the next move
            startDragPoint = draggedPoint;

            // Redraw the canvas to reflect the changes
            drawingPanel.repaint();
        }
    }

    // Capture the initial positions when mouse is pressed
    public void captureStartPositions() {
        roomStartPositions.clear(); // Clear previous captured positions

        for (DesignElement element : selectFunction.selectedElements) {
            if (element instanceof Room) {
                Room room = (Room) element;
                // Store both the start point and the end point
                Point[] position = new Point[] { new Point(room.getStartPoint()), new Point(room.getEndPoint()) };
                roomStartPositions.put(room, position); // Store original position (start and end points)
            }
        }
    }
}