package trivia;

import java.util.Stack;

/**
 *
 */
public class Category {

    public enum CategoryName {
        POP("Pop"),
        SCIENCE("Science"),
        SPORTS("Sports"),
        ROCK("Rock");

        public String desc;

        CategoryName(String desc){
            this.desc = desc;
        }
    }

    public final CategoryName name;
    private final Stack<String> questionStack;

    public Category( CategoryName name ) {
        this.name = name;
        this.questionStack = new Stack<>();
    }

    public void addQuestion(int index){
        questionStack.add(  name.desc + " Question " + index );
    }

    public String getNextQuestion(){
        return questionStack.pop();
    }
}
