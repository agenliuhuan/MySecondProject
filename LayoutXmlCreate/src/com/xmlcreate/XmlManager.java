package com.xmlcreate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 测试DOM4J创建，修改，遍历XML
 * 
 * @author jialin
 * 
 */
public class XmlManager {
	public static void main(String[] args) {
		XmlManager xmlManager = new XmlManager();
		// 初始化xml文档
		Document doc = null;
		// 通过dom4j方法创建xml
		// doc = xmlManager.createXml();

		// // XML字符串
		// String strXMl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		// "<?xml-stylesheet type=\"text/xsl\" href=\"students.xsl\"?>"
		// + "<students><!--Students Table-->   <student stu=\"001\">" +
		// "<name>张三</name><age>18</age></student><student stu=\"002\">"
		// + "<name>李四</name><age>19</age></student></students>";
		// // 通过字符串创建xml
		// doc = xmlManager.createDocumentByString(strXMl);
		// // XMl输出路径
		// String outputPath = "xml/Students.xml";
		// // 输出xml
		// xmlManager.saveDocument(doc, outputPath);

		// xml输入路径
		String inputPath = "xml/test.xml";

		// 根据xml路径更改XML
		// xmlManager.ModifyXml(inputPath);

		// 根据xml路径获取doc
		// xmlManager.getDocument(inputPath);

		// XMl输出路径
		String outputPath = "xml/output.xml";
		// 输出xml
		// xmlManager.saveDocument(doc, outputPath);

		// 遍历XML
		// xmlManager.traversalDocumentByElementIterator(doc);
		// xmlManager.traversalDocumentByVisitor(doc);
		// xmlManager.traversalDocumentByElements(doc);
		// xmlManager.traversalDocumentByselectNodes(doc,
		// "/Students/student/name");

		createResolution(720, 320, 480, xmlManager);
		// createResolution(720, 360, xmlManager);
		createResolution(720, 480, 800, xmlManager);
		createResolution(720, 480, 854, xmlManager);
		createResolution(720, 540, 960, xmlManager);
		createResolution(720, 600, 1024, xmlManager);
		createResolution(720, 640, 960, xmlManager);
		createResolution(720, 720, 1184, xmlManager);
		createResolution(720, 768, 1280, xmlManager);
		createResolution(720, 800, 1280, xmlManager);
		createResolution(720, 1080, 1776, xmlManager);
		createResolution(720, 1080, 1920, xmlManager);
		createResolution(720, 1152, 1920, xmlManager);
		System.out.println("全部创建成功");
	}

	public static void createResolution(int srcResolution, int destResolution, int destHeightResolution, XmlManager xmlManager) {
		String dirPath = "output2/layout-" + destHeightResolution + "x" + destResolution;
		File dir = new File(dirPath);
		deleteDir(dir);

		String path = "layout";
		File file = new File(path);
		File[] tempList = file.listFiles();
		System.out.println("该目录下对象个数：" + tempList.length);
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				// System.out.println("文     件：" + tempList[i].getPath());
				xmlManager.ModifyXml(srcResolution, destResolution, destHeightResolution, tempList[i].getPath());
			}
			if (tempList[i].isDirectory()) {
				// System.out.println("文件夹：" + tempList[i]);
			}
		}
	}

	/**
	 * 获取XML文件
	 * 
	 * @param inputPath
	 * @return
	 */
	public Document getDocument(String inputPath) {
		// 输入文件
		File inputXml = new File(inputPath);
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(inputXml);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * 通过Dom4j方法创建xml文档
	 * 
	 * @return
	 */
	public Document createXml() {
		Document doc = DocumentHelper.createDocument();
		// 创建ProcessingInstruction
		Map<String, String> inMap = new HashMap<String, String>();
		inMap.put("type", "text/xsl");
		inMap.put("href", "students.xsl");
		doc.addProcessingInstruction("xml-stylesheet", inMap);
		// 增加根节点
		Element studentsElement = doc.addElement("Students");
		// 增加注释
		studentsElement.addComment("Students Table");
		// 增加子节点
		Element stuElement = studentsElement.addElement("student");
		// 增加属性
		stuElement.addAttribute("stu", "001");
		// 增加名称节点
		Element nameElement = stuElement.addElement("name");
		// 设置名称节点的值
		nameElement.setText("张三");
		// 增加年龄节点
		Element ageElement = stuElement.addElement("age");
		// 设置年龄节点的值
		ageElement.setText("18");

		// 同上
		Element anotherStuElement = studentsElement.addElement("student");
		anotherStuElement.addAttribute("stu", "002");
		Element anotherNameElement = anotherStuElement.addElement("name");
		anotherNameElement.setText("李四");
		Element anotherAgeElement = anotherStuElement.addElement("age");
		anotherAgeElement.setText("19");

		return doc;
	}

	/**
	 * 通过字符串创建xml文档
	 * 
	 * @param xmlStr
	 * @return
	 */
	public Document createDocumentByString(String xmlStr) {

		Document doc = null;
		try {
			// 通过字符串转换直接构建xml文档
			doc = DocumentHelper.parseText(xmlStr);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * 修改xml
	 * 
	 * @param inputXmlPath
	 */
	public void ModifyXml(int srcResolution, int destResolution, int destHeightResolution, String inputXmlPath) {
		// 获取文件
		File inputXml = new File(inputXmlPath);

		try {
			SAXReader saxReader = new SAXReader();
			// 创建document
			Document doc = saxReader.read(inputXml);

			// 读取Students/student下所有具有属性stu的元素
			// List list = doc.content();
			// Iterator iter = list.iterator();
			// while (iter.hasNext()) {
			// Attribute attribute = (Attribute) iter.next();
			// if (attribute.getValue().equals("001"))
			// attribute.setValue("0001");
			// }

			String value = null;
			String newValue = null;
			List list = doc.content();
			Iterator iter = list.iterator();
			readElement(srcResolution, destResolution, iter);
			String fileName = inputXmlPath.substring(6);
			String outputPath = "output2/layout-" + destHeightResolution + "x" + destResolution + fileName;
			saveDocument(doc, outputPath);

		}

		catch (DocumentException e) {
			System.out.println(e.getMessage());
		}

	}

	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
			return true;
		} else {
			// 目录此时为空，可以删除
			return dir.delete();
		}
	}

	private void readElement(int srcResolution, int destResolution, Iterator iter) {
		String value = null;
		String newValue = null;

		while (iter.hasNext()) {
			try {

				Element element = (Element) iter.next();
				Iterator iter2 = element.attributeIterator();
				while (iter2.hasNext()) {
					Attribute attribute = (Attribute) iter2.next();
					value = attribute.getValue();
					setValue(attribute, value, srcResolution, destResolution);
				}

				Iterator iterator = element.elementIterator();
				while (iterator.hasNext()) {
					Element nameElement = (Element) iterator.next();

					iter2 = nameElement.attributeIterator();
					while (iter2.hasNext()) {
						Attribute attribute = (Attribute) iter2.next();
						value = attribute.getValue();
						setValue(attribute, value, srcResolution, destResolution);
					}

					readElement(srcResolution, destResolution, nameElement.elementIterator());
				}

			} catch (Exception e) {
			}
		}
	}

	private void setValue(Attribute attribute, String value, int srcResolution, int destResolution) {
		int pixelValue = 0;
		String unit = "";
		String newValue;

		if (value.endsWith("px")) {
			newValue = value.substring(0, value.length() - 2);
			if (isNumeric(newValue)) {
				unit = "px";
				try {
					pixelValue = Integer.parseInt(newValue.replace(unit, ""));

					if (pixelValue > 1) {
						pixelValue = (int) (((float) pixelValue) * 2 * destResolution / srcResolution / 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				attribute.setValue("" + pixelValue + unit);
			}
		} else if (value.endsWith("dp")) {
			newValue = value.substring(0, value.length() - 2);
			if (isNumeric(newValue)) {
				unit = "dp";
				try {
					pixelValue = Integer.parseInt(newValue.replace(unit, ""));

					pixelValue = (int) (((float) pixelValue) * 2 * destResolution / srcResolution);
				} catch (Exception e) {
					e.printStackTrace();
				}
				unit = "px";

				attribute.setValue("" + pixelValue + unit);
			}
		} else if (value.endsWith("dip")) {
			newValue = value.substring(0, value.length() - 3);
			if (isNumeric(newValue)) {
				unit = "dip";
				try {
					pixelValue = Integer.parseInt(newValue.replace(unit, ""));

					pixelValue = (int) (((float) pixelValue) * 2 * destResolution / srcResolution);
				} catch (Exception e) {
					e.printStackTrace();
				}
				unit = "px";

				attribute.setValue("" + pixelValue + unit);
			}
		}

	}

	/**
	 * 将文档输出到文件保存，可指定格式化输出,可指定字符编码。
	 * 
	 * @param document
	 * @param outputFile
	 */
	public void saveDocument(Document doc, String outputPath) {
		// 输出文件
		File outputFile = new File(outputPath);
		try {
			// 美化格式
			OutputFormat format = OutputFormat.createPrettyPrint();
			// 指定XML编码,不指定的话，默认为UTF-8
			format.setEncoding("UTF-8");
			XMLWriter output = new XMLWriter(new FileWriter(outputFile), format);
			output.write(doc);
			output.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 普通方法遍历xml
	 * 
	 * @param doc
	 */
	public void traversalDocumentByElementIterator(Document doc) {
		// 获取根节点
		Element root = doc.getRootElement();
		// 枚举根节点下所有子节点
		for (Iterator ie = root.elementIterator(); ie.hasNext();) {
			System.out.println("======");
			Element element = (Element) ie.next();
			System.out.println(element.getName());

			// 枚举属性
			for (Iterator ia = element.attributeIterator(); ia.hasNext();) {
				Attribute attribute = (Attribute) ia.next();
				System.out.println(attribute.getName() + ":" + attribute.getData());
			}
			// 枚举当前节点下所有子节点
			for (Iterator ieson = element.elementIterator(); ieson.hasNext();) {
				Element elementSon = (Element) ieson.next();
				System.out.println(elementSon.getName() + ":" + elementSon.getText());
			}
		}
	}

	/**
	 * 使用elements方法进行xml的读取，相当于条件查询，可以根据不同的节点，利用for循环查询该节点下所有的数据。
	 * 
	 * @throws DocumentException
	 */
	public static void traversalDocumentByElements(Document doc) {
		// 获取根节点
		Element root = doc.getRootElement();
		// 根据根节点，将根节点下 student中的所有数据放到list容器中。
		List list = root.elements("student");
		// 这种遍历方式，是jdk1.5以上的版本支持的遍历方式,嘿嘿试试
		for (Object obj : list) {
			Element el = (Element) obj;
			System.out.println("----------" + el.getName() + "-----------");
			// 获取name节点下所有的内容，存入listName容器中
			List listName = el.elements("name");
			// 获取age节点下所有的内容，存入age容器中
			List listAge = el.elements("age");
			for (int i = 0; i < listName.size(); i++) {
				Element elname = (Element) listName.get(i);
				// 获取name节点下的数据。
				System.out.println(elname.getName() + ": " + elname.getText());
				Element elage = (Element) listAge.get(i);
				// 获取age节点下的数据。
				System.out.println(elage.getName() + ": " + elage.getText());

			}

		}
	}

	/**
	 * 使用selectNodes读取xml文件
	 * 
	 * @param args
	 * @throws DocumentException
	 */
	public static void traversalDocumentByselectNodes(Document doc, String elementpath) {
		// 使用selectNodes获取所要查询xml的节点。
		List list = doc.selectNodes(elementpath);

		// 遍历节点，获取节点内数据。
		for (Iterator ie = list.iterator(); ie.hasNext();) {
			Element el = (Element) ie.next();
			System.out.println(el.getName() + ": " + el.getText());

		}

	}

	/**
	 * 基于访问者模式遍历
	 * 
	 * @param doc
	 */
	public void traversalDocumentByVisitor(Document doc) {
		doc.accept(new MyVisitor());
	}

}