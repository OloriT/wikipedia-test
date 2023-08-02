import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class WikipediaLink {

    Set<String> totalLinks = new HashSet<>();

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "resources/chromedriver.exe");

        try {
            String wikipediaLink = Constant.LINK;
            int n = 3;

            Set<String> uniqueLinks = new HashSet<>();
            uniqueLinks.add(wikipediaLink);

            WebDriver driver = new ChromeDriver();

            //2. maximize the window
            driver.manage().window().maximize();

            for (int i = 1; i <= n; i++) {
                Set<String> newLinks = new HashSet<>();
                System.out.println("NewLink Size" + newLinks.size());
                for (String link : uniqueLinks) {
                    scrapeLinks(link, newLinks, uniqueLinks, driver);
                }
                uniqueLinks.addAll(newLinks);
            }


            driver.quit();

            // Print the results to the console
            System.out.println("Total Links Found: " + uniqueLinks.size());
            System.out.println("Unique Links Found: " + uniqueLinks.size());
            System.out.println("All Found Links:");
            for (String link : uniqueLinks) {
                System.out.println(link);
            }

            // Write the results to a CSV file (optional)
            writeLinksToCSV(uniqueLinks, "wiki_links.csv");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void scrapeLinks(String link, Set<String> newLinks, Set<String> uniqueLinks, WebDriver driver) throws IOException {
        driver.get(link);

        //wait for page to load
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(10000));

        WebElement bodyElement = driver.findElement(By.tagName("body"));
        List<WebElement> linkElements = bodyElement.findElements(By.cssSelector("a[href^='/wiki/']"));

//        for (WebElement element : linkElements) {
        for (int i = 0; i < 9; i++) {
//            String href = element.getAttribute("href");
            String href = linkElements.get(i).getAttribute("href");
            if (!uniqueLinks.contains(href)) {
                newLinks.add(href);
                new WikipediaLink().totalLinks.add(href);
            }
        }
    }

    public static void writeLinksToCSV(Set<String> uniqueLinks, String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.write("Links\n");
        for (String link : uniqueLinks) {
            writer.write(link + "\n");
        }
        writer.close();
        System.out.println("Results written to " + fileName);
    }
}
