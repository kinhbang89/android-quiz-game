package fi.metropolia.translatorskeleton.model;

/**
 * Created by petrive on 23.3.16.
 */
/**
 *
 * @author peterh
 */

// one quiz item
public class QuizItem {
    private final String question;
    private boolean solved;

    public QuizItem(String question) {
        this.question = question;
        this.solved = false;
    }

    /**
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @return the solved
     */
    public boolean isSolved() {
        return solved;
    }

    /**
     * @param solved the solved to set
     */
    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}

