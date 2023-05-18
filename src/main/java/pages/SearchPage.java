package pages;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class SearchPage {

    @FindBy(id = "twotabsearchtextbox")
    public WebElement searchBox;
    @FindBy(id = "nav-search-submit-button")
    public WebElement searchButton;
    @FindBy(id = "nav-logo-sprites")
    public WebElement logo;
    @FindBy(css = "a[class='a-link-normal s-underline-text s-underline-link-text s-link-style a-text-normal']")
    public WebElement firstSearchResult;
    @FindBy(css = "input[title='Buy Now']")
    public WebElement byNowButton;
    @FindBy(css = "div[id='anonCarousel2']")
    public WebElement descriptionOfProduct;
    @FindBy(css = "div[id='anonCarousel3']")
    public WebElement offer;
    WebDriver driver;
    Random random;
    WebDriverWait wait;
    Logger log;
    File source;
    By ratingSelector = By.cssSelector("span[id='acrPopover']");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        random = new Random();
        wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        log = Logger.getLogger(getClass().getName());
        this.source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    }

    public boolean verifyNewPageLoading(String preUrl) {
        wait.until(ExpectedConditions.visibilityOf(logo));
        return !preUrl.equals(driver.getCurrentUrl());
    }

   public boolean searchByProductID() {
       wait.until(ExpectedConditions.visibilityOf(searchBox));
       String preUrl = driver.getCurrentUrl();
       searchBox.click();
       String productId = System.getenv("searchProductID");
       searchBox.sendKeys(productId);
       searchButton.click();
       return (verifyNewPageLoading(preUrl));
   }
    
    public boolean openProductPage() {
        wait.until(ExpectedConditions.visibilityOf(firstSearchResult));
        String preUrl = driver.getCurrentUrl();

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstSearchResult);
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        return (verifyNewPageLoading(preUrl));
    }

    public boolean verifyByNowButton() throws IOException {
        try {
            wait.until(ExpectedConditions.visibilityOf(byNowButton));
            log.info("ByNow button is visible");
            return true;
        } catch (NoSuchElementException e) {
            log.info("ByNow button is Not visible for this product");
            FileUtils.copyFile(source, new File("screenshot(NoRatingOption).png"));
            return false;
        }
    }

    public boolean verifyRating() throws IOException {
        try {
            WebElement rating = driver.findElements(ratingSelector).get(0);
            String ratingTitle = rating.getAttribute("title").trim();
            log.info(ratingTitle);
            float ratingResult = Float.parseFloat(ratingTitle.substring(0, 3));
            if (ratingResult >= 4.0) {
                log.info("Rating is :" + ratingResult);
            } else {
                log.info("Rating is below 4");
            }
            return true;
        } catch (NoSuchElementException e) {
            log.info("No Rating option is found for this Item");
            FileUtils.copyFile(source, new File("screenshot(NoRatingOption).png"));
            return false;
        }
    }

    public boolean printDescription() throws IOException {
        try {
            wait.until(ExpectedConditions.visibilityOf(descriptionOfProduct));
            log.info("offers is : " + descriptionOfProduct.getText());
            return true;
        } catch (TimeoutException e) {
            try {
                wait.until(ExpectedConditions.visibilityOf(offer));
                log.info("offers is : " + offer.getText());
                return true;
            } catch (TimeoutException e1) {
                log.info("No offer for this product");
                FileUtils.copyFile(source, new File("screenshot(NoOffer).png"));
                return false;
            }
        }
    }
}
