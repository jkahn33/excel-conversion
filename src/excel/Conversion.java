package excel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.logging.Logger;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Conversion {
	public static boolean webError;
	public static boolean spreadsheetError;
    private static Logger log = Logger.getLogger(Conversion.class.getName());
    public static ArrayList<String> getRestaurants(String state, String city){
    	ArrayList<String> restaurants = new ArrayList<>();
        String url = "https://www.allmenus.com/" + state + "/" + city + "/-/?filters=filter_online";
        Elements links = null;
        Elements table = null;
        try{
            Document doc = Jsoup.connect(url).get();
            table = doc.select("h4.name");
            links = table.select("a");
            for(Element el : links) {
            	restaurants.add(el.attr("abs:href"));
            }
        }
        catch(java.io.IOException e){
        	webError = true;
            log.severe("Error in URL Grabbing: " + e);
        }
        return restaurants;
    }
    public static Restaurant getMenuInfo(String url){
        Elements category = null;
        Elements menuItems = null;
        Restaurant rest = null;
        String[] splitStr = null;
        boolean overallPrice = false;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements headerGet = doc.select("div.menu-header");
            String restName = headerGet.select("h1").text();
            category = doc.select("#menu li.menu-category");
            rest = new Restaurant(restName);
            for(Element cat : category){
            	overallPrice = false;
            	menuItems = cat.select("li.menu-items");
            	String catName = cat.select("div.h4").text();
            	if(catName.contains("$")){
            		splitStr = catName.split("\\$");
            		overallPrice = true;
            	}
            	for(Element el : menuItems){
            		float price = 0;
            		String itemName = el.select("span.item-title").text();
	                String description = el.select("p.description").text();
	                String location = doc.select("a.menu-address").text();
            		if(overallPrice){
            			try{
            				price = Float.parseFloat(splitStr[1]);
            			}
            			catch(java.lang.NumberFormatException e){
            				overallPrice = false;
            				price = Float.parseFloat(el.select("span.item-price").text().replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
            			}
            		}
            		else{
		                try{
		                    price = Float.parseFloat(el.select("span.item-price").text().replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
		                }
		                catch(java.lang.NumberFormatException e){
		                    price = -1;
		                }
            		}
	                Item item = new Item(itemName, description, price, location);
	                rest.addItem(item);
            	}
            }
        }
        catch (java.io.IOException e){
        	webError = true;
            log.severe("Error in url grabbing: " + e);
        }
        return rest;
    }
    public static void writeToExcel (ArrayList<Restaurant> restaurants, String location, String fileName){
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet("Menu");
        String priceVal = null;
        int dec = -5;
        int start = -5;
        int rowCount = 0;
        for (Restaurant rest : restaurants){
            for(Item item : rest.menu()){
                Row row = sheet.createRow(rowCount++);
                Cell name = row.createCell(0);
                Cell itemName = row.createCell(1);
                Cell description = row.createCell(2);
                Cell address = row.createCell(4);
                name.setCellValue(rest.name());
                itemName.setCellValue(item.name());
                description.setCellValue(item.description());
                if(item.price() != -1) {
                    if(item.price() % 1 != 0) {
                        String val = Float.toString(item.price());
                        start = val.indexOf('.');
                        dec = val.length() - start - 1;
                        if (dec == 1) {
                            String value = Float.toString(item.price());
                            value += "0";
                            Cell price = row.createCell(3);
                            price.setCellValue(value);
                            priceVal = "if";
                        } else {
                            Cell price = row.createCell(3);
                            price.setCellValue(Float.toString(item.price()));
                            priceVal = "if else";
                        }
                    }
                    else{
                        String value = Float.toString(item.price());
                        value += "0";
                        Cell price = row.createCell(3);
                        price.setCellValue(value);
                        priceVal = "else";
                    }
                }
                address.setCellValue(item.location());
            }
        }
        try{
            FileOutputStream outputStream = new FileOutputStream(location + "/" + fileName + ".xlsx");
            book.write(outputStream);
            book.close();
        }catch(IOException e){
        	spreadsheetError = true;
            log.severe("Failed to create spreadsheet. Exception: " + e);
        }
    }
}
