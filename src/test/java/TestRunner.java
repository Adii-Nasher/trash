import Assignment.BrokenLink;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import resources.Base;

import java.io.IOException;


public class TestRunner {
    private WebDriver driver;
    private Base base;
    private BrokenLink brokenLink;

    @BeforeTest
    public void setUp() throws IOException {
        base = new Base();
        driver = base.initializeDriver();
        brokenLink = new BrokenLink(driver, base);
    }

    @Test
    public void checkLink()  {
        brokenLink.checkAllLinks();
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}

