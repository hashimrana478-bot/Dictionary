package com.dictionary.dto;

public class WordDTO {
    private int id;
    private String arabicForm;
    private String baseForm;
    private String urduMeaning;
    private String partOfSpeech;
    private int rootId;

    // Constructors
    public WordDTO() {}
    
    public WordDTO(String arabicForm, String baseForm, String urduMeaning, String partOfSpeech, int rootId) {
        this.arabicForm = arabicForm;
        this.baseForm = baseForm;
        this.urduMeaning = urduMeaning;
        this.partOfSpeech = partOfSpeech;
        this.rootId = rootId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getArabicForm() { return arabicForm; }
    public void setArabicForm(String arabicForm) { this.arabicForm = arabicForm; }
    public String getBaseForm() { return baseForm; }
    public void setBaseForm(String baseForm) { this.baseForm = baseForm; }
    public String getUrduMeaning() { return urduMeaning; }
    public void setUrduMeaning(String urduMeaning) { this.urduMeaning = urduMeaning; }
    public String getPartOfSpeech() { return partOfSpeech; }
    public void setPartOfSpeech(String partOfSpeech) { this.partOfSpeech = partOfSpeech; }
    public int getRootId() { return rootId; }
    public void setRootId(int rootId) { this.rootId = rootId; }

    @Override
    public String toString() {
        return "WordDTO{id=" + id + ", arabicForm='" + arabicForm + "', urduMeaning='" + urduMeaning + "'}";
    }
}
