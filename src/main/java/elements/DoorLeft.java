package elements;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Class representing a door design element.
 *
 * @author Wahad Latif
 */
public class DoorLeft implements DesignElement {
    private static final int DEFAULT_DOOR_WIDTH = 80;
    private static final int DEFAULT_DOOR_HEIGHT = 80;
    private int doorWidth = DEFAULT_DOOR_WIDTH;
    private int doorHeight = DEFAULT_DOOR_HEIGHT;
    private Point startPoint;
    private boolean isSelected = false;
    private int rotationAngle = 0;

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    @Override
    public void draw(Graphics2D g) {
        if (isSelected) {
            g.setColor(Color.MAGENTA);
        } else {
            g.setColor(Color.RED);
        }
        g.setStroke(new BasicStroke(2));


        int x = - doorWidth / 2;
        int y = - doorHeight / 2;

        // Save the current graphics transformation
        AffineTransform oldTransform = g.getTransform();

        // Translate and rotate the graphics context to draw the bed at the desired position and angle
        g.translate(startPoint.x, startPoint.y);
        g.rotate(Math.toRadians(rotationAngle));

        // Draw the quarter circle
        g.drawArc(x, y, doorWidth, doorHeight, 0, 90);

        // Calculate the center of the circle
        int centerX = x + doorWidth / 2;
        int centerY = y + doorHeight / 2;

        // Calculate the endpoint of the line to meet the quarter circle
        int endX = (int) (centerX + doorWidth / 2 * Math.cos(Math.toRadians(270)));
        int endY = (int) (centerY + doorHeight / 2 * Math.sin(Math.toRadians(270)));

        // Draw a line from the center of the circle to the arc
        g.drawLine(centerX, centerY, endX, endY);
    
        // Restore the old graphics transformation
        g.setTransform(oldTransform);
    }

    @Override
    public Shape getBounds() {
        // Calculate the coordinates of the corners of the unrotated rectangle
        int x1 = 0;
        int y1 = -doorHeight / 2;
        int x2 = doorWidth / 2;
        int y2 = -doorHeight / 2;
        int x3 = doorWidth / 2;
        int y3 = 0;
        int x4 = 0;
        int y4 = 0;

        // Apply the rotation to each corner
        double cosTheta = Math.cos(Math.toRadians(rotationAngle));
        double sinTheta = Math.sin(Math.toRadians(rotationAngle));

        int[] xPoints = {(int) (x1 * cosTheta - y1 * sinTheta), (int) (x2 * cosTheta - y2 * sinTheta),
                (int) (x3 * cosTheta - y3 * sinTheta), (int) (x4 * cosTheta - y4 * sinTheta)};
        int[] yPoints = {(int) (x1 * sinTheta + y1 * cosTheta), (int) (x2 * sinTheta + y2 * cosTheta),
                (int) (x3 * sinTheta + y3 * cosTheta), (int) (x4 * sinTheta + y4 * cosTheta)};

        // Create a polygon from the rotated corners
        Polygon polygon = new Polygon(xPoints, yPoints, 4);

        // Translate the polygon to the start point
        polygon.translate(startPoint.x, startPoint.y);

        return polygon;
    }

    @Override
	public boolean isSelected() {
    	return isSelected;
    }
    
    @Override
    public void setSelected(boolean selected) {
    	isSelected = selected;
    }

    @Override
    public void resize(double scale) {
    	doorWidth = (int) (scale * DEFAULT_DOOR_WIDTH);
        doorHeight = (int) (scale * DEFAULT_DOOR_HEIGHT);
    }

    @Override
    public void rotate(int angle) {
        rotationAngle = angle;
    }
}