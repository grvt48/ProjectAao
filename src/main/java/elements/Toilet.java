package elements;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Class representing a toilet design element.
 *
 * @author Kevin Cao
 */
public class Toilet implements DesignElement {
    private static final int DEFAULT_TOILET_RADIUS_X = 20;
    private static final int DEFAULT_TOILET_RADIUS_Y = 15;
    private int toiletRadiusX = DEFAULT_TOILET_RADIUS_X;
    private int toiletRadiusY = DEFAULT_TOILET_RADIUS_Y;
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
            g.setColor(Color.BLACK);
        }
        g.setStroke(new BasicStroke(2));

        // Calculate the center point of the toilet
        int centerX = startPoint.x;
        int centerY = startPoint.y;

        // Save the current graphics transformation
        AffineTransform oldTransform = g.getTransform();

        // Translate to the center point
        g.translate(centerX, centerY);

        // Rotate the graphics context
        g.rotate(Math.toRadians(rotationAngle));

        // Translate back to the original position
        g.translate(-centerX, -centerY);

        // Draw oblate toilet body outline
        g.drawOval(startPoint.x - toiletRadiusX, startPoint.y - toiletRadiusY, 2 * toiletRadiusX, 2 * toiletRadiusY);

        // Draw toilet tank outline
        int tankWidth = toiletRadiusX / 2;
        int tankHeight = toiletRadiusY * 2;
        g.drawRect(startPoint.x + toiletRadiusX, startPoint.y - tankHeight / 2, tankWidth, tankHeight);

        // Restore the old graphics transformation
        g.setTransform(oldTransform);
    }

    @Override
    public Shape getBounds() {
        // Calculate the coordinates of the corners of the unrotated rectangle
        int x1 = -toiletRadiusX;
        int y1 = -toiletRadiusY;
        int x2 = toiletRadiusX + toiletRadiusX / 2;
        int y2 = -toiletRadiusY;
        int x3 = toiletRadiusX + toiletRadiusX / 2;
        int y3 = toiletRadiusY;
        int x4 = -toiletRadiusX;
        int y4 = toiletRadiusY;

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
        toiletRadiusX = (int) (scale * DEFAULT_TOILET_RADIUS_X);
        toiletRadiusY = (int) (scale * DEFAULT_TOILET_RADIUS_Y);
    }

    @Override
    public void rotate(int angle) {
        rotationAngle = angle;
    }
}
