package floorplan;

import java.awt.*;
import javax.swing.*;

import com.formdev.flatlaf.FlatDarkLaf;
import elements.*;
import functions.*;

/**
 * Simple Floor Plan Designer using Java Swing.
 * Allows users to draw, save, load, and clear floor plans.
 *
 * @author ChatGPT, Wahad Latif, Kevin Cao
 */
public class App extends JFrame {
    private DrawingPanel drawingPanel;
    private BarElement elementbox;
    private BarFunction functionbox;

    public App() {
        super("Simple Floor Plan Designer");
        initUI();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new FlatDarkLaf());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        SwingUtilities.invokeLater(() -> new App().setVisible(true));
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Create and add the drawing panel to the center with resolution 1200x700
        drawingPanel = new DrawingPanel(1200, 700);
        add(drawingPanel, BorderLayout.CENTER);

        // Create and add the toolbox to the east
        elementbox = new BarElement();
        add(elementbox, BorderLayout.WEST);
        elementbox.addObserver(drawingPanel);

        // Create and add the functionbox to the north
        functionbox = new BarFunction();
        add(functionbox, BorderLayout.NORTH);
        functionbox.addObserver(drawingPanel);

        // Add functions to functionbox
        functionbox.addFunction(drawingPanel.getSelect());
        functionbox.addFunction(drawingPanel.getMove());
        functionbox.addFunction(drawingPanel.getRemove());
        //functionbox.addFunction(drawingPanel.getResize());
        functionbox.addFunction(drawingPanel.getRotate());

        // Create and add the menu bar at the top
        setupMenuBar();

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set the drawing panel to be focused for keyboard shortcuts
        drawingPanel.requestFocusInWindow();
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createHelpMenu());

        setJMenuBar(menuBar);
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem saveItem = new JMenuItem("Save   (Ctrl+S)");
        saveItem.addActionListener(e -> drawingPanel.saveImage());
        fileMenu.add(saveItem);

        JMenuItem loadItem = new JMenuItem("Load   (Ctrl+O)");
        loadItem.addActionListener(e -> drawingPanel.loadImage());
        fileMenu.add(loadItem);

        fileMenu.add(new JSeparator());

        JMenuItem exitItem = new JMenuItem("Exit     (Alt+F4)");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        return fileMenu;
    }

    private JMenu createEditMenu() {
        JMenu editMenu = new JMenu("Edit");

        JMenuItem clearItem = new JMenuItem("Clear");
        clearItem.addActionListener(e -> drawingPanel.clearCanvas());
        editMenu.add(clearItem);

        return editMenu;
    }

    private JMenu createViewMenu() {
        JMenu viewMenu = new JMenu("View");

        // Toggle grid snap
        JCheckBoxMenuItem gridSnapItem = new JCheckBoxMenuItem("Enable Grid Snap");
        gridSnapItem.setSelected(true); // Enable grid snap by default
        gridSnapItem.addActionListener(e -> drawingPanel.setGridSnapEnabled(gridSnapItem.isSelected()));
        viewMenu.add(gridSnapItem);

        return viewMenu;
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Simple Floorplan Application\nVersion 1.0\n Created by Garvt, Raghav, Aniket, Rhythm", "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);

        return helpMenu;
    }
}
