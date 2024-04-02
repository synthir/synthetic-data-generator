package no.uit.syntHIR.syntheticFHIRData.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BasicUtil {
	
	
	public static boolean isStringEmpty(String str) {
		if (str.isEmpty()) 
			return true;
		return false;
	}
	
	public static boolean isStringNull(String str) {
		if (str == null ) 
			return true;
		return false;
	}
	
	public static boolean isStringBlank(String str) {
		if (str.isBlank()) 
			return true;
		return false;
	}
	
	
	public static String convertDecimalToDateTime(double dateInDecimal,String dateTimeFormat) {
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(dateTimeFormat);
		DateTime dateTime = dateTimeFormatter.parseDateTime(String.valueOf(dateInDecimal));		
		
		return dateTime.toString();
	}
	
	public static DateTime convertStringDateToDateTimeFormat(String date,String dateTimeFormat) {
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(dateTimeFormat);
		DateTime dateTime = dateTimeFormatter.parseDateTime(date);		
		
		return dateTime;
	}
	
	public static int differenceBetweenDates(DateTime startDate, DateTime endDate) {
		
		return Days.daysBetween(startDate.toLocalDate(), endDate.toLocalDate()).getDays();
	}
	
	
	public static String replaceDotWithComma(String strToformat) {
		
		String formattedStr = null;
		
		formattedStr = strToformat.replaceAll(",", ".");
		formattedStr = String.format("%.3f", Float.valueOf(formattedStr));
		
		return formattedStr;
	}
	
	public static JsonNode parseJsonStrToJsonNode(String jsonString) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNodeObj = null;
		try {
			jsonNodeObj =  objectMapper.readTree(jsonString);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonNodeObj;
	}
	
	public static String storeFile(InputStream fileInputStream, String fileOriginalName, String targetLocation) {
		 
        Path targetLocationPath = Paths.get(targetLocation + APIConstants.REQUEST_SEPARATOR + fileOriginalName).toAbsolutePath().normalize();
        
        try {
			Files.copy(fileInputStream, targetLocationPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return targetLocationPath.toString();
	}
	
}

