package com.dictionary.bll;


import java.sql.SQLException;
import java.util.List;

import com.dictionary.dal.IWordDAO;
import com.dictionary.dto.WordDTO;

public class WordBO implements IWordBO {
    private final IWordDAO wordDAO;

    public WordBO(IWordDAO wordDAO) {
        this.wordDAO = wordDAO;
    }

    @Override
    public int create(WordDTO wordDTO) throws SQLException, IllegalArgumentException {
        validateWordDTO(wordDTO, false);  // No ID check for create
        return wordDAO.create(wordDTO);
    }

    @Override
    public WordDTO read(int id) throws SQLException, IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid ID: must be positive integer");
        }
        WordDTO dto = wordDAO.read(id);
        if (dto == null) {
            throw new IllegalArgumentException("Word with ID " + id + " not found");
        }
        return dto;
    }

    @Override
    public List<WordDTO> readByRoot(int rootId) throws SQLException, IllegalArgumentException {
        if (rootId <= 0) {
            throw new IllegalArgumentException("Invalid Root ID: must be positive integer");
        }
        return wordDAO.readByRoot(rootId);
    }

    @Override
    public List<WordDTO> searchByBaseForm(String baseForm) throws SQLException {
        if (baseForm == null || baseForm.trim().isEmpty()) {
            return getAll();  // If empty search, return all
        }
        return wordDAO.searchByBaseForm(baseForm.trim());
    }

    @Override
    public boolean update(WordDTO wordDTO) throws SQLException, IllegalArgumentException {
        validateWordDTO(wordDTO, true);  // ID required for update
        return wordDAO.update(wordDTO);
    }

    @Override
    public boolean delete(int id) throws SQLException, IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid ID: must be positive integer");
        }
        return wordDAO.delete(id);
    }

    @Override
    public List<WordDTO> getAll() throws SQLException {
        return wordDAO.getAll();  // Use dedicated getAll() in DAO
    }

    private void validateWordDTO(WordDTO wordDTO, boolean requireId) {
        if (requireId && wordDTO.getId() <= 0) {
            throw new IllegalArgumentException("ID required and must be positive for this operation");
        }
        if (wordDTO.getArabicForm() == null || wordDTO.getArabicForm().trim().isEmpty()) {
            throw new IllegalArgumentException("Arabic Form is required");
        }
        if (wordDTO.getBaseForm() == null || wordDTO.getBaseForm().trim().isEmpty()) {
            throw new IllegalArgumentException("Base Form is required");
        }
        if (wordDTO.getUrduMeaning() == null || wordDTO.getUrduMeaning().trim().isEmpty()) {
            throw new IllegalArgumentException("Urdu Meaning is required");
        }
        if (wordDTO.getPartOfSpeech() == null || wordDTO.getPartOfSpeech().trim().isEmpty()) {
            throw new IllegalArgumentException("Part of Speech is required");
        }
        if (wordDTO.getRootId() <= 0) {
            throw new IllegalArgumentException("Valid Root ID is required");
        }
        // Additional business rules: e.g., check root existence via RootBO if added
    }
}