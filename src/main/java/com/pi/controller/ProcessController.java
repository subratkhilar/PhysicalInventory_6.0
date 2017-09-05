package com.pi.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.pi.model.Message;
import com.pi.model.StoreProcess;
import com.pi.service.ProcessService;
import com.sherwin.polling.api.InvalidInputException;

@RestController
public class ProcessController {
	@Autowired
	private ProcessService processService;
	Logger logger = Logger.getLogger(ProcessController.class);

	@RequestMapping(value = "/listOsProcess", method = RequestMethod.GET, headers = "Accept=application/json")
	public StoreProcess getAllSProcess() {
		logger.info("===============  ProcessCOntroller...getAllSProcess..============== ");
		List<StoreProcess> storeList = processService.getAllStorePo();
		String dateStr = processService.dateAsString();
		StoreProcess storeProc = new StoreProcess();
		storeProc.setStoreList(storeList);
		storeProc.setProcessDate(dateStr);

		return storeProc;
	}

	@RequestMapping(value = "/approve", method = RequestMethod.POST)
	public ResponseEntity<Message> approve(@RequestBody StoreProcess processData) {

		Message msg = new Message();
		String message = "";
		String result = null;
		List<String> stNoList = processData.getStoreNoList();
		try {
			// result = callToWebService(stNoList);
			// processService.storeTaskId(result,stNoList);
			// processService.updateStore(processData);
			ModifyXMLFile(processData.getProcessDate());
			message = "Inventory Approved Sucesssfully !!";
			msg.setMessage(message);
			return new ResponseEntity<Message>(msg, HttpStatus.CREATED);

		} catch (InvalidInputException ie) {
			message = "Error in Approved !!";
			msg.setMessage(message + " Invalid Input parameters");
			return new ResponseEntity<Message>(msg,
					HttpStatus.FAILED_DEPENDENCY);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Error in Approved !!";
			msg.setMessage(message + e.getMessage());
			return new ResponseEntity<Message>(msg,
					HttpStatus.FAILED_DEPENDENCY);
		}
	}

	/*
	 * public String callToWebService(List<String> stNoList) throws Exception {
	 * PollingClient pc = new PollingClient("dev", "PhysicalInventory"); String
	 * fname =
	 * "C:\\Users\\rxd876\\Downloads\\new_workspace\\PhysicalInventory-2016\\src\\main\\resources\\PollingFile.xml"
	 * ; // String fname = "//resources//PollingFile.xml"; File f = new
	 * File(fname); List<String> storesList = new ArrayList<String>();
	 * storesList.add("9953"); pc.postToStores(storesList, "LIST"); pc.write(f);
	 * String result = pc.postMaintenance(); return result; }
	 */

	/*
	 * public String callToWebService(List<String> stNoList) throws Exception {
	 * 
	 * /*PollingClient pc = new PollingClient("dev", "PhysicalInventory");
	 * String fname =
	 * "C:\\Users\\rxd876\\Downloads\\new_workspace\\PhysicalInventory-2016\\src\\main\\resources\\PollingFile.xml"
	 * ; // String fname = "//resources//PollingFile.xml"; File f = new
	 * File(fname); // List<String> storesList = new ArrayList<String>(); //
	 * storesList.add("9953"); pc.postToStores(stNoList, "LIST"); pc.write(f);
	 * String result = pc.postMaintenance(); return result;
	 * System.out.println(10/0); return null; }
	 */

	@RequestMapping(value = "/createStr", method = RequestMethod.POST)
	public ResponseEntity<Message> updateStores(
			@RequestBody StoreProcess processData,
			UriComponentsBuilder ucBuilder) {
		Message msg = new Message();
		String message = "";
		try {
			processService.updateStore(processData);

			message = "Inventory Saved Sucesssfully !!";
			msg.setMessage(message);
			return new ResponseEntity<Message>(msg, HttpStatus.CREATED);
		} catch (Exception e) {
			message = "Error in Saved !!";
			msg.setMessage(message + e.getMessage());
			return new ResponseEntity<Message>(msg,
					HttpStatus.FAILED_DEPENDENCY);
		}
	}

	public void ModifyXMLFile(String date) throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		//File file = new File(classLoader.getResource("resources/PollingFile.xml").getFile());
		File file = new File(getClass().getResource("resources/PollingFile.xml").getFile());
		// URL url =
		// ProcessController.class.getClassLoader().getResource("PollingFile.xml");
		// System.out.println(url.getPath());
		logger.info("url is " + file.getName());

		String filepath = file.getName();// "\\resource\\PollingFile.xml";
		logger.info("Filepath :: " + filepath);

		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(filepath);

		Node startdate = doc.getElementsByTagName("exec_date").item(0);

		startdate.setTextContent(date);
System.out.println("date >> "+date);
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filepath));
		transformer.transform(source, result);
	}

	@RequestMapping(value = "/invFileUpload", method = RequestMethod.POST)
	public ResponseEntity<Message> invFileUploadForProcess(
			@RequestParam("file") CommonsMultipartFile fileData) {

		Message msg = new Message();
		String message = "";
		Workbook workbook;

		ByteArrayInputStream bis = new ByteArrayInputStream(fileData.getBytes());
		List<String> sheetData = new ArrayList<String>();

		try {
			if (fileData.getOriginalFilename().endsWith("xls")) {
				workbook = new HSSFWorkbook(bis);

			} else if (fileData.getOriginalFilename().endsWith("xlsx")) {
				workbook = new XSSFWorkbook(bis);
			} else {
				throw new IllegalArgumentException(
						"Received file does not have a standard excel extension.");
			}
			//Thread.sleep(50000);
			List<StoreProcess> processList = new ArrayList<StoreProcess>();
			Sheet sheet = workbook.getSheetAt(0);
			for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				StoreProcess process = new StoreProcess();
				String storeNo = "";
				if (row != null) {
					Cell cell = row.getCell(0);
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						storeNo = cell.getStringCellValue();
						logger.info("Cell Value" + cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_BOOLEAN:

						break;
					case Cell.CELL_TYPE_NUMERIC:

						int num = (int) cell.getNumericCellValue();
						storeNo = String.valueOf(num);
						break;
					}
					sheetData.add(storeNo);

					Cell cell1 = row.getCell(1);
					String storeName = cell1.getStringCellValue();
					process.setStoreNo(storeNo);
					process.setStoreName(storeName);
					processList.add(process);
					logger.info("storeNo >> " + storeNo + " storeName >> "
							+ storeName);

				}
			}

			processService.invFileUploadForProcess(sheetData, processList);
			message = "Inventory Upload Sucesssfully !!";
			msg.setMessage(message);
			return new ResponseEntity<Message>(msg, HttpStatus.CREATED);
		} catch (Exception e) {
			message = "Error in Saved !!";
			logger.info("Error Message ::  " + e.getMessage());
			msg.setMessage(message + e.getMessage());
			return new ResponseEntity<Message>(msg,
					HttpStatus.FAILED_DEPENDENCY);
		}
	}
}
