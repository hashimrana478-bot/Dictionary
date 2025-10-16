package com.dictionary.dal;

import com.dictionary.dto.WordDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WordDAO implements IWordDAO {

    private Connection conn;  // Connection member

    // Constructor: Initialize connection from Singleton
    public WordDAO() throws SQLException {
        this.conn = DBConnectionManager.getInstance().getConnection();
    }

    // CREATE
    @Override
    public int create(WordDTO wordDTO) throws SQLException {
        String sql = "INSERT INTO Word (arabic_form, base_form, urdu_meaning, part_of_speech, root_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, wordDTO.getArabicForm());
            pstmt.setString(2, wordDTO.getBaseForm());
            pstmt.setString(3, wordDTO.getUrduMeaning());
            pstmt.setString(4, wordDTO.getPartOfSpeech());
            pstmt.setInt(5, wordDTO.getRootId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            return -1;
        }
    }

    // READ by ID
    @Override
    public WordDTO read(int id) throws SQLException {
        String sql = "SELECT * FROM Word WHERE ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    WordDTO wordDTO = new WordDTO();
                    wordDTO.setId(rs.getInt("ID"));
                    wordDTO.setArabicForm(rs.getString("arabic_form"));
                    wordDTO.setBaseForm(rs.getString("base_form"));
                    wordDTO.setUrduMeaning(rs.getString("urdu_meaning"));
                    wordDTO.setPartOfSpeech(rs.getString("part_of_speech"));
                    wordDTO.setRootId(rs.getInt("root_id"));
                    return wordDTO;
                }
                return null;
            }
        }
    }

    // READ by root_id
    @Override
    public List<WordDTO> readByRoot(int rootId) throws SQLException {
        List<WordDTO> wordDTOs = new ArrayList<>();
        String sql = "SELECT * FROM Word WHERE root_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, rootId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    WordDTO wordDTO = new WordDTO();
                    wordDTO.setId(rs.getInt("ID"));
                    wordDTO.setArabicForm(rs.getString("arabic_form"));
                    wordDTO.setBaseForm(rs.getString("base_form"));
                    wordDTO.setUrduMeaning(rs.getString("urdu_meaning"));
                    wordDTO.setPartOfSpeech(rs.getString("part_of_speech"));
                    wordDTO.setRootId(rs.getInt("root_id"));
                    wordDTOs.add(wordDTO);
                }
            }
            return wordDTOs;
        }
    }

    // SEARCH by base_form
    @Override
    public List<WordDTO> searchByBaseForm(String baseForm) throws SQLException {
        List<WordDTO> wordDTOs = new ArrayList<>();
        String sql = "SELECT * FROM Word WHERE base_form LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + baseForm + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    WordDTO wordDTO = new WordDTO();
                    wordDTO.setId(rs.getInt("ID"));
                    wordDTO.setArabicForm(rs.getString("arabic_form"));
                    wordDTO.setBaseForm(rs.getString("base_form"));
                    wordDTO.setUrduMeaning(rs.getString("urdu_meaning"));
                    wordDTO.setPartOfSpeech(rs.getString("part_of_speech"));
                    wordDTO.setRootId(rs.getInt("root_id"));
                    wordDTOs.add(wordDTO);
                }
            }
            return wordDTOs;
        }
    }

    // UPDATE
    @Override
    public boolean update(WordDTO wordDTO) throws SQLException {
        String sql = "UPDATE Word SET arabic_form = ?, base_form = ?, urdu_meaning = ?, part_of_speech = ?, root_id = ? WHERE ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, wordDTO.getArabicForm());
            pstmt.setString(2, wordDTO.getBaseForm());
            pstmt.setString(3, wordDTO.getUrduMeaning());
            pstmt.setString(4, wordDTO.getPartOfSpeech());
            pstmt.setInt(5, wordDTO.getRootId());
            pstmt.setInt(6, wordDTO.getId());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    // DELETE
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM Word WHERE ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Close the member connection
    @Override
    public void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
    
    @Override
    public List<WordDTO> getAll() throws SQLException {
        List<WordDTO> words = new ArrayList<>();
        String sql = "SELECT * FROM Word ORDER BY ID";  // Optional: order by ID for consistency
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                WordDTO wordDTO = new WordDTO();
                wordDTO.setId(rs.getInt("ID"));
                wordDTO.setArabicForm(rs.getString("arabic_form"));
                wordDTO.setBaseForm(rs.getString("base_form"));
                wordDTO.setUrduMeaning(rs.getString("urdu_meaning"));
                wordDTO.setPartOfSpeech(rs.getString("part_of_speech"));
                wordDTO.setRootId(rs.getInt("root_id"));
                words.add(wordDTO);
            }
        } catch (SQLException e) {
            // Log or wrap in custom exception if needed
            throw e;  // Rethrow to be handled by BLL/GUI
        }
        return words;
    }
}