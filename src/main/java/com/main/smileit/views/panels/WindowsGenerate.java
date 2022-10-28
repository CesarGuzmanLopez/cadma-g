package com.main.smileit.views.panels;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import com.main.smileit.domain.generator.WriteAndGenerate;
import com.main.smileit.domain.models.Molecule;
import com.main.smileit.domain.models.MoleculesList;
import com.main.smileit.interfaces.CompleteEventInterface;
import com.main.smileit.interfaces.MoleculeListInterface;

import java.awt.event.MouseListener;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.Deque;
import java.util.LinkedList;
import java.awt.GridBagConstraints;

@SuppressWarnings("java:S1948")
public class WindowsGenerate extends JFrame {

    private JSpinner rSubstitutes;
    private Molecule principal;
    private MoleculesList moleculeList;
    private JFileChooser savePath;
    private String path, parentPath;
    private JLabel selectedFolder;
    private JTextField nameField;
    private MoleculeListInterface allMoleculesGenerated;
    private Deque<CompleteEventInterface> completeEvents;
    public WindowsGenerate(final Molecule principal, final MoleculesList moleculeList) {
        setTitle("Generate");
        setSize(550, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        initialize();
        this.principal = principal;
        this.moleculeList = moleculeList;

        revalidate();
        repaint();
        setVisible(true);
        completeEvents = new LinkedList<>();
        path = System.getProperty("user.dir");
        repaint();

    }
    public void addCompleteEvent(final CompleteEventInterface completeEvent) {
        completeEvents.add(completeEvent);
    }
    /** Genera View Principal generate. */
    boolean generate() {

        principal.setName(nameField.getText());
        if(savePath == null) {
            JOptionPane.showMessageDialog(null, "Please select a folder to save the files");
            return false;
        }
        File saveFileListDescriptive = new File(savePath.getSelectedFile().getAbsolutePath(),
                principal.getName() +  System.getProperty("file.separator") +"info.txt");
        File saveFileListSmile = new File(savePath.getSelectedFile().getAbsolutePath(),
                principal.getName() + System.getProperty("file.separator") + "output.txt");
        parentPath =saveFileListSmile.getParent();
        try {
            WriteAndGenerate.verifyEntry(principal, moleculeList, (int) rSubstitutes.getValue(),
                    saveFileListDescriptive, saveFileListSmile);
            final WriteAndGenerate generator = new WriteAndGenerate(moleculeList, principal,
                    (int) rSubstitutes.getValue(), 1, saveFileListDescriptive, saveFileListSmile);

            File directory = new File(savePath.getSelectedFile().getAbsolutePath(),
                    principal.getName() + System.getProperty("file.separator") + "Structures-png");

            if (!directory.exists()) {
                directory.mkdir();
            }
            generator.setSaveImages(directory.getAbsolutePath());
            allMoleculesGenerated =generator.generate();
        } catch (Exception e) { // NOSONAR
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        dispose();
        JOptionPane.showMessageDialog(this, "Generate", "Generate", JOptionPane.INFORMATION_MESSAGE);
        for (CompleteEventInterface completeGenerated : completeEvents) {
            completeGenerated.action();
        }
        return true;
    }
    public MoleculeListInterface getAllMoleculesGenerated() {
        if (allMoleculesGenerated == null) {
            throw new IllegalStateException("Error, The molecules have not been generated");
        }
        return allMoleculesGenerated;
    }
    private void initialize() {
        final GridBagConstraints gbc = new GridBagConstraints();

        final JLabel nameLabel = new JLabel("Name ");
        nameLabel.setPreferredSize(new java.awt.Dimension(210, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);
        nameField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(nameField, gbc);

        final JLabel labelSmileName = new JLabel("Select path to save files:  ");
        labelSmileName.setPreferredSize(new java.awt.Dimension(210, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(labelSmileName, gbc);
        final JButton selectSaveFolder = new JButton("Select path");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        selectSaveFolder.addActionListener(e -> selectSave());
        add(selectSaveFolder, gbc);
        selectedFolder = new JLabel("");
        gbc.gridx = 2;
        gbc.gridy = 1;
        add(selectedFolder, gbc);
        final JLabel labelSmileDesc = new JLabel("r-substitutions: ");
        labelSmileDesc.setPreferredSize(new java.awt.Dimension(210, 30));
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(labelSmileDesc, gbc);
        final JLabel labelQuestionSubs = new JLabel("<html><b style=\"color:green; padding:5px;\">?</b></html>");

        labelQuestionSubs.setPreferredSize(new java.awt.Dimension(50, 30));
        gbc.gridx = 2;
        gbc.gridy = 3;
        add(labelQuestionSubs, gbc);

        rSubstitutes = new JSpinner();
        rSubstitutes.setValue(1);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(rSubstitutes, gbc);

        final JButton cancelButton = new JButton("Cancel");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton, gbc);
        final JButton generateButton = new JButton("Generate structures!");
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        generateButton.addActionListener(e -> generate());
        add(generateButton, gbc);
        final JLabel labelQuestion = new JLabel("<html><b style=\"color:green; padding:5px;\"></b></html>");
        labelQuestion.setPreferredSize(new java.awt.Dimension(50, 30));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(labelQuestion, gbc);

        labelQuestionSubs.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(final java.awt.event.MouseEvent e) {
              // this method is empty
            }

            @Override
            public void mousePressed(final java.awt.event.MouseEvent e) {
              // this method is empty
            }

            @Override
            public void mouseReleased(final java.awt.event.MouseEvent e) {
              // this method is empty
            }

            @Override
            public void mouseEntered(final java.awt.event.MouseEvent e) {
                labelQuestion.setText("<html><b style=\"color:green; padding:5px;\">How many simultaneous"
                        + " substitutions are will be allowed?</b></html>");
            }

            @Override
            public void mouseExited(final java.awt.event.MouseEvent e) {
                labelQuestion.setText("<html><b style=\"color:green; padding:5px;\"></b></html>");

            }

        });
    }

    private void selectSave() {
        savePath = new JFileChooser();
        savePath.setCurrentDirectory(new File(path));
        savePath.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int outPut = savePath.showSaveDialog(this);
        savePath.setAcceptAllFileFilterUsed(false);
        path = savePath.getCurrentDirectory().getPath();
        if (outPut == JFileChooser.APPROVE_OPTION) {
            selectedFolder.setText("Selected");
        } else {
            savePath = null;
            selectedFolder.setText("");
        }
    }
    public String getParentPath(){
        return parentPath;
    }

}
