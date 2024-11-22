package elements;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Class representing a bed design element.
 *
 * @author Wahad Latif
 */
public class Bed implements DesignElement {
    private static final int DEFAULT_BED_WIDTH = 40;
    private static final int DEFAULT_BED_HEIGHT = 80;
    private int bedWidth = DEFAULT_BED_WIDTH;
    private int bedHeight = DEFAULT_BED_HEIGHT;
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
    		g.setColor(Color.RED);
    	}
        //g.fillRect(startPoint.x - bedWidth / 2, startPoint.y - bedHeight / 2, bedWidth, bedHeight);
        g.setStroke(new BasicStroke(2));


        int x = - bedWidth / 2;
        int y = - bedHeight / 2;
    
        // Save the current graphics transformation
        AffineTransform oldTransform = g.getTransform();
    
        // Translate and rotate the graphics context to draw the bed at the desired position and angle
        g.translate(startPoint.x, startPoint.y);
        g.rotate(Math.toRadians(rotationAngle));
    
        // Draw the bed frame
        g.drawRect(x, y, bedWidth, bedHeight);
    
        // Offset the pillow from the bed frame
        int pillowWidth = bedWidth * 3 / 4; // Make the pillow wider
        int pillowHeight = bedHeight / 4;
        int pillowX =  x + (bedWidth - pillowWidth) / 2;//-pillowWidth / 2;
        int pillowY = y + bedHeight / 12; // Adjusted for closer to the top
    
        // Draw the pillow
        g.drawRect(pillowX, pillowY, pillowWidth, pillowHeight);
    
        // Draw the blanket
        int blanketHeight = bedHeight / 2;
        g.drawRect(x, pillowY + pillowHeight, bedWidth, blanketHeight);
    
        // Restore the old graphics transformation
        g.setTransform(oldTransform);
    }

    @Override
    public Shape getBounds() {
        // Calculate the coordinates of the corners of the unrotated rectangle
        int x1 = -bedWidth / 2;
        int y1 = -bedHeight / 2;
        int x2 = bedWidth / 2;
        int y2 = -bedHeight / 2;
        int x3 = bedWidth / 2;
        int y3 = bedHeight / 2;
        int x4 = -bedWidth / 2;
        int y4 = bedHeight / 2;

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
        bedWidth = (int) (scale * DEFAULT_BED_WIDTH);
        bedHeight = (int) (scale * DEFAULT_BED_HEIGHT);
    }

    @Override
    public void rotate(int angle) {
        rotationAngle = angle;
    }
}