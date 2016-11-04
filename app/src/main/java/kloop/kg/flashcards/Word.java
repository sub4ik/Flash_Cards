package kloop.kg.flashcards;

public class Word {
    private String id;
    private String word;
    private String translate;

    public String getId (){
        return id;
    }
    public String getWord(){
        return word;
    }
    public String getTranslate(){
        return translate;
    }
    public void setId(String id){
        this.id = id;
    }
    public void setWord(String word){
        this.word = word;
    }
    public void  setTranslate(String translate){
        this.translate = translate;
    }
}
