package elements;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Class representing a plant design element.
 *
 * @author Wahad Latif
 */
public class Plant implements DesignElement {
    private static final int DEFAULT_FLOWER_WIDTH = 40;
    private static final int DEFAULT_FLOWER_HEIGHT = 40;

    private int flowerWidth = DEFAULT_FLOWER_WIDTH;
    private int flowerHeight = DEFAULT_FLOWER_HEIGHT;

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
            g.setColor(Color.PINK);
        }

        // Save the current graphics transformation
        AffineTransform oldTransform = g.getTransform();

        // Translate and rotate the graphics context to draw the flower at the desired position and angle
        g.translate(startPoint.x, startPoint.y);
        g.rotate(Math.toRadians(rotationAngle));

        // Draw petals
        int petalRadius = Math.min(flowerWidth, flowerHeight) / 2;
        double angleIncrement = 2 * Math.PI / 5;
        double angle = 0;
        int[] xPoints = new int[5];
        int[] yPoints = new int[5];
        for (int i = 0; i < 5; i++) {
            xPoints[i] = (int) (petalRadius * Math.cos(angle));
            yPoints[i] = (int) (petalRadius * Math.sin(angle));
            angle += angleIncrement;
        }
        g.fillPolygon(xPoints, yPoints, 5);

        // Draw flower center
        g.setColor(Color.YELLOW);
        int centerSize = Math.min(flowerWidth, flowerHeight) / 4;
        g.fillOval(-centerSize / 2, -centerSize / 2, centerSize, centerSize);

        // Restore the old graphics transformation
        g.setTransform(oldTransform);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(startPoint.x - flowerWidth / 2, startPoint.y - flowerHeight / 2, flowerWidth, flowerHeight);
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
        flowerWidth = (int) (scale * DEFAULT_FLOWER_WIDTH);
        flowerHeight = (int) (scale * DEFAULT_FLOWER_HEIGHT);
    }

    @Override
    public void rotate(int angle) {
        rotationAngle = angle;
    }
}