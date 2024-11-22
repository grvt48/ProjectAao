package elements;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Class representing a Door design element.
 *
 * @author ChatGPT, Wahad Latif
 */
public class Door implements DesignElement{
    private int DEFUALT_Door_THICKNESS = 3;
    protected int DoorThickness = DEFUALT_Door_THICKNESS;
    private Point startPoint;
    private Point endPoint;
    private boolean isSelected = false;
    private int rotationAngle = 0;

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }
    
    public void setStartPoint(Point startPoint) {
        //First start point
        if(this.endPoint == null){
            this.startPoint = startPoint;
            this.endPoint = startPoint; // Reset end point to start point initially
        //Moving the Door, adjust endpoint too
        }else{
            if(this.startPoint.x == this.endPoint.x){
                int ydiff = this.endPoint.y - this.startPoint.y;
                this.startPoint = startPoint;
                this.endPoint = new Point(this.startPoint.x, this.startPoint.y + ydiff);
            }else{
                int xdiff = this.endPoint.x - this.startPoint.x;
                this.startPoint = startPoint;
                this.endPoint = new Point(this.startPoint.x + xdiff, this.startPoint.y);
            }
        }
    }
        
    public void setEndPoint(Point endPoint) {
        this.endPoint = calculateAdjustedEnd(startPoint, endPoint);
    }

    private Point calculateAdjustedEnd(Point start, Point end) {
        int dx = Math.abs(end.x - start.x);
        int dy = Math.abs(end.y - start.y);
        if (dx > dy) {
            return new Point(end.x, start.y);
        } else {
            return new Point(start.x, end.y);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (startPoint != null && endPoint != null && endPoint != startPoint) {
            if (isSelected == true) {
        		g.setColor(Color.MAGENTA);
        	} else {
        		g.setColor(Color.WHITE);
        	}
            g.setStroke(new BasicStroke(DoorThickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
            
            //first draw
            if(rotationAngle == 0){
                g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
            //rotate draw
            }else{
                // Save the current graphics transformation
                AffineTransform oldTransform = g.getTransform();

                // Translate and rotate the graphics context to draw at the desired position and angle
                g.translate(startPoint.x, startPoint.y);
                g.rotate(Math.toRadians(rotationAngle));

                g.drawLine(0, 0, endPoint.x - startPoint.x, endPoint.y - startPoint.y);

                // Restore the old graphics transformation
                g.setTransform(oldTransform);
            }  
        }
    }

    @Override
    public Shape getBounds() {
        if (startPoint == null || endPoint == null) {
            return new Rectangle();
        }

        // Calculate the angle of rotation
        double angle = Math.toRadians(rotationAngle);

        // Calculate the half thickness of the Door
        int halfThickness = DoorThickness / 2;

        // Calculate the unrotated coordinates of the four corners of the bounding box
        int[] xPoints = {startPoint.x - halfThickness, endPoint.x + halfThickness, endPoint.x + halfThickness, startPoint.x - halfThickness};
        if(startPoint.x > endPoint.x){
            xPoints[0] = startPoint.x + halfThickness;
            xPoints[1] = endPoint.x - halfThickness; 
            xPoints[2] = endPoint.x - halfThickness; 
            xPoints[3] = startPoint.x + halfThickness; 
        }
        if(startPoint.y != endPoint.y){
            xPoints[1] = startPoint.x - halfThickness; 
            xPoints[3] = endPoint.x + halfThickness; 
        }

        int[] yPoints = {startPoint.y - halfThickness, endPoint.y - halfThickness, endPoint.y + halfThickness, startPoint.y + halfThickness};
        if(startPoint.y < endPoint.y){
            yPoints[1] = endPoint.y + halfThickness; 
            yPoints[3] = startPoint.y - halfThickness; 
        }
        if(startPoint.y > endPoint.y){
            yPoints[0] = startPoint.y + halfThickness; 
            yPoints[2] = endPoint.y - halfThickness; 
        }

        // Rotate the corners around the start point
        int[] rotatedXPoints = new int[4];
        int[] rotatedYPoints = new int[4];
        for (int i = 0; i < 4; i++) {
            int dx = xPoints[i] - startPoint.x;
            int dy = yPoints[i] - startPoint.y;
            rotatedXPoints[i] = startPoint.x + (int) (dx * Math.cos(angle) - dy * Math.sin(angle));
            rotatedYPoints[i] = startPoint.y + (int) (dx * Math.sin(angle) + dy * Math.cos(angle));
        }

        // Create a polygon from the rotated corners
        Polygon polygon = new Polygon(rotatedXPoints, rotatedYPoints, 4);

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
        DoorThickness = (int) (scale * DEFUALT_Door_THICKNESS);
    }

    @Override
    public void rotate(int angle) {
    }
}