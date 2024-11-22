package floorplan;

import java.util.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;

import elements.*;
import functions.*;

/**
 * Panel for drawing on a canvas.
 *
 * @author ChatGPT, Wahad Latif
 */
public class DrawingPanel extends JPanel implements ElementSelectedObserver, FunctionSelectedObserver {
    //The canvas image
    private BufferedImage canvas;
    //The last mouse point
    private Point lastPoint;

    //All design elements on canvas that are drawn
    private List<DesignElement> designElements;
    //Currently drawing element (previewing on canvas, draw will be finalized on click)
    private DesignElement currentElement;

    //Manipulation Functions
    private Select selectFunction;
    private Move moveFunction;
    private Remove removeFunction;
    private Resize resizeSlider;
    private Rotate rotateSlider;
    //Currently selected function
    private ManipulationFunction currentFunction;

    //Nikka gridsnap
    private boolean isGridSnapEnabled = true;
    private int gridSize = 20;

    //Nikka snapper attempt
    private Point snapToGrid(Point point) {
        if (!isGridSnapEnabled) {
            return point; // Snap off = original point
        }
        int x = Math.round(point.x / (float) gridSize) * gridSize;
        int y = Math.round(point.y / (float) gridSize) * gridSize;
        return new Point(x, y);
    }


    public DrawingPanel(int width, int height) {
        //Create the canvas image and set the focus
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        clearCanvas();
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        setBackground(Color.WHITE);


        //Initialize variables
        designElements = new ArrayList<>();

        selectFunction = new Select(this);
        moveFunction = new Move(this, selectFunction);
        removeFunction = new Remove(this, selectFunction);
        resizeSlider = new Resize(this, selectFunction);
        rotateSlider = new Rotate(this, selectFunction);


        //Mouse Event Handlers (was too hard to move this to separate class)
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Return if it wasn't the left mouse button
                if (e.getButton() != MouseEvent.BUTTON1) {
                    return;
                }

                // Snap the point to the grid
                lastPoint = snapToGrid(e.getPoint());
                moveFunction.captureStartPositions();


                // ELEMENTS
                if (currentElement instanceof Wall) {
                    Wall newWall = new Wall();
                    currentElement = newWall;
                    currentElement.setStartPoint(lastPoint);
                    designElements.add(currentElement);
                } else if (currentElement instanceof Room) {
                    try {
                        Room newRoom = new Room(lastPoint); // Pass the required Point
                        currentElement = newRoom;
                        currentElement.setStartPoint(lastPoint);
                        designElements.add(currentElement);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (currentElement != null) {
                    try {
                        currentElement.setStartPoint(lastPoint);
                        designElements.add(currentElement);
                        currentElement = currentElement.getClass().getDeclaredConstructor().newInstance();
                        currentElement.setStartPoint(lastPoint);
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException |
                             InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                        ex.printStackTrace();
                    }
                }

                // FUNCTIONS
                if (currentFunction instanceof Select) {
                    selectFunction.startPoint = lastPoint;
                    selectFunction.endPoint = selectFunction.startPoint;
                }
                if (currentFunction instanceof Move) {
                    moveFunction.startDragPoint = lastPoint;
                }

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Return if it wasn't the left mouse button
                if (e.getButton() != MouseEvent.BUTTON1) {
                    return;
                }

                lastPoint = snapToGrid(e.getPoint());

                // ELEMENTS
                if (currentElement instanceof Wall) {
                    ((Wall) currentElement).setEndPoint(lastPoint);
                } else if (currentElement instanceof Room) {
                    ((Room) currentElement).setEndPoint(lastPoint);
                }
                if (currentElement instanceof Room) {
                    ((Room) currentElement).setEndPoint(lastPoint);

                    // Check for overlap with existing rooms
                    for (DesignElement element : designElements) {
                        if (element instanceof Room && element != currentElement) {
                            if (((Room) currentElement).overlaps((Room) element)) {
                                JOptionPane.showMessageDialog(DrawingPanel.this, "Overlap error");
                                designElements.remove(currentElement); // Remove the new room
                                currentElement = null;
                                repaint();
                                return;
                            }
                        }
                    }
                }

                // FUNCTIONS
                if (currentFunction instanceof Select) {
                    selectFunction.startPoint = null;
                    selectFunction.endPoint = null;
                }
                if (currentFunction instanceof Move) {
                    moveFunction.startDragPoint = null;
                }

                repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Return if it wasn't the left mouse button
                if (!SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }

                lastPoint = snapToGrid(e.getPoint());

                // ELEMENTS
                if (currentElement instanceof Wall) {
                    ((Wall) currentElement).setEndPoint(lastPoint);
                } else if (currentElement instanceof Room) {
                    ((Room) currentElement).setEndPoint(lastPoint);
                }

                repaint();

                // FUNCTIONS
                if (currentFunction instanceof Select || currentFunction instanceof Move) {
                    currentFunction.performFunction(lastPoint);
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                lastPoint = snapToGrid(e.getPoint());

                // Preview of the current element
                if (currentElement != null && !(currentElement instanceof Wall || currentElement instanceof Room)) {
                    currentElement.setStartPoint(lastPoint);
                }

                repaint();
            }
        });


        //Keyboard Event Handler (was too hard to move this to separate class)
        addKeyListener(new KeyAdapter() {
            //Implementing keyboard shortcuts
            @Override
            public void keyPressed(KeyEvent e) {
                //ESCAPE, not placing a design element anymore so discard the preview current element
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    currentElement = null;
                    repaint();
                }

                //SPACE, unselect to finalize manipulations
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    selectFunction.clearSelection();
                    resizeSlider.setVisible(false);
                    rotateSlider.setVisible(false);
                    repaint();
                }

                //S, select
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    onFunctionSelected(selectFunction);
                }

                //M, move
                if (e.getKeyCode() == KeyEvent.VK_M) {
                    onFunctionSelected(moveFunction);
                }

                //R, remove
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    onFunctionSelected(removeFunction);
                }

                //T, rotate
                if (e.getKeyCode() == KeyEvent.VK_T) {
                    onFunctionSelected(rotateSlider);
                }

                /*//Z, resize
                if (e.getKeyCode() == KeyEvent.VK_Z) {
                    onFunctionSelected(resizeSlider);
                }*/

                //CTRL+S, save floorplan to file
                if (e.getKeyCode() == KeyEvent.VK_S && e.isControlDown()) {
                    saveImage();
                }

                //CTRL+O, load floorplan from file
                if (e.getKeyCode() == KeyEvent.VK_O && e.isControlDown()) {
                    loadImage();
                }
            }
        });

        // Component Event Handler for resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeCanvas(getWidth(), getHeight());
            }
        });
    }

    //This function is continuously automatically called, and repaint() calls it manually
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Erase everything
        eraseCanvas();
        //Draw the grid
        drawGrid(g);
        //Draw elements onto canvas
        drawElements(designElements);
        //Draw the canvas image to panel
        g.drawImage(canvas, 0, 0, null);
    }

    private void drawGrid(Graphics g) {
        // Draw the grid lines
        int gridSize = 20; // 20X20 pixel grid squares
        g.setColor(Color.LIGHT_GRAY);
        for (int x = 0; x < getWidth(); x += gridSize) {
            g.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += gridSize) {
            g.drawLine(0, y, getWidth(), y);
        }
    }

    private void drawElements(List<DesignElement> designElements) {
        Graphics2D g2d = canvas.createGraphics();

        //Draw the design elements
        for (DesignElement element : designElements){
            element.draw(g2d);
        }

        //also draw selection rectangle if needed
        if(currentFunction instanceof Select){
            selectFunction.draw(g2d);
        }

        //also draw preview of current element if needed
        if(currentElement != null){
            currentElement.draw(g2d);
        }

        g2d.dispose();
    }

    @Override
    public void onElementSelected(DesignElement element) {
        try {
            if (element instanceof Room) {
                // For Room, use a constructor that initializes with a Point
                currentElement = new Room(new Point(0, 0)); // Default start point
            } else {
                // For other elements, use reflection to create a copy
                currentElement = element.getClass().getDeclaredConstructor().newInstance();
            }

            // Initialize the element with the current mouse position
            currentElement.setStartPoint(lastPoint);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            ex.printStackTrace();
        }

        // Reset the current function since we're now in "element placement" mode
        currentFunction = null;

        // Clear any selection and hide adjustment sliders
        selectFunction.clearSelection();
        resizeSlider.setVisible(false);
        rotateSlider.setVisible(false);

        // Ensure focus stays on the drawing panel
        requestFocusInWindow();
    }


    //User clicked on the functions toolbar
    @Override
    public void onFunctionSelected(ManipulationFunction function) {
        currentElement = null;
        currentFunction = function;

        //Perform remove immediately on click
        if(currentFunction instanceof Remove){
            currentFunction.performFunction(lastPoint);
            currentFunction = null;
        }

        //Show resize slider if appropriate // turned it off
        if(currentFunction instanceof Resize){
            setLayout(new BorderLayout());
            add(resizeSlider, BorderLayout.NORTH);
            resizeSlider.setVisible(false);
        }else{
            resizeSlider.setVisible(false);
        }

        //Show rotate slider if appropriate
        if(currentFunction instanceof Rotate){
            setLayout(new BorderLayout());
            add(rotateSlider, BorderLayout.NORTH);
            rotateSlider.setVisible(true);
        }else{
            rotateSlider.setVisible(false);
        }

        //Request focus for the panel (move focus away from function bar)
        requestFocusInWindow();
        //Hide current element preview (current element is now null, we are manipulating not placing)
        repaint();
    }

    //Resize canvas if panel resized
    private void resizeCanvas(int width, int height) {
        BufferedImage newCanvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = newCanvas.createGraphics();
        g2d.drawImage(canvas, 0, 0, null);
        g2d.dispose();
        canvas = newCanvas;
        repaint();
    }

    //Only erasing the canvas image, Keep design elements
    public void eraseCanvas() {
        Graphics2D g2d = canvas.createGraphics();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2d.setComposite(AlphaComposite.SrcOver);
        g2d.dispose();
    }

    //Erase the canvas image and design elements
    public void clearCanvas() {
        eraseCanvas();
        if(designElements != null){
            selectFunction.clearSelection();
            designElements.clear();
        }
        repaint();
    }

    //Nikka turn on grid snapping
    public void setGridSnapEnabled(boolean enabled) {
        isGridSnapEnabled = enabled;
    }


    //Save floorplan to file
    public void saveImage() {
        //clear selections before saving
        selectFunction.clearSelection();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Image");
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileOutputStream fos = new FileOutputStream(file);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                // Serialize the design elements and canvas
                oos.writeObject(designElements);
                repaint();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //Load floorplan from file
    @SuppressWarnings("unchecked")
    public void loadImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open Image");
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                // Deserialize the design elements
                designElements = (List<DesignElement>) ois.readObject();
                repaint();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    //Getters
    public List<DesignElement> getDesignElements(){
        return designElements;
    }

    public Select getSelect(){
        return selectFunction;
    }

    public Move getMove(){
        return moveFunction;
    }

    public Remove getRemove(){
        return removeFunction;
    }

    public Rotate getRotate(){
        return rotateSlider;
    }

    public Resize getResize(){
        return resizeSlider;
    }
}