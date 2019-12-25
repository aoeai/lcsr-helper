public class LeetCode {

    /**
     * 题号
     */
    private String id;

    /**
     * 问题
     */
    private String problem;

    /**
     * 问题的英文版
     */
    private String problemEn;

    /**
     * 难度
     */
    private String difficulty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getProblemEn() {
        return problemEn;
    }

    public void setProblemEn(String problemEn) {
        this.problemEn = problemEn;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "LeetCode{" +
                "id='" + id + '\'' +
                ", problem='" + problem + '\'' +
                ", problemEn='" + problemEn + '\'' +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }
}
