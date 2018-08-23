package lightner.sadeqzadeh.lightner.rest;

import com.google.gson.annotations.SerializedName;

public class PackageFlashcard {
    @SerializedName("ID")
    private int id;
    @SerializedName("PackageId")
    private int packageId;
    @SerializedName("Question")
    private String question;
    @SerializedName("Answer")
    private String answer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
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
}
