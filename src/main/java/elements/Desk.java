package elements;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Class representing a desk design element.
 *
 * @author Kevin Cao
 */
public class Desk implements DesignElement {
    private static final int DEFAULT_DESK_WIDTH = 60;
    private static final int DEFAULT_DESK_HEIGHT = 40;
    private static final int DEFAULT_LEG_WIDTH = 5;

    private int deskWidth = DEFAULT_DESK_WIDTH;
    private int deskHeight = DEFAULT_DESK_HEIGHT;
    private int legWidth = DEFAULT_LEG_WIDTH;

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
            g.setColor(Color.DARK_GRAY);
        }
        g.setStroke(new BasicStroke(2));

        // Save the current graphics transformation
        AffineTransform oldTransform = g.getTransform();

        // Translate and rotate the graphics context to draw the stove at the desired position and angle
        g.translate(startPoint.x, startPoint.y);
        g.rotate(Math.toRadians(rotationAngle));

        // Draw desk top
        g.drawRect(-deskWidth/2, -deskHeight/2, deskWidth, deskHeight);

        // Draw desk corners
        int cornerSize = legWidth * 2;
        g.drawRect(-deskWidth/2, -deskHeight/2, cornerSize, cornerSize);
        g.drawRect(-deskWidth/2 + deskWidth - legWidth * 2, -deskHeight/2, cornerSize, cornerSize);
        g.drawRect(-deskWidth/2, -deskHeight/2 + deskHeight - legWidth * 2, cornerSize, cornerSize);
        g.drawRect(-deskWidth/2 + deskWidth - legWidth * 2, -deskHeight/2 + deskHeight - legWidth * 2, cornerSize, cornerSize);

        // Restore the old graphics transformation
        g.setTransform(oldTransform);
    }

    @Override
    public Shape getBounds() {
        // Calculate the coordinates of the corners of the unrotated rectangle
        int x1 = -deskWidth / 2;
        int y1 = -deskHeight / 2;
        int x2 = deskWidth / 2;
        int y2 = -deskHeight / 2;
        int x3 = deskWidth / 2;
        int y3 = deskHeight / 2;
        int x4 = -deskWidth / 2;
        int y4 = deskHeight / 2;

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
        deskWidth = (int) (scale * DEFAULT_DESK_WIDTH);
        deskHeight = (int) (scale * DEFAULT_DESK_HEIGHT);
        legWidth = (int) (scale * DEFAULT_LEG_WIDTH);
    }

    @Override
    public void rotate(int angle) {
        rotationAngle = angle;
    }
}
