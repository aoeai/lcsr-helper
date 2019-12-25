import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class MarkdownHelper {

    private static List<Second> recodes = new ArrayList<>();

    public static void main(String[] args) {
        String allUrl = "https://leetcode-cn.com/problems/search-in-rotated-sorted-array/\n" +
                "https://leetcode-cn.com/problems/search-a-2d-matrix/\n" +
                "https://leetcode-cn.com/problems/find-minimum-in-rotated-sorted-array/";
        String tag = "二分查找";

        for (String url : allUrl.split("\n")) {
            First first = doFirst(url);;
            Second second = doSecond(first);
            recodes.add(second);
        }

        for (Second second : recodes) {
            print(second, tag);
        }
    }

    private static First doFirst(String url){
        String chromedriverPath = System.getProperty("user.dir") + "/src/main/resources/driver-mac/chromedriver";
        // Optional, if not specified, WebDriver will search your path for chromedriver.
        System.setProperty("webdriver.chrome.driver", chromedriverPath);

        // Create a new instance of the Firefox driver
        // Notice that the remainder of the code relies on the interface,
        // not the implementation.
        WebDriver driver = new ChromeDriver();
        // 全屏ChromeDriver
//        driver.manage().window().maximize();

        // And now use this to visit Google
        driver.get(url);

        new WebDriverWait(driver, 60, 200)
                .until(ExpectedConditions.elementToBeClickable(By.ByClassName.className("css-3mrlhg-Title")));

        List<WebElement> list = ((ChromeDriver) driver).findElementsByClassName("css-3mrlhg-Title");
        WebElement div = list.get(0);
        String a = div.getAttribute("innerHTML");

        List<WebElement> difList = ((ChromeDriver) driver).findElementsByClassName("css-14oznsj-Difficulty");
        String dif = difList.get(1).getText();

        First first = new First(a, dif);
        driver.close();
        return first;
    }

    private static Second doSecond(First first){
        Document doc = Jsoup.parse(first.a);
        Element a = doc.getElementsByTag("a").first();
        String href = a.attr("href");
        String value = doc.body().text().trim();

        String num = value.substring(0, value.indexOf("."));
        String name = value.substring(value.indexOf(".") + 1).trim();

        Second second = new Second(getdifficulty(first.difficulty), num, name, href);
        return second;
    }

    /**
     * 打印结果
     * @param second
     * @param tag 问题标签
     */
    private static void print(Second second, String tag) {
        // | 51 | [N皇后](https://leetcode-cn.com/problems/n-queens/)| 困难 | 分治、回溯 | - |
        String temp = "| [%s](%s) | [%s](%s)| %s | %s | - |";
        String mostVotesUrl =
                String.format("https://leetcode.com%sdiscuss/?currentPage=1&orderBy=most_votes&query=", second.href);
        String cnUrl = String.format("https://leetcode-cn.com%s", second.href);

        String result = String.format(temp, second.num, mostVotesUrl,
                second.name, cnUrl, second.difficulty, tag);

        System.out.println(result);
    }

    private static String getdifficulty(String diff) {
        String icon = "";
        switch (diff) {
            case "简单":
                icon = "\uD83D\uDFE2 ";
                break;
            case "中等":
                icon = "\uD83D\uDFE1 ";
                break;
            case "困难":
                icon = "\uD83D\uDD34️ ";
                break;
        }

        return icon + diff;
    }

    /**
     * 第一次的结果
     */
    private static class First{
        String a; // a标签
        String difficulty;

        public First(String a, String difficulty) {
            this.a = a;
            this.difficulty = difficulty;
        }
    }

    private static class Second{
        /**
         * 题目难度
         */
        String difficulty;

        /**
         * 题号
         */
        String num;

        /**
         * 题目名称
         */
        String name;

        /**
         * 题目链接
         */
        String href;

        public Second(String difficulty, String num, String name, String href) {
            this.difficulty = difficulty;
            this.num = num;
            this.name = name;
            this.href = href;
        }

        @Override
        public String toString() {
            return "Second{" +
                    "difficulty='" + difficulty + '\'' +
                    ", num='" + num + '\'' +
                    ", name='" + name + '\'' +
                    ", href='" + href + '\'' +
                    '}';
        }
    }
}

