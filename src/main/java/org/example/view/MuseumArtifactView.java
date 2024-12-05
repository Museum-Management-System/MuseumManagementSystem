package org.example.view;

import javax.swing.*;
import java.awt.*;

public class MuseumArtifactView extends JFrame {
    private JTable artifactTable;
    private JButton addButton, updateButton, deleteButton, searchButton;

    public MuseumArtifactView() {
        setTitle("Museum Management System - Employee");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        artifactTable = new JTable();
        add(new JScrollPane(artifactTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        addButton = new JButton("Add Artifact");
        updateButton = new JButton("Update Artifact");
        deleteButton = new JButton("Delete Artifact");
        searchButton = new JButton("Search Artifact");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JTable getArtifactTable() {
        return artifactTable;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getUpdateButton() {
        return updateButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getSearchButton() {
        return searchButton;
    }
}
