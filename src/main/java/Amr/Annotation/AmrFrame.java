package Amr.Annotation;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class AmrFrame extends JFrame implements ActionListener {

    private final JMenuItem itemClose;
    private final JMenuItem itemUndo;
    private final JMenuItem itemCloseAll;
    private final JMenuItem itemSave;
    private final JMenuItem itemExport;
    private final JMenuItem itemPaste;
    private final JTabbedPane diagramPane;
    private ArrayList<AmrObject> copyList = null;
    static final private String EMPTY = "empty";
    static final private String WORD = "word";
    static final private String CONNECTION = "connection";
    static final private String FORWARD = "forward";
    static final private String BACKWARD = "backward";

    private JMenuItem addMenuItem(JMenu menu, String name, KeyStroke stroke) {
        JMenuItem newItem;
        newItem = new JMenuItem(name);
        newItem.setAccelerator(stroke);
        menu.add(newItem);
        return newItem;
    }

    protected JButton makeDrawingButton(String imageName, String actionCommand, String toolTipText) {
        String imgLocation = "/icons/" + imageName + ".gif";
        URL imageURL = AmrFrame.class.getResource(imgLocation);
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(this);
        if (imageURL != null) {
            button.setIcon(new ImageIcon(imageURL));
        } else {
            button.setText(toolTipText);
            System.err.println("Resource not found: " + imgLocation);
        }
        return button;
    }

    private void addButtons(JToolBar toolBar) {
        JButton button;
        button = makeDrawingButton("empty", EMPTY, "");
        button.setVisible(false);
        toolBar.add(button);
        button = makeDrawingButton("word", WORD, "Add Word");
        button.setVisible(false);
        toolBar.add(button);
        button = makeDrawingButton("connection", CONNECTION, "Add Connection");
        button.setVisible(false);
        toolBar.add(button);
        button = makeDrawingButton("backward", BACKWARD, "Backward");
        button.setVisible(false);
        toolBar.add(button);
        button = makeDrawingButton("forward", FORWARD, "Forward");
        button.setVisible(false);
        toolBar.add(button);
    }

    private void enableMenu() {
        itemClose.setEnabled(true);
        itemCloseAll.setEnabled(true);
        itemSave.setEnabled(true);
        itemExport.setEnabled(true);
    }

    private void disableMenu() {
        itemClose.setEnabled(false);
        itemCloseAll.setEnabled(false);
        itemSave.setEnabled(false);
        itemExport.setEnabled(false);
        itemPaste.setEnabled(false);
    }

    public AmrFrame() {
        JMenuBar menu;
        JMenu fileMenu;
        JMenu editMenu;
        JMenu newMenu;
        menu = new JMenuBar();
        setJMenuBar(menu);
        fileMenu = new JMenu("File");
        menu.add(fileMenu);
        newMenu = new JMenu("New");
        fileMenu.add(newMenu);
        JMenuItem itemDiagram = addMenuItem(newMenu, "Diagram", KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        JMenuItem itemOpen = addMenuItem(fileMenu, "Open", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        itemSave = addMenuItem(fileMenu, "Save", KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        itemExport = addMenuItem(fileMenu, "Export", KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        itemClose = addMenuItem(fileMenu, "Close", null);
        itemCloseAll = addMenuItem(fileMenu, "Close All", null);
        editMenu = new JMenu("Edit");
        menu.add(editMenu);
        itemUndo = addMenuItem(editMenu, "Undo", KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        JMenuItem itemCut = addMenuItem(editMenu, "Cut", KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        JMenuItem itemCopy = addMenuItem(editMenu, "Copy", KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        itemPaste = addMenuItem(editMenu, "Paste", KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        JMenuItem itemDelete = addMenuItem(editMenu, "Delete", KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
        JMenuItem itemSelectAll = addMenuItem(editMenu, "Select All", KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        diagramPane = new JTabbedPane();
        disableMenu();
        add(diagramPane, BorderLayout.CENTER);
        final JToolBar ToolBar = new JToolBar("ToolBox");
        addButtons(ToolBar);
        add(ToolBar, BorderLayout.PAGE_START);
        ToolBar.setVisible(true);
        diagramPane.addChangeListener(c -> {
            int i;
            if (diagramPane.getSelectedIndex() != -1) {
                itemUndo.setEnabled(true);
                for (i = 0; i < 5; i++) {
                    ToolBar.getComponent(i).setVisible(true);
                }
                itemPaste.setEnabled(true);
                diagramPane.getSelectedComponent().repaint();
            } else {
                for (i = 0; i < 5; i++) {
                    ToolBar.getComponent(i).setVisible(false);
                }
            }
        });
        itemDiagram.addActionListener(e -> {
            AmrPanel newPanel;
            newPanel = new AmrPanel(".", "0000.train");
            diagramPane.add(newPanel, "Diagram", diagramPane.getSelectedIndex() + 1);
            enableMenu();
        });
        itemClose.addActionListener(e -> {
            diagramPane.remove(diagramPane.getSelectedIndex());
            if (diagramPane.getTabCount() == 0) {
                disableMenu();
            }
        });
        itemCloseAll.addActionListener(e -> {
            while (diagramPane.getSelectedIndex() != -1) {
                diagramPane.remove(diagramPane.getSelectedIndex());
            }
            disableMenu();
        });
        itemUndo.addActionListener(e -> {
            DiagramPanel current;
            current = (DiagramPanel) diagramPane.getSelectedComponent();
            if (current != null) {
                current.undo();
                current.repaint();
            }
        });
        itemSelectAll.addActionListener(e -> {
            DiagramPanel current;
            current = (DiagramPanel) diagramPane.getSelectedComponent();
            if (current != null) {
                current.getDiagram().selectAll();
                current.repaint();
            }
        });
        itemCopy.addActionListener(e -> {
            DiagramPanel current;
            current = (DiagramPanel) diagramPane.getSelectedComponent();
            if (current != null) {
                copyList = current.getDiagram().copyAll();
            }
        });
        itemCut.addActionListener(e -> {
            DiagramPanel current;
            current = (DiagramPanel) diagramPane.getSelectedComponent();
            if (current != null) {
                copyList = current.getDiagram().copyAll();
                current.save();
                current.getDiagram().deleteSelected();
                current.repaint();
            }
        });
        itemPaste.addActionListener(e -> {
            DiagramPanel current;
            current = (DiagramPanel) diagramPane.getSelectedComponent();
            if (current != null) {
                current.save();
                current.getDiagram().pasteObjects(copyList);
                current.repaint();
            }
        });
        itemDelete.addActionListener(e -> {
            DiagramPanel current;
            current = (DiagramPanel) diagramPane.getSelectedComponent();
            if (current != null) {
                current.save();
                current.getDiagram().deleteSelected();
                current.repaint();
            }
        });
        itemExport.addActionListener(e -> {
            String filename;
            final JFileChooser fcoutput = new JFileChooser();
            fcoutput.setDialogTitle("Select output jpg file");
            fcoutput.setDialogType(JFileChooser.SAVE_DIALOG);
            int returnVal = fcoutput.showSaveDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                DiagramPanel current;
                current = (DiagramPanel) diagramPane.getSelectedComponent();
                filename = fcoutput.getSelectedFile().getAbsolutePath();
                BufferedImage image = new BufferedImage(current.getWidth(), current.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = image.createGraphics();
                g2.fillRect(0, 0, current.getWidth(), current.getHeight());
                current.paint(g2);
                try {
                    ImageIO.write(image, "jpeg", new File(filename));
                } catch (IOException ioException) {
                    System.out.println("Output file can not be opened");
                }
                image.flush();
            }
        });
        itemSave.addActionListener(e -> {
            DiagramPanel current;
            current = (DiagramPanel) diagramPane.getSelectedComponent();
            if (current.getFileName() == null) {
                final JFileChooser fcoutput = new JFileChooser();
                fcoutput.setDialogTitle("Select output file");
                fcoutput.setDialogType(JFileChooser.SAVE_DIALOG);
                int returnVal = fcoutput.showSaveDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String filename;
                    filename = fcoutput.getSelectedFile().getAbsolutePath();
                    diagramPane.setToolTipTextAt(diagramPane.getSelectedIndex(), fcoutput.getSelectedFile().getName());
                    current.setFileName(filename);
                    current.getDiagram().save(filename);
                }
            } else {
                current.getDiagram().save(current.getFileName());
            }
        });
        itemOpen.addActionListener(e -> {
            String filename;
            final JFileChooser fcinput = new JFileChooser();
            fcinput.setDialogTitle("Select diagram file");
            fcinput.setDialogType(JFileChooser.OPEN_DIALOG);
            int returnVal = fcinput.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                Node rootNode;
                filename = fcinput.getSelectedFile().getAbsolutePath();
                DOMParser parser = new DOMParser();
                Document doc;
                try {
                    parser.parse(filename);
                } catch (SAXException | IOException ignored) {
                }
                doc = parser.getDocument();
                rootNode = doc.getFirstChild();
                AmrPanel newPanel;
                newPanel = new AmrPanel(fcinput.getSelectedFile().getParent(), fcinput.getSelectedFile().getName());
                diagramPane.add(newPanel, fcinput.getSelectedFile().getName(), diagramPane.getSelectedIndex() + 1);
                newPanel.getDiagram().loadFromXml(rootNode);
                enableMenu();
            }
        });
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DiagramPanel current;
        String cmd = e.getActionCommand();
        EnumCommand lastCommand = EnumCommand.EMPTY;
        current = (DiagramPanel) diagramPane.getSelectedComponent();
        if (EMPTY.equals(cmd)) {
            lastCommand = EnumCommand.EMPTY;
            current.setCommand(lastCommand);
        } else {
            if (WORD.equals(cmd)) {
                lastCommand = EnumCommand.WORD;
                current.setCommand(lastCommand);
            } else {
                if (CONNECTION.equals(cmd)) {
                    lastCommand = EnumCommand.CONNECTION;
                    current.setCommand(lastCommand);
                } else {
                    if (FORWARD.equals(cmd)){
                        current.nextAmr(1);
                        repaint();
                    } else {
                        if (BACKWARD.equals(cmd)){
                            current.previousAmr(1);
                            repaint();
                        }
                    }
                }
            }
        }
    }

    public static void prepareData() throws FileNotFoundException {
        int startX = 750, startY = 100;
        String[] lastParent = new String[10];
        int[] lastX = new int[10];
        int[] childCount = new int[10];
        PrintWriter output = null;
        HashSet<String> words = new HashSet<>();
        boolean firstLine = false, skipFile = false;
        Scanner input = new Scanner(new File("framenet.txt"));
        while (input.hasNext()){
            String line = input.nextLine();
            if (line.matches(".*[0-9]+\\.(train|test|dev)")){
                System.out.println(line);
                String folder = ".";
                if (line.contains("/")){
                    folder = line.substring(0, line.indexOf("/"));
                    File file = new File("./" + folder);
                    if (!file.exists()){
                        file.mkdir();
                    }
                    line = line.substring(line.indexOf("/") + 1);
                }
                output = new PrintWriter(folder + "/" + line);
                words = new HashSet<>();
                firstLine = true;
                skipFile = false;
            } else {
                if (line.isEmpty()){
                    if (!skipFile){
                        output.println("</Amr>");
                    }
                    output.close();
                } else {
                    if (firstLine){
                        if (line.startsWith("\t")){
                            skipFile = true;
                        } else {
                            output.println("<Amr>");
                            words.add(line);
                            output.println("<Word name=\"" + line + "\" positionX=\"" + startX + "\" positionY=\"" + startY + "\"/>");
                            lastParent[0] = line;
                            childCount[0] = 0;
                            lastX[0] = startX;
                        }
                        firstLine = false;
                    } else {
                        if (!skipFile){
                            int tabCount = 0, i = 0;
                            while (line.charAt(i) == '\t'){
                                tabCount++;
                                i++;
                            }
                            line = line.substring(tabCount);
                            lastParent[tabCount] = line;
                            childCount[tabCount] = 0;
                            lastX[tabCount] = lastX[tabCount - 1] + (childCount[tabCount - 1] - 1) * 100;
                            if (!words.contains(line)){
                                output.println("<Word name=\"" + line + "\" positionX=\"" + lastX[tabCount] + "\" positionY=\"" + (startY + 100 * tabCount) + "\"/>");
                                words.add(line);
                            }
                            output.println("<Connection from=\"" + lastParent[tabCount - 1] + "\" to=\"" + line + "\"/>");
                            childCount[tabCount - 1]++;
                        }
                    }
                }
            }
        }
        output.close();
    }
}
