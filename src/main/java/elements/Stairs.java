package elements;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Class representing a stairs design element.
 *
 * @author Wahad Latif
 */
public class Stairs implements DesignElement {
    private static final int DEFAULT_STAIR_WIDTH = 60;
    private static final int DEFAULT_STAIR_HEIGHT = 120;
    private int stairWidth = DEFAULT_STAIR_WIDTH;
    private int stairHeight = DEFAULT_STAIR_HEIGHT;
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
        if (isSelected == true) {
    		g.setColor(Color.MAGENTA);
    	} else {
    		g.setColor(Color.GRAY);
    	}
        //g.fillRect(startPoint.x - bedWidth / 2, startPoint.y - bedHeight / 2, bedWidth, bedHeight);
        g.setStroke(new BasicStroke(2));


        int x = - stairWidth / 2;
        int y = - stairHeight / 2;
    
        // Save the current graphics transformation
        AffineTransform oldTransform = g.getTransform();
    
        // Translate and rotate the graphics context to draw the bed at the desired position and angle
        g.translate(startPoint.x, startPoint.y);
        g.rotate(Math.toRadians(rotationAngle));
    
        // Draw the frame
        g.drawRect(x, y, stairWidth, stairHeight);

        int numSteps = stairHeight/12;
        int stepHeight = stairHeight/numSteps;
        for(int i = 0; i < stairHeight; i+=stepHeight){
            // Draw the steps
            g.drawRect(x, y, stairWidth, i);
        }
    
        // Restore the old graphics transformation
        g.setTransform(oldTransform);
    }

    @Override
    public Shape getBounds() {
        // Calculate the coordinates of the corners of the unrotated rectangle
        int x1 = -stairWidth / 2;
        int y1 = -stairHeight / 2;
        int x2 = stairWidth / 2;
        int y2 = -stairHeight / 2;
        int x3 = stairWidth / 2;
        int y3 = stairHeight / 2;
        int x4 = -stairWidth / 2;
        int y4 = stairHeight / 2;

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
        stairWidth = (int) (scale * DEFAULT_STAIR_WIDTH);
        stairHeight = (int) (scale * DEFAULT_STAIR_HEIGHT);
    }

    @Override
    public void rotate(int angle) {
        rotationAngle = angle;
    }
}