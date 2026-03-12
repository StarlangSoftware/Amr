package Amr.Annotation;

import AnnotatedSentence.AnnotatedSentence;
import WordNet.WordNet;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Properties;

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
    static final private String FASTFORWARD = "fastforward";
    static final private String FASTBACKWARD = "fastbackward";
    static final private String FASTFASTFORWARD = "fastfastforward";
    static final private String FASTFASTBACKWARD = "fastfastbackward";
    String amrPath;
    String phrasePath;
    String subFolder;
    protected JLabel infoBottom;
    protected JPanel bottom;
    private AnnotatedSentence sentence;
    private final WordNet wordNet;

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
        button = makeDrawingButton("fastfastbackward", FASTFASTBACKWARD, "Fast Fast Backward");
        button.setVisible(false);
        toolBar.add(button);
        button = makeDrawingButton("fastbackward", FASTBACKWARD, "Fast Backward");
        button.setVisible(false);
        toolBar.add(button);
        button = makeDrawingButton("backward", BACKWARD, "Backward");
        button.setVisible(false);
        toolBar.add(button);
        button = makeDrawingButton("forward", FORWARD, "Forward");
        button.setVisible(false);
        toolBar.add(button);
        button = makeDrawingButton("fastforward", FASTFORWARD, "Fast Forward");
        button.setVisible(false);
        toolBar.add(button);
        button = makeDrawingButton("fastfastforward", FASTFASTFORWARD, "Fast Fast Forward");
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

    private void getAnnotatedSentence(DiagramPanel panel){
        if (subFolder.equals("true")){
            sentence = new AnnotatedSentence(new File(phrasePath + "/" + panel.getFolder() + "/" + panel.getFileName()));
        } else {
            sentence = new AnnotatedSentence(new File(phrasePath + "/" + panel.getFileName()));
        }
        infoBottom.setText(sentence.toWords());
    }

    public AmrFrame() {
        JMenuBar menu;
        JMenu fileMenu;
        JMenu editMenu;
        JMenu newMenu;
        menu = new JMenuBar();
        setJMenuBar(menu);
        wordNet = new WordNet();
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
        diagramPane.setFocusable(false);
        disableMenu();
        add(diagramPane, BorderLayout.CENTER);
        final JToolBar ToolBar = new JToolBar("ToolBox");
        addButtons(ToolBar);
        add(ToolBar, BorderLayout.PAGE_START);
        ToolBar.setVisible(true);
        bottom = new JPanel(new BorderLayout());
        infoBottom = new JLabel("Cümle Burada Olacak ");
        infoBottom.setForeground(Color.RED);
        bottom.add(infoBottom, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);
        Properties properties1 = new Properties();
        amrPath = ".";
        phrasePath = "";
        subFolder = "false";
        try {
            properties1.load(Files.newInputStream(new File("config.properties").toPath()));
            amrPath = properties1.getProperty("amrPath");
            phrasePath = properties1.getProperty("phrasePath");
            subFolder = properties1.getProperty("subFolder");
        } catch (IOException ignored) {
        }
        diagramPane.addChangeListener(c -> {
            int i;
            if (diagramPane.getSelectedIndex() != -1) {
                itemUndo.setEnabled(true);
                for (i = 0; i < 9; i++) {
                    ToolBar.getComponent(i).setVisible(true);
                }
                itemPaste.setEnabled(true);
                diagramPane.getSelectedComponent().repaint();
            } else {
                for (i = 0; i < 9; i++) {
                    ToolBar.getComponent(i).setVisible(false);
                }
            }
        });
        itemDiagram.addActionListener(e -> {
            AmrPanel newPanel;
            newPanel = new AmrPanel(".", "0000.train");
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setViewportView(newPanel);
            diagramPane.add(scrollPane, "Diagram", diagramPane.getSelectedIndex() + 1);
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
                current.undoList.add(current.diagram.clone());
                current.getDiagram().deleteSelected();
                current.save();
                current.repaint();
            }
        });
        itemPaste.addActionListener(e -> {
            DiagramPanel current;
            current = (DiagramPanel) diagramPane.getSelectedComponent();
            if (current != null) {
                current.undoList.add(current.diagram.clone());
                current.getDiagram().pasteObjects(copyList);
                current.save();
                current.repaint();
            }
        });
        itemDelete.addActionListener(e -> {
            DiagramPanel current;
            current = (DiagramPanel) diagramPane.getSelectedComponent();
            if (current != null) {
                current.undoList.add(current.diagram.clone());
                current.getDiagram().deleteSelected();
                current.save();
                current.repaint();
            }
        });
        itemExport.addActionListener(e -> {
            String filename;
            final JFileChooser fcoutput = new JFileChooser();
            fcoutput.setDialogTitle("Select output png file");
            fcoutput.setDialogType(JFileChooser.SAVE_DIALOG);
            int returnVal = fcoutput.showSaveDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                AmrPanel current;
                current = (AmrPanel) diagramPane.getSelectedComponent();
                filename = fcoutput.getSelectedFile().getAbsolutePath();
                BufferedImage image = new BufferedImage(current.getWidth(), current.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                Graphics2D g2 = image.createGraphics();
                g2.fillRect(0, 0, current.getWidth(), current.getHeight());
                current.paint(g2);
                try {
                    ImageIO.write(image, "png", new File(filename));
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
            final JFileChooser fcinput = new JFileChooser();
            fcinput.setDialogTitle("Select diagram file");
            fcinput.setDialogType(JFileChooser.OPEN_DIALOG);
            fcinput.setCurrentDirectory(new File(amrPath));
            int returnVal = fcinput.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                AmrPanel newPanel;
                newPanel = new AmrPanel(fcinput.getSelectedFile().getParent(), fcinput.getSelectedFile().getName());
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setViewportView(newPanel);
                diagramPane.add(scrollPane, fcinput.getSelectedFile().getName(), diagramPane.getSelectedIndex() + 1);
                getAnnotatedSentence(newPanel);
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
        EnumCommand lastCommand;
        current = (DiagramPanel) ((JScrollPane) diagramPane.getSelectedComponent()).getViewport().getView();
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
                        nextAmr(current, 1);
                    } else {
                        if (BACKWARD.equals(cmd)){
                            previousAmr(current,1);
                        } else {
                            if (FASTFORWARD.equals(cmd)){
                                nextAmr(current, 10);
                            } else {
                                if (FASTBACKWARD.equals(cmd)){
                                    previousAmr(current,10);
                                } else {
                                    if (FASTFASTFORWARD.equals(cmd)){
                                        nextAmr(current, 100);
                                    } else {
                                        if (FASTFASTBACKWARD.equals(cmd)){
                                            previousAmr(current,100);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private String getNextAnnotatedSentence(DiagramPanel current, int count){
        String fileName;
        if (subFolder.equals("true")){
            fileName = phrasePath + "/" + current.getFolder() + "/" + current.getNextFileName(count);
        } else {
            fileName = phrasePath + "/" + current.getNextFileName(count);
        }
        if (new File(fileName).exists()){
            return fileName;
        } else {
            return null;
        }
    }

    private String getPreviousAnnotatedSentence(DiagramPanel current, int count){
        String fileName;
        if (subFolder.equals("true")){
            fileName = phrasePath + "/" + current.getFolder() + "/" + current.getPreviousFileName(count);
        } else {
            fileName = phrasePath + "/" + current.getPreviousFileName(count);
        }
        if (new File(fileName).exists()){
            return fileName;
        } else {
            return null;
        }
    }

    private void nextAmr(DiagramPanel current, int count){
        if (current.diagram.nextAmrExists(count)){
            current.nextAmr(count);
            diagramPane.setTitleAt(diagramPane.getSelectedIndex(), current.getFileName());
            getAnnotatedSentence(current);
            repaint();
        }
    }

    private void previousAmr(DiagramPanel current, int count){
        if (current.diagram.previousAmrExists(count)){
            current.previousAmr(count);
            diagramPane.setTitleAt(diagramPane.getSelectedIndex(), current.getFileName());
            getAnnotatedSentence(current);
            repaint();
        }
    }

}
