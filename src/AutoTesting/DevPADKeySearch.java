package AutoTesting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import atu.testrecorder.ATUTestRecorder;
import atu.testrecorder.exceptions.ATUTestRecorderException;

public class DevPADKeySearch {
	@Test
	public void test_DevPADCrawler() throws IOException, InterruptedException, ATUTestRecorderException {
		// start up WebDrivers
		//ATUTestRecorder recorder = new ATUTestRecorder("C:\\Users\\masher\\git\\PAD-Pub-Selenium\\TestVideoRecordings", "Test1", false);

		//recorder.start();
		System.setProperty("webdriver.chrome.driver", "./build/ChromeDriver/chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("load-extension=C:\\Users\\malki\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Extensions\\gighmmpiobklfepjocnamgkkbiglidom\\3.48.0_0");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		WebDriver chromedriver = new ChromeDriver(capabilities);
		//WebDriver chromedriver = new ChromeDriver();
		Thread.sleep(3000);
		ArrayList<String> tabs = new ArrayList<String>(chromedriver.getWindowHandles());
		chromedriver.switchTo().window(tabs.get(1));
		chromedriver.close();
		chromedriver.switchTo().window(tabs.get(0));
		String originalHandle = chromedriver.getWindowHandle();
		chromedriver.get("https://open.spotify.com/playlist/0Kh62SFMRacUsKMb7zQ4Zu");
		chromedriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[5]/div[1]/nav/div[2]/div/p[2]/button")).click();
		Thread.sleep(3000);
		chromedriver.findElement(By.xpath("//*[@id=\"app\"]/body/div[1]/div[2]/div/div[2]/div/a")).click();
		Thread.sleep(1500);
		String testUser_username = fetchCredentials("general", "username");
		String testUser_password = fetchCredentials("general", "password");
		chromedriver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys(testUser_username);
		chromedriver.findElement(By.xpath("//*[@id=\"pass\"]")).sendKeys(testUser_password);
		chromedriver.findElement(By.xpath("//*[@id=\"loginbutton\"]")).click();
		Thread.sleep(3000);

		while(true) {
			//song Name
			String songName = chromedriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[4]/div[3]/footer/div/div[1]/div/div[1]/div[1]/div/span/a")).getText();
			String[] songName2 = songName.split("()", 1);
			String sN = songName2[0];
			System.out.println("Song Title: " + songName);

			//song Artists
			String songArtists= null;
			String[] songArtists2 = null;
			String sA = null;
			int maxLimit = chromedriver.findElements(By.xpath("//*[@id=\"main\"]/div/div[4]/div[3]/footer/div[1]/div[1]/div/div/div[2]/span")).size();
			for(int i = 1; i<=maxLimit; i++) {
				songArtists = chromedriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[4]/div[3]/footer/div[1]/div[1]/div/div/div[2]/span[" + i + "]/span/span/a")).getText();
				songArtists2 = songArtists.split(",");
				System.out.println("Artists: " + songArtists);
			}
			Thread.sleep(5000);

			//song Time
			String songTime = chromedriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[4]/div[3]/footer/div[1]/div[2]/div/div[2]/div[3]")).getText();
			String[] split = songTime.split(":");
			int Minutes = Integer.parseInt(split[0]);
			int Seconds= Integer.parseInt(split[1]);
			long minutes = TimeUnit.MINUTES.toMillis(Minutes);
			long seconds = TimeUnit.SECONDS.toMillis(Seconds);
			long total = (minutes + seconds);
			System.out.println("Total Song Time: " + total);
			String songTimePassed = chromedriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[4]/div[3]/footer/div[1]/div[2]/div/div[2]/div[1]")).getText();
			String[] split2 = songTimePassed.split(":");
			int Minutes2 = Integer.parseInt(split2[0]);
			int Seconds2 = Integer.parseInt(split2[1]);
			long minutes2 = TimeUnit.MINUTES.toMillis(Minutes2);
			long seconds2 = TimeUnit.SECONDS.toMillis(Seconds2);
			long total2 = (minutes2 + seconds2);
			System.out.println("Current Song Time: " + total2);
			while(total2 == total) { 
				chromedriver.navigate().refresh();
				Thread.sleep(3000);
				songTimePassed = chromedriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[4]/div[3]/footer/div[1]/div[2]/div/div[2]/div[1]")).getText();
				split2 = songTimePassed.split(":");
				Minutes2 = Integer.parseInt(split2[0]);
				Seconds2 = Integer.parseInt(split2[1]);
				minutes2 = TimeUnit.MINUTES.toMillis(Minutes2);
				seconds2 = TimeUnit.SECONDS.toMillis(Seconds2);
				total2 = (minutes2 + seconds2);
			}
			total = total - total2;
			Thread.sleep(2000);

			//get lyrics from azlyrics
			String url="https://www.azlyrics.com/";
			((JavascriptExecutor) chromedriver).executeScript("window.open(arguments[0])", url);
			tabs = new ArrayList<String> (chromedriver.getWindowHandles());
			chromedriver.switchTo().window(tabs.get(1));
			Thread.sleep(2000);

			//search the song			  
			WebElement searchBox = chromedriver.findElement(By.xpath("/html/body/div[2]/div[1]/div[1]/form/div/input"));
			searchBox.sendKeys(sN);
			searchBox.submit();
			Boolean smoosh = false;

			if(chromedriver.findElement(By.xpath("/html/body/div[2]/div/div/div")).getText().contains("Sorry, your search returned"))
			{
				System.out.println("Couldn't find Lyrics :(");
			}
			else {
				if(chromedriver.getPageSource().contains("More Song Results"))
					chromedriver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/table/tbody/tr[6]/td/a")).click();
				sA = songArtists2[0];
				if(chromedriver.getPageSource().contains(sA)){
					int j = 1;
					int tableSeq = 0;
					if(chromedriver.getPageSource().contains("Artist results:")){
						tableSeq++;
					}
					if(chromedriver.getPageSource().contains("Album results:")){
						tableSeq++;
					}
					if(chromedriver.getPageSource().contains("Song results:")){
						tableSeq++;
					}
					int  maxLimit2 = chromedriver.findElements(By.xpath("/html/body/div[2]/div/div/div["+ tableSeq +"]/table/tbody/tr")).size();
					while(j <= maxLimit2 && !smoosh) {
						int i = 0;
						while(i <= maxLimit && !smoosh){
							sA = songArtists2[i];
							System.out.println("Looking for the right version...");
							///html/body/div[2]/div/div/div[3]/table/tbody/tr[1]/td/b
							///html/body/div[2]/div/div/div[3]/table/tbody/tr[2]/td/b
							tableSeq = 0;
							if(chromedriver.getPageSource().contains("Artist results:")){
								tableSeq++;
							}
							if(chromedriver.getPageSource().contains("Album results:")){
								tableSeq++;
							}
							if(chromedriver.getPageSource().contains("Song results:")){
								tableSeq++;
							}
							if(chromedriver.findElement(By.xpath("/html/body/div[2]/div/div/div["+ tableSeq +"]/table/tbody/tr["+ j +"]/td/b")).getText().contains(sA)){//table of all the search results
								chromedriver.findElement(By.xpath("/html/body/div[2]/div/div/div/table/tbody/tr[2]/td/a")).click();
								tabs = new ArrayList<String> (chromedriver.getWindowHandles());
								chromedriver.switchTo().window(tabs.get(2));
								String title = chromedriver.findElement(By.xpath("/html/body/div[3]/div/div[2]/b")).getText();
								System.out.println("Time left: " + total);
								System.out.println(title + " Lyrics");
								smoosh = true;
							}
							else {
								System.out.println("Trying Alternative Artists of the song :(");
								i++;
							}
						}
						System.out.println("Wrong Artist, Going to the next result ;)");
						j++;
					}
				}
				else
					System.out.println("Couldn't find Artist :(");
			}
			Thread.sleep(total);
			for(String handle : chromedriver.getWindowHandles()) {
				if (!handle.equals(originalHandle)) {
					chromedriver.switchTo().window(handle);
					chromedriver.close();
				}
			}
			chromedriver.switchTo().window(originalHandle);
		}
		//recorder.stop();
	}
	public static String fetchCredentials(String userType, String infoToFetch) {
		List<String> fileStrings = new ArrayList<String>();
		File f;
		try {
			if (userType.equals("general"))
				f = new File("./logins/general_login.txt");
			else {
				System.out.println("Error: invalid user specification passed to fetchCredentials().");
				return "Error: invalid user specification passed to fetchCredentials().";
			}

			BufferedReader b = new BufferedReader(new FileReader(f));

			String readLine = "";

			while ((readLine = b.readLine()) != null) {
				fileStrings.add(readLine);
			}

			if (infoToFetch.equals("username"))
				return fileStrings.get(0);
			else if (infoToFetch.equals("password"))
				return fileStrings.get(1);
			else {
				System.out.println("Error: invalid info specification passed to fetchCredentials().");
				return "Error: invalid info specification passed to fetchCredentials().";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Error: Check user and info specification sent to fetchCredentials()."; //should be unreachable if inputs are correct
	}
}