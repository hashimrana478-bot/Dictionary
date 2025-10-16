package com.dictionary.bll;



import com.dictionary.dal.IWordDAO;
import com.dictionary.dto.WordDTO;
import java.sql.SQLException;
import java.util.List;

public interface IWordBO {
    int create(WordDTO wordDTO) throws SQLException, IllegalArgumentException;
    WordDTO read(int id) throws SQLException, IllegalArgumentException;
    List<WordDTO> readByRoot(int rootId) throws SQLException, IllegalArgumentException;
    List<WordDTO> searchByBaseForm(String baseForm) throws SQLException;
    boolean update(WordDTO wordDTO) throws SQLException, IllegalArgumentException;
    boolean delete(int id) throws SQLException, IllegalArgumentException;
    List<WordDTO> getAll() throws SQLException;
}
