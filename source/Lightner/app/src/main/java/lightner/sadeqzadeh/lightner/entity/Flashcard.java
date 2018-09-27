package lightner.sadeqzadeh.lightner.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

@Entity
public class Flashcard {
    @Id
    private Long id;
    @NotNull
    private String question;
    private String questionUri;
    @NotNull
    private String answer;
    private String answerUri;
    private int currentBox;
    private Date lastVisit;
    private Date nextVisit;
    private Long categoryId;

    public Flashcard() {
    }

    public Flashcard(String question, String questionUri, String answer, String answerUri, int currentBox, Date lastVisit, Date nextVisit, Long categoryId) {
        this.question = question;
        this.questionUri = questionUri;
        this.answer = answer;
        this.answerUri = answerUri;
        this.currentBox = currentBox;
        this.lastVisit = lastVisit;
        this.nextVisit = nextVisit;
        this.categoryId = categoryId;
    }

    @Generated(hash = 675945604)
    public Flashcard(Long id, @NotNull String question, String questionUri, @NotNull String answer, String answerUri, int currentBox, Date lastVisit,
            Date nextVisit, Long categoryId) {
        this.id = id;
        this.question = question;
        this.questionUri = questionUri;
        this.answer = answer;
        this.answerUri = answerUri;
        this.currentBox = currentBox;
        this.lastVisit = lastVisit;
        this.nextVisit = nextVisit;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getCurrentBox() {
        return currentBox;
    }

    public void setCurrentBox(int currentBox) {
        this.currentBox = currentBox;
    }

    public Date getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }

    public Date getNextVisit() {
        return nextVisit;
    }

    public void setNextVisit(Date nextVisit) {
        this.nextVisit = nextVisit;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getQuestionUri() {
        return questionUri;
    }

    public void setQuestionUri(String questionUri) {
        this.questionUri = questionUri;
    }

    public String getAnswerUri() {
        return answerUri;
    }

    public void setAnswerUri(String answerUri) {
        this.answerUri = answerUri;
    }
}
