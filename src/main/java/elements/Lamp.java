package elements;

import java.awt.*;
import java.awt.geom.AffineTransform;


/**
 * Class representing a lamp design element.
 *
 * @author Kevin Cao
 */
public class Lamp implements DesignElement {
    private static final int DEFAULT_BASE_WIDTH = 10;
    private static final int DEFAULT_BASE_HEIGHT = 20;
    private static final int DEFAULT_STAND_LENGTH = 30;
    private static final int DEFAULT_LAMP_WIDTH = 20;
    private static final int DEFAULT_LAMP_HEIGHT = 10;

    private int baseWidth = DEFAULT_BASE_WIDTH;
    private int baseHeight = DEFAULT_BASE_HEIGHT;
    private int standLength = DEFAULT_STAND_LENGTH;
    private int lampWidth = DEFAULT_LAMP_WIDTH;
    private int lampHeight = DEFAULT_LAMP_HEIGHT;

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
            g.setColor(Color.ORANGE);
        }
        g.setStroke(new BasicStroke(2));

        // Calculate center point
        int centerX = startPoint.x + baseWidth / 2;
        int centerY = startPoint.y + (baseHeight + standLength + lampHeight) / 2;

        // Save the current graphics transformation
        AffineTransform oldTransform = g.getTransform();

        // Translate to the center point
        g.translate(centerX, centerY);

        // Rotate the graphics context
        g.rotate(Math.toRadians(rotationAngle));

        // Translate back to original position
        g.translate(-centerX, -centerY);

        // Draw lamp base
        g.drawRect(startPoint.x -baseWidth / 2, startPoint.y, baseWidth, baseHeight);

        // Draw lamp stand
        g.drawLine(startPoint.x , startPoint.y+ baseHeight, startPoint.x, startPoint.y + baseHeight + standLength);

        // Draw lamp shade
        g.drawArc(startPoint.x -lampWidth / 2, startPoint.y + baseHeight + standLength - lampHeight, lampWidth, lampHeight * 2, 0, 180);

        // Restore the old graphics transformation
        g.setTransform(oldTransform);
    }

    @Override
    public Shape getBounds() {
        // Calculate center point
        int centerX = startPoint.x + baseWidth / 2;
        int centerY = startPoint.y + (baseHeight + standLength + lampHeight) / 2;

        // Calculate the coordinates of the corners of the unrotated rectangle
        int x1 = -baseWidth-baseWidth/2;
        int y1 = -(baseHeight + standLength + lampHeight) / 2;
        int x2 = baseWidth / 2;
        int y2 = -(baseHeight + standLength + lampHeight) / 2;
        int x3 = baseWidth / 2;
        int y3 = (baseHeight + standLength) / 2;
        int x4 = -baseWidth-baseWidth/2;
        int y4 = (baseHeight + standLength) / 2;

        // Apply the rotation to each corner
        double cosTheta = Math.cos(Math.toRadians(rotationAngle));
        double sinTheta = Math.sin(Math.toRadians(rotationAngle));

        int[] xPoints = {(int) (x1 * cosTheta - y1 * sinTheta), (int) (x2 * cosTheta - y2 * sinTheta),
                (int) (x3 * cosTheta - y3 * sinTheta), (int) (x4 * cosTheta - y4 * sinTheta)};
        int[] yPoints = {(int) (x1 * sinTheta + y1 * cosTheta), (int) (x2 * sinTheta + y2 * cosTheta),
                (int) (x3 * sinTheta + y3 * cosTheta), (int) (x4 * sinTheta + y4 * cosTheta)};

        // Translate the rotated points to the center of the chair
        for (int i = 0; i < 4; i++) {
            xPoints[i] += centerX;
            yPoints[i] += centerY;
        }

        // Create a polygon from the rotated corners
        Polygon polygon = new Polygon(xPoints, yPoints, 4);

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
        baseWidth = (int) (scale * DEFAULT_BASE_WIDTH);
        baseHeight = (int) (scale * DEFAULT_BASE_HEIGHT);
        standLength = (int) (scale * DEFAULT_STAND_LENGTH);
        lampWidth = (int) (scale * DEFAULT_LAMP_WIDTH);
        lampHeight = (int) (scale * DEFAULT_LAMP_HEIGHT);
    }

    @Override
    public void rotate(int angle) {
        rotationAngle = angle;
    }
}
