package com.alti.business;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;



import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ExtractTextByArea {
	
	

	public static String invoiceNo(PDPage currentPage) {
		PDFTextStripperByArea stripper;
		try {
			stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(true);
			Rectangle rect = new Rectangle(0, 130, 150, 10);
			stripper.addRegion("class1", rect);
			stripper.extractRegions(currentPage);
			return  (stripper.getTextForRegion("class1"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String invoiceDate(PDPage currentPage) {
		PDFTextStripperByArea stripper;
		try {
			stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(true);
			Rectangle rect = new Rectangle(130, 130, 160, 8);
			stripper.addRegion("class1", rect);
			stripper.extractRegions(currentPage);
			return (stripper.getTextForRegion("class1"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String customerPo(PDPage currentPage) {
		PDFTextStripperByArea stripper;
		try {
			stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(true);
			Rectangle rect = new Rectangle(260, 148, 140, 8);
			stripper.addRegion("class1", rect);
			stripper.extractRegions(currentPage);
			return (stripper.getTextForRegion("class1"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String address(PDPage currentPage) {
		PDFTextStripperByArea stripper;
		try {
			stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(true);
			Rectangle rect = new Rectangle(285, 162, 200, 70);
			stripper.addRegion("class1", rect);
			stripper.extractRegions(currentPage);
			return (stripper.getTextForRegion("class1"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String finalAmount(PDPage currentPage) {
		PDFTextStripperByArea stripper;
		try {
			stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(true);
			for (int i = 0, y = 382; i < 20; i++, y = y + 10) {
				Rectangle rect = new Rectangle(370, y, 550, 8);
				stripper.addRegion("class1", rect);
				stripper.extractRegions(currentPage);
				if (stripper.getTextForRegion("class1").contains("Total Invoice")) {
					return (stripper.getTextForRegion("class1").replace("Total Invoice ", ""));

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

}
