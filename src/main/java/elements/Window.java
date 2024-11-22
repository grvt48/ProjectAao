package elements;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Class representing a window design element.
 *
 * @author ChatGPT, Wahad Latif
 */
public class Window implements DesignElement {
    private static final int DEFAULT_WINDOW_WIDTH = 40;
    private static final int DEFAULT_WINDOW_HEIGHT = 3;
    private int windowWidth = DEFAULT_WINDOW_WIDTH;
    private int windowHeight = DEFAULT_WINDOW_HEIGHT;
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
    // Save the current graphics transformation
    AffineTransform oldTransform = g.getTransform();

    // Translate and rotate the graphics context to draw the window at the desired position and angle
    g.translate(startPoint.x, startPoint.y);
    g.rotate(Math.toRadians(rotationAngle));

    // Set the stroke for drawing
    g.setStroke(new BasicStroke(2));

    // Calculate the number of stripes that will fit inside the window
    int numStripes = windowWidth / 5; // Stripes will be 10px wide each, can adjust as needed

    // Loop to draw alternating black and white stripes
    for (int i = 0; i < numStripes; i++) {
        if (i == 1||i == 2||i == 5||i == 6) {
            g.setColor(new Color(0, 0, 0, 0));
        } else {
            g.setColor(Color.WHITE); // White stripe
        }
        g.fillRect(-windowWidth / 2 + (i * 5), -windowHeight / 2, 5, windowHeight); // 5px wide stripes
    }

    // Restore the old graphics transformation
    g.setTransform(oldTransform);
}




    @Override
    public Shape getBounds() {
        // Calculate the coordinates of the corners of the unrotated rectangle
        int x1 = -windowWidth / 2;
        int y1 = -windowHeight / 2;
        int x2 = windowWidth / 2;
        int y2 = -windowHeight / 2;
        int x3 = windowWidth / 2;
        int y3 = windowHeight / 2;
        int x4 = -windowWidth / 2;
        int y4 = windowHeight / 2;

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
        windowWidth = (int) (scale * DEFAULT_WINDOW_WIDTH);
        windowHeight = (int) (scale * DEFAULT_WINDOW_HEIGHT);
    }

    @Override
    public void rotate(int angle) {
        rotationAngle = angle;
    }
}
