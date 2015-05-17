package cn.edu.scnu.pmtreewebapp;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class ExcelView extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workbook,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HSSFSheet excelSheet = workbook.createSheet("list");
		setExcelHeader(excelSheet);
		
		List animalList = (List) model.get("animalList");
		setExcelRows(excelSheet,animalList);
		
	}

	public void setExcelHeader(HSSFSheet excelSheet) {
		HSSFRow excelHeader = excelSheet.createRow(0);
		excelHeader.createCell(0).setCellValue("Id");
		excelHeader.createCell(1).setCellValue("Name");
		excelHeader.createCell(2).setCellValue("Type");
		excelHeader.createCell(3).setCellValue("Aggressive");
		excelHeader.createCell(4).setCellValue("Weight");
	}
	
	public void setExcelRows(HSSFSheet excelSheet, List animalList){
//		int record = 1;
//		for (Animal animal : animalList) {
//			HSSFRow excelRow = excelSheet.createRow(record++);
//			excelRow.createCell(0).setCellValue(animal.getId());
//			excelRow.createCell(1).setCellValue(animal.getAnimalName());
//			excelRow.createCell(2).setCellValue(animal.getAnimalType());
//			excelRow.createCell(3).setCellValue(animal.getAggressive());
//			excelRow.createCell(4).setCellValue(animal.getWeight());
//		}
	}
}