package com.alti.presentation;

import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

import com.alti.business.EmailAttachmentReceiver;
import com.alti.business.ExtractTextByArea;
import com.alti.business.ReplyToEmail;
import com.alti.data.Db;

public class Main {

	public static void main(String[] args) {
		EmailAttachmentReceiver emailReceiver = new EmailAttachmentReceiver();
		ExtractTextByArea extraction = new ExtractTextByArea();
		Db dataBase = new Db();
		ReplyToEmail acknowledgeMail = new ReplyToEmail();
		InputStream invoiceToExtract = null;
		invoiceToExtract = emailReceiver.downloadEmailAttachments();

		try (PDDocument document = PDDocument.load(invoiceToExtract)) {
			int count = document.getNumberOfPages();

			for (int pageNumber = 0; pageNumber < document.getNumberOfPages(); pageNumber++) {

				PDFTextStripper pdfStripper = new PDFTextStripper();
				pdfStripper.setStartPage(pageNumber);
				pdfStripper.setEndPage(pageNumber);
				String contents = pdfStripper.getText(document).toLowerCase();

				PDPage currentPage = document.getPage(pageNumber);

				if (!document.isEncrypted()) {
					String inoviceNum = extraction.invoiceNo(currentPage);
					String inDate = extraction.invoiceDate(currentPage);
					String cPo = extraction.customerPo(currentPage);
					String addressTo = extraction.address(currentPage);
					String total = extraction.finalAmount(currentPage);
					dataBase.sendToDb(inoviceNum, inDate, cPo, addressTo, total);
					pageNumber++;

				}

			}

		} catch (IOException e) {
			System.err.println("Exception while trying to read pdf document - " + e);
		}
		dataBase.displayDb();
		acknowledgeMail.reply();

	}

}
