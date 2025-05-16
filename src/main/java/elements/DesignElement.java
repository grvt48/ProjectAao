package elements;

import java.awt.*;
import java.io.Serializable;


public interface DesignElement extends Serializable {
    //Position
    public Point getStartPoint();
    public void setStartPoint(Point startPoint);

    //Draw
    public void draw(Graphics2D g);

    //Selection Logic
    public Shape getBounds();
    public boolean isSelected();
    public void setSelected(boolean selected);

    //Other Manipulations
    public void resize(double scale);
    public void rotate(int angle);
}

