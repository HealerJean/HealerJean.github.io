/*
 * Copyright (C) 2018 dy_only, Inc. All Rights Reserved.
 */
package com.fintech.core.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.fintech.sc.exception.Exceptions;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

public class Html2PdfUtil {
	static final Charset UTF8 = Charset.forName("UTF-8");
	public static String cssFile = "/static/assets/dist/css/itextpdf.css";
	public static String imagePath;

	public static void main(String[] args) {
			parse2Pdf("D:/pdf/html.html", "D:/pdf/html.pdf");
	}

	public static void parse2Pdf(String htmlFle, String pdfFile) {
		try {
			parse2Pdf((InputStream) (new FileInputStream(htmlFle)), (OutputStream) (new FileOutputStream(pdfFile)));
		} catch (FileNotFoundException arg2) {
			throw Exceptions.unchecked("转换失败", arg2);
		}
	}

	public static void parse2Pdf(InputStream input, OutputStream pdfFile) {
		try {
			Document e = new Document(PageSize.A4);
			PdfWriter writer = PdfWriter.getInstance(e, pdfFile);
			e.open();
			InputStream cssIn = Html2PdfUtil.MyFontsProvider.class.getResourceAsStream(cssFile);
			XMLWorkerHelper.getInstance().parseXHtml(writer, e, input, cssIn, UTF8, new Html2PdfUtil.MyFontsProvider("fonts/simsun.ttc"));
			e.close();
		} catch (Exception arg4) {
			throw Exceptions.unchecked("转换失败", arg4);
		}
	}

	public static void parseHtml2Pdf(String htmlContext, OutputStream pdfFile) {
		ByteArrayInputStream input = new ByteArrayInputStream(htmlContext.getBytes());
		parse2Pdf((InputStream) input, (OutputStream) pdfFile);
	}

	public static void parseHtml2Pdf(String htmlContext, File pdfFile) {
		try {
			parseHtml2Pdf(htmlContext, (OutputStream) (new FileOutputStream(pdfFile)));
		} catch (Exception arg2) {
			throw Exceptions.unchecked("转换失败", arg2);
		}
	}

	
	protected static class ImageProvider extends AbstractImageProvider {
		private String imageRootPath;

		public ImageProvider(String imageRootPath) {
			this.imageRootPath = imageRootPath;
		}

		public String getImageRootPath() {
			return this.imageRootPath;
		}
	}

	protected static class MyXMLParser {
		public static XMLParser getInstance(Document doc, PdfWriter pdfWriter) throws Exception {
			CssFilesImpl cssFiles = new CssFilesImpl();
			if (StringUtils.isNotBlank(Html2PdfUtil.cssFile)) {
				cssFiles.add(XMLWorkerHelper.getCSS(new FileInputStream(new File(Html2PdfUtil.cssFile))));
			} else {
				cssFiles.add(XMLWorkerHelper.getInstance().getDefaultCSS());
			}

			StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
			HtmlPipelineContext hpc = new HtmlPipelineContext(new CssAppliersImpl());
			if (StringUtils.isNotBlank(Html2PdfUtil.imagePath)) {
				hpc.setImageProvider(new Html2PdfUtil.ImageProvider(Html2PdfUtil.imagePath));
			}

			hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory());
			HtmlPipeline htmlPipeline = new HtmlPipeline(hpc, new PdfWriterPipeline(doc, pdfWriter));
			CssResolverPipeline pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
			return new XMLParser(true, new XMLWorker(pipeline, true));
		}
	}

	public static class MyFontsProvider extends XMLWorkerFontProvider {
		public MyFontsProvider() {
			super((String) null, (HashMap) null);
		}

		public MyFontsProvider(String ttc) {
			super((String) null, (HashMap) null);
			String ttcp = Html2PdfUtil.MyFontsProvider.class.getClassLoader().getResource(ttc).getPath();
			if (StringUtils.indexOf(ttcp, "jar!/") > 0) {
				ttcp = "jar:" + ttcp;
			}

			this.register(ttcp);
		}

		public Font getFont(String fontname, String encoding, float size, int style) {
			String fntname = fontname;
			if (fontname == null) {
				fntname = "SimSun";
			}

			return super.getFont(fntname, encoding, size, style);
		}
	}
}
