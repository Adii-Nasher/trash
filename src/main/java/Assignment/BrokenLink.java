package Assignment;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import resources.Base;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class BrokenLink {
    public WebDriver driver;

    public Base base;

    public BrokenLink(WebDriver driver, Base base) {
        this.driver = driver;
        this.base = base;
        PageFactory.initElements(driver, this);
    }


    public void checkAllLinks() {
        try {
            String homePage = "https://www.knoldus.com/";
            List<String> links = getLinksOnPage(homePage);
            Map<String, String> linkStatuses = getLinkStatuses(links);
            writeLinksToWorkbook(links, linkStatuses);
        } catch (WebDriverException e) {
            takeScreenshotOnFailure("All Links");
        }
    }

    private List<String> getLinksOnPage(String homePage) {
        List<WebElement> links = driver.findElements(By.tagName("a"));
        List<String> validLinks = new ArrayList<>();
        for (WebElement link : links) {
            String url = link.getAttribute("href");
            if (url == null || url.isEmpty()) {
                continue;
            }
            if (url.startsWith(homePage)) {
                System.out.println(url + " is a valid link.");
                validLinks.add(url);
            } else {
                System.out.println(url + " is an invalid link.");
            }
        }
        return validLinks;
    }


    private Map<String, String> getLinkStatuses(List<String> links) {
        Map<String, String> linkStatuses = new HashMap<>();
        for (String link : links) {
            try {
                HttpURLConnection huc = (HttpURLConnection) (new URL(link).openConnection());
                huc.setRequestMethod("HEAD");
                huc.connect();
                int respCode = huc.getResponseCode();

                if (respCode >= 400 && respCode < 500) {
                    linkStatuses.put(link, "Client Error (" + respCode + ")");
                } else if (respCode >= 500 && respCode < 600) {
                    linkStatuses.put(link, "Server Error (" + respCode + ")");
                } else if (respCode == 200) {
                    linkStatuses.put(link, "Active");
                } else {
                    linkStatuses.put(link, "Unknown Status (" + respCode + ")");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                linkStatuses.put(link, "Malformed URL");
                takeScreenshotOnFailure("Link status");
            } catch (IOException e) {
                e.printStackTrace();
                linkStatuses.put(link, "IO Exception");
                takeScreenshotOnFailure("Link status");
            } catch (NoSuchWindowException e) {
                e.printStackTrace();
                takeScreenshotOnFailure("Link status");
            }
        }
        return linkStatuses;
    }


    private void writeLinksToWorkbook(List<String> links, Map<String, String> linkStatuses) {
        File file = new File("output.xlsx");
        XSSFWorkbook workbook;
        XSSFSheet sheet;
        int rowCount = 0;

        // If the file already exists, open it and get the current row count
        if (file.exists()) {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                workbook = new XSSFWorkbook(inputStream);
                sheet = workbook.getSheetAt(0);
                rowCount = sheet.getLastRowNum();
            } catch (IOException e) {
                e.printStackTrace();
                takeScreenshotOnFailure("Links to Workbook");
                return;
            }
        } else {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Links");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Link");
            headerRow.createCell(1).setCellValue("Response Code");
            headerRow.createCell(2).setCellValue("Status");
            rowCount = 1;
        }

        // Append new data to the end of the sheet
        for (String link : links) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(link);
            row.createCell(1).setCellValue(getResponseCode(link));
            row.createCell(2).setCellValue(linkStatuses.get(link));
        }

        // Write the updated data to the file
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            takeScreenshotOnFailure("Links to Workbook");
        }
    }



    private int getResponseCode(String link) {
        try {
            HttpURLConnection huc = (HttpURLConnection) (new URL(link).openConnection());
            huc.setRequestMethod("HEAD");
            huc.connect();
            return huc.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            takeScreenshotOnFailure("Response code");
            return -1;
        }
    }




    public void takeScreenshotOnFailure(String methodName) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String fileName = String.format("%s_%d.png", methodName, System.currentTimeMillis());
            String filePath = String.format("%s/screenshots/%s", System.getProperty("user.dir"), fileName);
            FileUtils.copyFile(screenshot, new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }