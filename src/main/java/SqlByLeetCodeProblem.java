import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqlByLeetCodeProblem {

    public static void main(String[] args) throws IOException {
        String path = getRootPath() + "leetcode.html";

        List<LeetCode> records = getProblemList(path);
        generateSql(records);
    }

    private static List<LeetCode> getProblemList(String path) throws IOException {
        File file = new File(path);
        Document doc = Jsoup.parse(file, "UTF-8");
        Elements reactable = doc.getElementsByClass("reactable-data");
        Element table = reactable.get(0);
        // 使用选择器选择该table内所有的<tr> <tr/>
        Elements trs = table.select("tr");
        List<LeetCode> records = new ArrayList<>();
        for (Element tr : trs) {
            Elements tds = tr.select("td");
            // 题号
            Element td1 = tds.get(1);
            String id = td1.html();

            if (!NumberUtils.isDigits(id)) {
                System.out.println(id);
                continue;
            }

            // 标题、链接
            Element td2 = tds.get(2);
            Element link = td2.select("a").first();
            String problemEn = link.attr("href").replace("https://leetcode-cn.com/problems/", "");

            // 难度
            Element td5 = tds.get(5);
            Element span = td5.select("span").first();
            String difficulty = span.html();

            LeetCode leetCode = new LeetCode();
            leetCode.setId(id);
            leetCode.setProblem(link.html());
            leetCode.setProblemEn(problemEn);
            leetCode.setDifficulty(difficulty);
            records.add(leetCode);
        }

        return records;
    }

    private static void generateSql(List<LeetCode> records) {
        String sql = "INSERT INTO `lcsr_problem` (`id`, `problem`, `problem_en`, `difficulty`, `tag`) " +
                "SELECT '%s', '%s', '%s', '%s', '0' " +
                "WHERE (SELECT COUNT(*) FROM `lcsr_problem` WHERE id = %s)  < 1;\n";
        StringBuilder insertSql = new StringBuilder();
        for (LeetCode record : records) {
            insertSql.append(String.format(sql, record.getId(), record.getProblem(),
                    record.getProblemEn(), getDifficulty(record.getDifficulty()), record.getId()));
        }

        File file = new File(getRootPath() + "problem-insert.sql");
        System.out.println(file.getAbsolutePath());
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(insertSql.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getDifficulty(String difficulty) {
        switch (difficulty) {
            case "简单":
                return 1;
            case "中等":
                return 2;
        }
        return 3;
    }

    private static String getRootPath(){
        return System.getProperty("user.dir") + "/src/main/resources/";
    }
}
