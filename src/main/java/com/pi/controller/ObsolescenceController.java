package com.pi.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

import com.pi.model.Message;
import com.pi.model.Obsolescence;
import com.pi.model.StoreProcess;
import com.pi.service.ObsolescenceService;

@RestController
public class ObsolescenceController {
	@Autowired
	private ObsolescenceService obsolService;

	@RequestMapping(value = "/getAllObs", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<Obsolescence> getAllObsolence() {
		List<Obsolescence> obsList = obsolService.getAllObsolence();
		System.out.println("=========== cming to fetching data =="+obsList.size());
		return obsList;
	}

	@RequestMapping(value = "/deleteObso", method = RequestMethod.POST)
	public ResponseEntity<Message> approve(@RequestBody Obsolescence obsolence) {
		System.out.println("no of records " + obsolence.getSkuNoList().size());
		obsolService.deleteObsolence(obsolence);
		Message msg = new Message();
		String message = "";
		message = "Deleted Successfully !!";
		msg.setMessage(message);
		return new ResponseEntity<Message>(msg, HttpStatus.OK);
	}

	@RequestMapping(value = "/createObso", method = RequestMethod.POST)
	public ResponseEntity<Message> createObs(@RequestBody Obsolescence obsolence) {
		System.out.println("Store No  of records " + obsolence.getoId());
		System.out.println("Sku no records " + obsolence.getSkuNo());
		Message msg = new Message();
		String message = "";

		try {
			message = obsolService.createObsolence(obsolence);
			msg.setMessage(message);
			return new ResponseEntity<Message>(msg, HttpStatus.OK);

		} catch (Exception e) {
			message = "Error in  Obsolence Creation !!";
			msg.setMessage(message);
			return new ResponseEntity<Message>(msg,
					HttpStatus.FAILED_DEPENDENCY);
		}

	}

	@RequestMapping(value = "/obsFileUpload", method = RequestMethod.POST)
	public ResponseEntity<Message> obsFileUpload(
			@RequestParam("file") CommonsMultipartFile fileData) {
		System.out.println("Obns File Uploading ");
		Message msg = new Message();
		String message = "";
		ByteArrayInputStream bis = new ByteArrayInputStream(fileData.getBytes());
		List<String> sheetData = new ArrayList<String>();
		Workbook workbook;
		try {
			// HSSFWorkbook workbook= new HSSFWorkbook(bis);
			if (fileData.getOriginalFilename().endsWith("xls")) {
				workbook = new HSSFWorkbook(bis);
				// workbook = new HSSFWorkbook(bis);
			} else if (fileData.getOriginalFilename().endsWith("xlsx")) {
				workbook = new XSSFWorkbook(bis);
			} else {
				throw new IllegalArgumentException(
						"Received file does not have a standard excel extension.");
			}

			//
			List<Obsolescence> processList = new ArrayList<Obsolescence>();
			Sheet sheet = workbook.getSheetAt(0);
			for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				Obsolescence process = new Obsolescence();
				String storeNo = "";
				if (row != null) {
					Cell cell = row.getCell(0);
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						storeNo = cell.getStringCellValue();
						// sheetData.add(cell.getStringCellValue());
						//System.out.print("here string"		+ cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						//System.out.print("here boolean "								+ cell.getBooleanCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						//System.out.print("here number.."	+ cell.getNumericCellValue());
						int num = (int) cell.getNumericCellValue();
						storeNo = String.valueOf(num);
						// storeNo = Double.toString(
						// cell.getNumericCellValue());
						break;
					}
					// String storeNo = cell.getStringCellValue();

					sheetData.add(storeNo);

					Cell cell1 = row.getCell(1);
					String skuNo = cell1.getStringCellValue();
					process.setStoreNumber(storeNo);
					process.setSkuNo(skuNo);
					processList.add(process);
					
				}
			}
			
			obsolService.obsFileUploadForProcess(sheetData,processList);
			message = "Obs Upload Sucesssfully !!";
			msg.setMessage(message);
			return new ResponseEntity<Message>(msg, HttpStatus.CREATED);
		} catch (Exception e) {
			message = "Error in Saved !!";
			msg.setMessage(message + e.getMessage());
			return new ResponseEntity<Message>(msg,
					HttpStatus.FAILED_DEPENDENCY);
		}
	}
}
