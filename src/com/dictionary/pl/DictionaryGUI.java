package com.dictionary.pl;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.dictionary.dal.DBConnectionManager;
import com.dictionary.dal.IWordDAO;
import com.dictionary.dal.WordDAO;
import com.dictionary.dto.WordDTO;

public class DictionaryGUI extends JFrame {
    private IWordDAO wordDAO;
    private JTextField idField, arabicFormField, baseFormField, urduMeaningField, partOfSpeechField, rootIdField;
    private JTextField searchBaseFormField, searchRootIdField;
    private JTable wordTable;
    private DefaultTableModel tableModel;

    public DictionaryGUI() {
        // Initialize DAO and DB
        try {
            DBConnectionManager.getInstance();  // Triggers auto init
            wordDAO = new WordDAO();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        setTitle("Arabic-Urdu Dictionary System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Form Panel (North: Input fields for CRUD)
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Word Entry"));

        formPanel.add(new JLabel("ID (for Update/Delete):"));
        idField = new JTextField();
        formPanel.add(idField);

        formPanel.add(new JLabel("Arabic Form:"));
        arabicFormField = new JTextField();
        formPanel.add(arabicFormField);

        formPanel.add(new JLabel("Base Form:"));
        baseFormField = new JTextField();
        formPanel.add(baseFormField);

        formPanel.add(new JLabel("Urdu Meaning:"));
        urduMeaningField = new JTextField();
        formPanel.add(urduMeaningField);

        formPanel.add(new JLabel("Part of Speech:"));
        partOfSpeechField = new JTextField();
        formPanel.add(partOfSpeechField);

        formPanel.add(new JLabel("Root ID:"));
        rootIdField = new JTextField();
        formPanel.add(rootIdField);

        // Buttons for CRUD
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton createButton = new JButton("Create");
        JButton readButton = new JButton("Read by ID");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear Form");

        buttonPanel.add(createButton);
        buttonPanel.add(readButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        formPanel.add(buttonPanel);  // Span buttons across

        add(formPanel, BorderLayout.NORTH);

        // Table for displaying results (Center)
        tableModel = new DefaultTableModel(new Object[]{"ID", "Arabic Form", "Base Form", "Urdu Meaning", "Part of Speech", "Root ID"}, 0);
        wordTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(wordTable);
        add(scrollPane, BorderLayout.CENTER);

        // Search Panel (South)
        JPanel searchPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search & Browse"));

        searchPanel.add(new JLabel("Search by Base Form:"));
        searchBaseFormField = new JTextField();
        searchPanel.add(searchBaseFormField);
        JButton searchBaseButton = new JButton("Search Base");
        searchPanel.add(searchBaseButton);

        searchPanel.add(new JLabel("Browse by Root ID:"));
        searchRootIdField = new JTextField();
        searchPanel.add(searchRootIdField);
        JButton searchRootButton = new JButton("Browse Root");
        searchPanel.add(searchRootButton);

        JButton refreshButton = new JButton("Refresh All");
        searchPanel.add(refreshButton);

        add(searchPanel, BorderLayout.SOUTH);

        // Action Listeners
        createButton.addActionListener(new CRUDActionListener("create"));
        readButton.addActionListener(new CRUDActionListener("read"));
        updateButton.addActionListener(new CRUDActionListener("update"));
        deleteButton.addActionListener(new CRUDActionListener("delete"));
        clearButton.addActionListener(e -> clearForm());

        searchBaseButton.addActionListener(new SearchActionListener("base"));
        searchRootButton.addActionListener(new SearchActionListener("root"));
        refreshButton.addActionListener(e -> loadAllWords());

        // Load dummy data or all words on start
        loadDummyDataAndRefresh();  // Or just loadAllWords();
    }

    private void clearForm() {
        idField.setText("");
        arabicFormField.setText("");
        baseFormField.setText("");
        urduMeaningField.setText("");
        partOfSpeechField.setText("");
        rootIdField.setText("");
    }

    private WordDTO getFormData() {
        WordDTO dto = new WordDTO();
        if (!idField.getText().isEmpty()) dto.setId(Integer.parseInt(idField.getText()));
        dto.setArabicForm(arabicFormField.getText());
        dto.setBaseForm(baseFormField.getText());
        dto.setUrduMeaning(urduMeaningField.getText());
        dto.setPartOfSpeech(partOfSpeechField.getText());
        if (!rootIdField.getText().isEmpty()) dto.setRootId(Integer.parseInt(rootIdField.getText()));
        return dto;
    }

    private void setFormData(WordDTO dto) {
        idField.setText(String.valueOf(dto.getId()));
        arabicFormField.setText(dto.getArabicForm());
        baseFormField.setText(dto.getBaseForm());
        urduMeaningField.setText(dto.getUrduMeaning());
        partOfSpeechField.setText(dto.getPartOfSpeech());
        rootIdField.setText(String.valueOf(dto.getRootId()));
    }

    private void refreshTable(List<WordDTO> words) {
        tableModel.setRowCount(0);  // Clear table
        for (WordDTO dto : words) {
            tableModel.addRow(new Object[]{
                dto.getId(),
                dto.getArabicForm(),
                dto.getBaseForm(),
                dto.getUrduMeaning(),
                dto.getPartOfSpeech(),
                dto.getRootId()
            });
        }
    }

    private void loadAllWords() {
        try {
            // Simulate fetching all (add a method to IWordDAO/WordDAO if needed, e.g., List<WordDTO> getAll()
            // For now, assume empty or search with empty base
            List<WordDTO> allWords = wordDAO.searchByBaseForm("");  // Hack: LIKE '%%' to get all
            refreshTable(allWords);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load words: " + e.getMessage());
        }
    }

    private void loadDummyDataAndRefresh() {
        // Reuse logic from Main to insert dummy via temp method or direct calls
        // For simplicity, run a subset here (assume roots inserted manually or extend DAO)
        JOptionPane.showMessageDialog(this, "Note: Run com.dictionary.main.Main first to insert dummy roots/words, or implement IRootDAO.");
        loadAllWords();
    }

    // Inner class for CRUD actions
    private class CRUDActionListener implements ActionListener {
        private String action;

        public CRUDActionListener(String action) {
            this.action = action;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                WordDTO dto = getFormData();
                boolean success = false;
                String msg = "";
                switch (action) {
                    case "create":
                        if (dto.getRootId() == 0) throw new IllegalArgumentException("Root ID required");
                        int newId = wordDAO.create(dto);
                        msg = "Created with ID: " + newId;
                        success = true;
                        break;
                    case "read":
                        if (dto.getId() == 0) throw new IllegalArgumentException("ID required");
                        WordDTO readDto = wordDAO.read(dto.getId());
                        if (readDto != null) {
                            setFormData(readDto);
                            refreshTable(List.of(readDto));
                            msg = "Read successful";
                        } else {
                            msg = "Word not found";
                        }
                        success = true;
                        break;
                    case "update":
                        if (dto.getId() == 0) throw new IllegalArgumentException("ID required");
                        success = wordDAO.update(dto);
                        msg = success ? "Updated" : "Update failed";
                        break;
                    case "delete":
                        if (dto.getId() == 0) throw new IllegalArgumentException("ID required");
                        success = wordDAO.delete(dto.getId());
                        msg = success ? "Deleted" : "Delete failed";
                        break;
                }
                JOptionPane.showMessageDialog(DictionaryGUI.this, msg);
                if (success) {
                    clearForm();
                    loadAllWords();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(DictionaryGUI.this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(DictionaryGUI.this, "Input Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Inner class for Search actions
    private class SearchActionListener implements ActionListener {
        private String type;

        public SearchActionListener(String type) {
            this.type = type;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                List<WordDTO> results;
                if (type.equals("base")) {
                    String base = searchBaseFormField.getText();
                    results = wordDAO.searchByBaseForm(base);
                } else {
                    int rootId = Integer.parseInt(searchRootIdField.getText());
                    results = wordDAO.readByRoot(rootId);
                }
                refreshTable(results);
                JOptionPane.showMessageDialog(DictionaryGUI.this, "Found " + results.size() + " results");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(DictionaryGUI.this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(DictionaryGUI.this, "Invalid Root ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Main to launch GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DictionaryGUI().setVisible(true);
        });
    }

    // Override close to cleanup DAO connection
    @Override
    public void dispose() {
        try {
            if (wordDAO != null) {
                wordDAO.closeConnection();
            }
        } catch (SQLException e) {
            // Log or ignore
        }
        super.dispose();
    }
}
