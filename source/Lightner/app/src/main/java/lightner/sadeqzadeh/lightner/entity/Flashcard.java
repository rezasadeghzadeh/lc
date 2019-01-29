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
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int correctOption;

    public Flashcard() {
    }

    public Flashcard(Long id, String question, String questionUri, int currentBox, Date lastVisit, Date nextVisit, Long categoryId, String option1, String option2, String option3, String option4, int correctOption) {
        this.id = id;
        this.question = question;
        this.questionUri = questionUri;
        this.currentBox = currentBox;
        this.lastVisit = lastVisit;
        this.nextVisit = nextVisit;
        this.categoryId = categoryId;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctOption = correctOption;
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

    @Generated(hash = 8927802)
    public Flashcard(Long id, @NotNull String question, String questionUri, @NotNull String answer, String answerUri, int currentBox, Date lastVisit, Date nextVisit, Long categoryId, String option1, String option2,
            String option3, String option4, int correctOption) {
        this.id = id;
        this.question = question;
        this.questionUri = questionUri;
        this.answer = answer;
        this.answerUri = answerUri;
        this.currentBox = currentBox;
        this.lastVisit = lastVisit;
        this.nextVisit = nextVisit;
        this.categoryId = categoryId;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctOption = correctOption;
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

    public String getOption1() {
        return this.option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return this.option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return this.option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return this.option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public int getCorrectOption() {
        return this.correctOption;
    }

    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }
}
