package com.dictionary.dal;

import java.sql.SQLException;
import java.util.List;

import com.dictionary.dto.WordDTO;

public interface IWordDAO {
    int create(WordDTO wordDTO) throws SQLException;
    WordDTO read(int id) throws SQLException;
    List<WordDTO> readByRoot(int rootId) throws SQLException;
    List<WordDTO> searchByBaseForm(String baseForm) throws SQLException;
    boolean update(WordDTO wordDTO) throws SQLException;
    boolean delete(int id) throws SQLException;
    List<WordDTO> getAll() throws SQLException;
    
    void closeConnection() throws SQLException;
}