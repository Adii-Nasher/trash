package pages;

import io.cucumber.java.eo.Se;
import org.openqa.selenium.WebDriver;

public class PageObjectManager {
    WebDriver driver;


    public PageObjectManager(WebDriver driver){
        this.driver=driver;
    }



  public SearchPage getSearchPage(){
        return new SearchPage(driver);
  }

}
