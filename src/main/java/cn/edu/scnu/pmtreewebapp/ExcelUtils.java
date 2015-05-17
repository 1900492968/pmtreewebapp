package cn.edu.scnu.pmtreewebapp;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

import pmtree.PM_tree;
import cn.edu.scnu.pmtreewebapp.model.NetworkPoint;
import cn.edu.scnu.pmtreewebapp.model.RoadNetwork;
import cn.edu.scnu.pmtreewebapp.model.RoadNetworkGraph;

import com.MBR;
import com.PointData;

public class ExcelUtils {
	public static final Sheet getSheetAt(InputStream is, int sheetNum) {
		try {
			Workbook wb = WorkbookFactory.create(is);
			Sheet sheet = wb.getSheetAt(sheetNum);
			return sheet;
		} catch (EncryptedDocumentException | InvalidFormatException
				| IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getLocalizedMessage(), e);
		}
	}

	// public static final Sheet getSheetAt(InputStream is, String sheetName) {
	// try {
	// Workbook wb = WorkbookFactory.create(is);
	// Sheet sheet = wb.get;
	// return sheet;
	// } catch (EncryptedDocumentException | InvalidFormatException |
	// IOException e) {
	// e.printStackTrace();
	// throw new RuntimeException(e.getLocalizedMessage(), e);
	// }
	// }

	public static final <T> List<T> process(InputStream is, int sheetNum,
			Class<T> clazz, String[] fieldsNames, int rowFrom, int rowTo) {
		List<T> result = Collections.emptyList();
		try {
			Workbook wb = WorkbookFactory.create(is);
			Sheet sheet = wb.getSheetAt(sheetNum);
			result = processInternal(sheet, clazz, fieldsNames);
		} catch (EncryptedDocumentException | InvalidFormatException
				| IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getLocalizedMessage(), e);
		}
		return result;
	}

	public static final <T> List<T> process(InputStream is, Class<T> clazz,
			String[] fieldsNames, int rowFrom, int rowTo) {
		return process(is, 0, clazz, fieldsNames, rowFrom, rowTo);
	}

	public static final <T> List<T> process(InputStream is, Class<T> clazz,
			String[] fieldsNames, int rowTo) {
		return process(is, 0, clazz, fieldsNames, 1, rowTo);
	}

	private static final <T> List<T> processInternal(Sheet sheet,
			Class<T> clazz, String[] fieldsNames) {
		List<T> results = new ArrayList<T>();
		Iterator<Row> rows = sheet.rowIterator();
		while (rows.hasNext()) {
			Row row = rows.next();
			T t = newInstance(clazz);
			int endColumn = fieldsNames.length;
			int firstColumn = 0;
			for (int i = firstColumn; i < endColumn; i++) {
				if (fieldsNames[i] != null || "".equals(fieldsNames[i])) {
					try {
						Field field = t.getClass().getField(fieldsNames[i]);
						Class<?> fieldType = field.getType();
						Cell cell = row.getCell(i);
						if (cell != null) {
							if (fieldType.getName().equals("String")) {
								field.set(t, cell.getStringCellValue());
							} else if (fieldType.getName().equals("int")) {
								field.setInt(t,
										(int) cell.getNumericCellValue());
							} else if (fieldType.getName().equals("double")) {
								field.setDouble(t, cell.getNumericCellValue());
							} else if (fieldType.getName().equals("float")) {
								field.setFloat(t,
										(float) cell.getNumericCellValue());
							} else if (fieldType.getName().equals("long")) {
								field.setLong(t,
										(long) cell.getNumericCellValue());
							} else if (fieldType.getName().equals("boolean")) {
								field.setBoolean(t, cell.getBooleanCellValue());
							} else if (fieldType.getName().equals("Date")) {
								field.set(t, cell.getDateCellValue());
							} else {
								field.set(t, cell.getStringCellValue());
							}
						}
					} catch (NoSuchFieldException | SecurityException
							| IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
						throw new RuntimeException("读取Excel失败。"
								+ e.getLocalizedMessage(), e);
					}
				}
			}
		}
		return results;
	}

	private static final <T> T newInstance(Class<T> clazz) {
		T t = null;
		try {
			t = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getLocalizedMessage(), e);
		}
		return t;
	}

	// private static final <T> T newInstance() {
	// // T t = null;
	// try {
	// T.class.newInstance();
	// // t = (T) t.getClass().newInstance();
	// } catch (InstantiationException | IllegalAccessException e) {
	// e.printStackTrace();
	// throw new RuntimeException(e.getLocalizedMessage(), e);
	// }
	// // return t;
	// }

	public static final List<RoadNetwork> processRoadNetwork(InputStream is) {
		final List<RoadNetwork> result = new ArrayList<RoadNetwork>();
		Sheet sheet = getSheetAt(is, 0);
		Iterator<Row> iter = sheet.rowIterator();
		// 略去第一行
		if (iter.hasNext())
			iter.next();

		while (iter.hasNext()) {
			Row row = iter.next();
			int firstColumn = row.getFirstCellNum();
			int endColumn = row.getLastCellNum();
			Cell rnidCell = row.getCell(0);
			if (rnidCell == null || firstColumn == -1 || endColumn == -1) {
				continue;
			}
			int rnidInt = (int) rnidCell.getNumericCellValue();
			String rnid = rnidInt + "";
			List<NetworkPoint> npList = new ArrayList<NetworkPoint>(endColumn);
			RoadNetwork rn = new RoadNetwork();
			rn.setId(rnid);
			rn.setName(rnid);
			rn.setJointPoints(npList);
			result.add(rn);

			for (int i = firstColumn + 1; i < endColumn; i++) {
				Cell cell = row.getCell(i);
				if (cell == null) {
					continue;
				}
				String value = cell.getStringCellValue();
				NetworkPoint np = extractNetworkPoint(value);
				npList.add(np);
			}
		}
		return result;
	}

	private static NetworkPoint extractNetworkPoint(String value) {
		String[] subValue = value.split("[,|，]");
		double x = Double.valueOf(subValue[0].trim());
		double y = Double.valueOf(subValue[1].trim());
		NetworkPoint np = new NetworkPoint(x, y);
		return np;
	}

	public static final Map<String, List<MBR>> processTracetoryMBR(
			InputStream is) {
		final Map<String, List<MBR>> result = new HashMap<String, List<MBR>>();
		Sheet sheet = getSheetAt(is, 1);
		Iterator<Row> iter = sheet.rowIterator();
		// 略去第一行
		if (iter.hasNext())
			iter.next();
		while (iter.hasNext()) {
			Row row = iter.next();
			String rnid = row.getCell(0).getStringCellValue().trim();
			String objid = row.getCell(1).getStringCellValue().trim();
			String spaceStr = row.getCell(2).getStringCellValue().trim();
			String timeStr = row.getCell(3).getStringCellValue().trim();
			List<MBR> mbrList = getMBRList(rnid, result);
			MBR mbr = extractMBR(objid, spaceStr, timeStr);
			mbrList.add(mbr);
		}
		return result;
	}

	private static MBR extractMBR(String objid, String spaceStr, String timeStr) {
		int objId = Integer.valueOf(objid);
		String[] subValue = spaceStr.split("[,|，]");
		int d1 = Integer.valueOf(subValue[0].trim());
		int d2 = Integer.valueOf(subValue[1].trim());

		subValue = timeStr.split("[,|，]");
		int t1 = Integer.valueOf(subValue[0].trim());
		int t2 = Integer.valueOf(subValue[1].trim());

		PointData p1 = new PointData(objId, d1, t1);
		PointData p2 = new PointData(objId, d2, t2);
		MBR mbr = new MBR(p1, p2);
		return mbr;
	}

	private static final List<MBR> getMBRList(String rnid,
			Map<String, List<MBR>> map) {
		List<MBR> mbrList = map.get(rnid);
		if (mbrList == null) {
			mbrList = new ArrayList<MBR>();
			map.put(rnid, mbrList);
		}
		return mbrList;
	}

	public static final RoadNetworkGraph processGraphFromExcel(
			RoadNetworkGraph rng, MultipartFile roadnetworkfile) {
		if (roadnetworkfile == null)
			return rng;
		List<RoadNetwork> roadNetworks = null;
		Map<String, List<MBR>> map = null;
		try {
			roadNetworks = ExcelUtils.processRoadNetwork(roadnetworkfile
					.getInputStream());
			map = ExcelUtils.processTracetoryMBR(roadnetworkfile
					.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("上传处理失败。" + e.getLocalizedMessage(), e);
		}
		Iterator<RoadNetwork> itr = roadNetworks.iterator();
		while (itr.hasNext()) {
			RoadNetwork rn = itr.next();
			String rnid = rn.getId();
			List<MBR> mrbList = map.get(rnid);
			if (mrbList == null)
				mrbList = Collections.emptyList();
			List<List<MBR>> pm_tree = PM_tree.generateLop(mrbList,
					mrbList.size());
			String fileName = RoadNetwork.generatePMTreeFileName(rng.getName(),
					rnid);
			rn.setPmtreeFileName(fileName);
			RoadNetwork.savePMTree(fileName, pm_tree);
		}
		rng.setRoadNetworks(roadNetworks);
		return rng;
	}
}
