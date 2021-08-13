package com.microsoft.azure.javaee;

import com.microsoft.playwright.*;

/** Hello world! */
public class FetchBuildVersion {
  public static void main(String[] args) {
    try (Playwright playwright = Playwright.create()) {
      BrowserType webkit = playwright.webkit();
      Browser browser = webkit.launch(new BrowserType.LaunchOptions().setHeadless(true));
      Page page = browser.newPage();
      page.navigate(args[0]);

      ElementHandle p = page.querySelector("xpath=/html/body/div[1]/div/div[2]/p");

      if (!p.innerText().equals(args[1])) {
        throw new RuntimeException("Build Version is not updated!");
      }
    }
  }
}
