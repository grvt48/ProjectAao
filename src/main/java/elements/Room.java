package elements;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Room implements DesignElement {
    private Point startPoint;
    private Point endPoint;
    private List<Wall> walls;
    private boolean isSelected = false; // Selection state
    private char type='z';

    public Room(Point startPoint) {
        this.startPoint = startPoint;
        this.endPoint = startPoint; // Initialize with startPoint
        this.walls = new ArrayList<>();
        generateWalls();
    }

    public Room(Point startPoint, char type) {
        this.type=type;
        this.startPoint = startPoint;
        this.endPoint = startPoint; // Initialize with startPoint
        this.walls = new ArrayList<>();
        generateWalls();
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
        generateWalls();
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
        generateWalls();
    }

    private void generateWalls() {
        walls.clear();

        int x1 = Math.min(startPoint.x, endPoint.x);
        int y1 = Math.min(startPoint.y, endPoint.y);
        int x2 = Math.max(startPoint.x, endPoint.x);
        int y2 = Math.max(startPoint.y, endPoint.y);

        Wall topWall = new Wall();
        topWall.setStartPoint(new Point(x1, y1));
        topWall.setEndPoint(new Point(x2, y1));

        Wall bottomWall = new Wall();
        bottomWall.setStartPoint(new Point(x1, y2));
        bottomWall.setEndPoint(new Point(x2, y2));

        Wall leftWall = new Wall();
        leftWall.setStartPoint(new Point(x1, y1));
        leftWall.setEndPoint(new Point(x1, y2));

        Wall rightWall = new Wall();
        rightWall.setStartPoint(new Point(x2, y1));
        rightWall.setEndPoint(new Point(x2, y2));

        walls.add(topWall);
        walls.add(bottomWall);
        walls.add(leftWall);
        walls.add(rightWall);
    }

    public boolean overlaps(Room other) {
        int x1 = Math.min(startPoint.x, endPoint.x);
        int y1 = Math.min(startPoint.y, endPoint.y);
        int x2 = Math.max(startPoint.x, endPoint.x);
        int y2 = Math.max(startPoint.y, endPoint.y);
    
        int otherX1 = Math.min(other.startPoint.x, other.endPoint.x);
        int otherY1 = Math.min(other.startPoint.y, other.endPoint.y);
        int otherX2 = Math.max(other.startPoint.x, other.endPoint.x);
        int otherY2 = Math.max(other.startPoint.y, other.endPoint.y);
    
        return x1 < otherX2 && x2 > otherX1 && y1 < otherY2 && y2 > otherY1;
    }    

    public int getWidth() {
        return Math.abs(endPoint.x - startPoint.x);
    }

    // Get the height of the room (difference in y coordinates)
    public int getHeight() {
        return Math.abs(endPoint.y - startPoint.y);
    }

    @Override
    public void draw(Graphics2D g) {
        if (startPoint == null || endPoint == null) return;

        // Calculate room dimensions
        int x = Math.min(startPoint.x, endPoint.x);
        int y = Math.min(startPoint.y, endPoint.y);
        int width = Math.abs(endPoint.x - startPoint.x);
        int height = Math.abs(endPoint.y - startPoint.y);

        // Fill the room interior with a color (different if selected)
        if (isSelected) {
            g.setColor(new Color(255, 0, 255, 100)); // Magenta with transparency for selected
        } else {
            switch (type) {
                case 'a':
                    g.setColor(new Color(115, 186, 155)); // Green for Bedroom
                    break;
                case 'd':
                    g.setColor(new Color(95, 168, 211)); // Blue for Kitchen
                    break;
                case 'b':
                    g.setColor(new Color(214, 40, 40,210)); // Red for Dining Room
                    break;
                case 'c':
                    g.setColor(new Color(252, 191, 73)); // Yellow for Bathroom
                    break;
                default:
                    g.setColor(new Color(173, 216, 230)); // Default color
                    break;
            }            
        }
        g.fillRect(x, y, width, height);

        // Draw walls (outline)
        g.setColor(isSelected ? Color.MAGENTA : Color.BLACK); // Magenta for selected walls
        for (Wall wall : walls) {
            wall.draw(g);
        }
    }

    @Override
    public Shape getBounds() {
        int x1 = Math.min(startPoint.x, endPoint.x);
        int y1 = Math.min(startPoint.y, endPoint.y);
        int width = Math.abs(startPoint.x - endPoint.x);
        int height = Math.abs(startPoint.y - endPoint.y);
        return new Rectangle(x1, y1, width, height);
    }

    @Override
    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    @Override
    public void resize(double scale) {
        // Optional: Implement resizing if needed
    }

    @Override
    public void rotate(int angle) {
        
    }

    @Override
    public boolean isSelected() {
        return isSelected; // Return the selection status
    }

    public List<Point[]> getBoundaryWalls() {
        List<Point[]> boundaryWalls = new ArrayList<>();
        for (Wall wall : walls) {
            boundaryWalls.add(new Point[]{wall.getStartPoint(), wall.getEndPoint()});
        }
        return boundaryWalls;
    }
    

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected; // Update the selection status
    }

    public char getRoomType() {
        return type;
    }
}
