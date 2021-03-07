package Services;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import Entity.Product;
import Entity.order;
import Entity.user_full;


public class order_excel extends AbstractXlsxView{
	private String header= "HÓA ĐƠN BÁN LẺ";
	private String[] cation_table= {"THÔNG TIN NHÀ BÁN HÀNG","THÔNG TIN NGƯỜI MUA","THÔNG TIN ĐƠN HÀNG"};
	private String[] header_table_order = {"SẢN PHẨM","GIÁ THÀNH","TỔNG GIÁ"};
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		order o = (order) model.get("order");
		String name ="don_hang_"+o.getMadh()+".xlsx";
		Sheet sheet = workbook.createSheet("1");
		CreationHelper creationhelper= workbook.getCreationHelper();
		//Merge cells
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 5));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 8));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 2));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 3, 5));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 8));
		sheet.addMergedRegion(new CellRangeAddress(3, 5, 0, 2));
		sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, 2));
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 5));
		sheet.addMergedRegion(new CellRangeAddress(4, 4, 3, 5));
		sheet.addMergedRegion(new CellRangeAddress(5, 5, 3, 5));
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 6, 8));
		sheet.addMergedRegion(new CellRangeAddress(4, 6, 6, 8));
		sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 8));
		sheet.addMergedRegion(new CellRangeAddress(8, 8, 0, 2));
		sheet.addMergedRegion(new CellRangeAddress(8, 8, 3, 5));
		sheet.addMergedRegion(new CellRangeAddress(8, 8, 6, 8));
		
        // font áp dụng cho row header (dòng tiêu đề )
        Font fontHeader =workbook.createFont();
        fontHeader.setBold(true);
        fontHeader.setColor(IndexedColors.BLUE.getIndex());
        fontHeader.setFontHeightInPoints((short) 11);
        fontHeader.setCharSet(HSSFFont.ANSI_CHARSET);
        // style áp dụng cho row header (dòng tiêu đề )
        CellStyle cellStyle =workbook.createCellStyle();
        cellStyle.setFont(fontHeader);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setLocked(true);
        //style ap dung cho dong thuong
        Font font =workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontHeightInPoints((short) 11);
        font.setCharSet(HSSFFont.ANSI_CHARSET);
        CellStyle cellStyle2 =workbook.createCellStyle();
        cellStyle2.setFont(font);
        cellStyle2.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle2.setWrapText(true);
        // style ap dung cho total 
        CellStyle cellStyle3 =workbook.createCellStyle();
        cellStyle3.setFont(font);
        cellStyle3.setAlignment(HorizontalAlignment.RIGHT);
        // tạo mới  row header (dòng tiêu đề ) h
        Row headerRow = sheet.createRow(0);
        Cell cell = headerRow.createCell(0);
        cell.getStringCellValue().getBytes(Charset.forName("UTF-8"));
        cell.setCellValue(header);
        cell.setCellStyle(cellStyle);
        //tạo mới dòng tiêu đề thông tin
        Row headerData = sheet.createRow(1);
        int x=0;
        for(int i=0;i<cation_table.length;i++)
        {
        	Cell cellHeaderData = headerData.createCell(x);
            cellHeaderData.getStringCellValue().getBytes(Charset.forName("UTF-8"));
            cellHeaderData.setCellValue(cation_table[i]);
            cellHeaderData.setCellStyle(cellStyle);
            x+=3;
        }
        
        user_full uf=(user_full) model.get("user");
        //thông tin nhà bán hàng
        Row r2 =sheet.createRow(2);
        Cell ca1=r2.createCell(0);
        ca1.getStringCellValue().getBytes(Charset.forName("UTF-8"));
        ca1.setCellValue("Tên nhà bán hàng:Phạm Hữu Ngân");
        ca1.setCellStyle(cellStyle2);
        Row r3 =sheet.createRow(3);
        Cell ca2=r3.createCell(0);
        ca2.getStringCellValue().getBytes(Charset.forName("UTF-8"));
        ca2.setCellValue("Địa chỉ nhà bán hàng:Gia Lâm, Hà Nội");
        ca2.setCellStyle(cellStyle2);
        Row r6=sheet.createRow(6);
        Cell ca3 =r6.createCell(0);
        ca3.getStringCellValue().getBytes(Charset.forName("UTF-8"));
        ca3.setCellValue("Liên hệ:0327944481");
        ca3.setCellStyle(cellStyle2);
        //thông tin Người mua
        Cell cb1 =r2.createCell(3);
        cb1.getStringCellValue().getBytes(Charset.forName("UTF-8"));
        cb1.setCellValue("Tên người mua:"+uf.getFirstname()+" "+uf.getLastname());
        cb1.setCellStyle(cellStyle2);
        Cell cb2 =r3.createCell(3);
        cb2.getStringCellValue().getBytes(Charset.forName("UTF-8"));
        cb2.setCellValue("Liên hệ:");
        cb2.setCellStyle(cellStyle2);
        Row r4 =sheet.createRow(4);
        Row r5= sheet.createRow(5);
        Cell cb3=r4.createCell(3);
        Cell cb4=r5.createCell(3);
        cb3.getStringCellValue().getBytes(Charset.forName("UTF-8"));
        cb3.setCellValue(uf.getHomephone());
        cb3.setCellStyle(cellStyle2);
        cb4.getStringCellValue().getBytes(Charset.forName("UTF-8"));
        cb4.setCellValue(uf.getMobilephone());
        cb4.setCellStyle(cellStyle2);
        //Thong tin don hang
        Cell co1 = r2.createCell(6);
        co1.getStringCellValue().getBytes(Charset.forName("UTF-8"));
        co1.setCellValue("Mã đơn hàng: "+o.getMadh());
        co1.setCellStyle(cellStyle2);
        Cell co2 = r3.createCell(6);
        co2.getStringCellValue().getBytes(Charset.forName("UTF-8"));
        co2.setCellValue("Ngày tạo đơn: "+o.getNgay_tao_don());
        co2.setCellStyle(cellStyle2);
        Cell co3 = r4.createCell(6);
        co3.getStringCellValue().getBytes(Charset.forName("UTF-8"));
        co3.setCellValue("Địa chỉ giao hàng: "+o.getAddr());
        co3.setCellStyle(cellStyle2);
        //Thông tin sản phẩm
        Row r7 = sheet.createRow(7);
        Cell cp1 = r7.createCell(0);
        cp1.getStringCellValue().getBytes(Charset.forName("UTF-8"));
        cp1.setCellValue("THÔNG TIN SẢN PHẨM");
        cp1.setCellStyle(cellStyle);
        Row r8=sheet.createRow(8);
        x=0;
        for(int i=0;i<header_table_order.length;i++)
        {
        	Cell chp = r8.createCell(x);
        	chp.getStringCellValue().getBytes(Charset.forName("UTF-8"));
        	chp.setCellValue(header_table_order[i]);
        	chp.setCellStyle(cellStyle);
        	x+=3;
        }
        int r=8;int tonggia=0;r++;
        for(Product p: o.getList())
        {
        	
        	//merge
        	sheet.addMergedRegion(new CellRangeAddress(r, r+1, 0	, 2));
        	sheet.addMergedRegion(new CellRangeAddress(r, r+1, 3	, 5));
        	sheet.addMergedRegion(new CellRangeAddress(r, r+1, 6	, 8));
        	int z=Integer.parseInt(p.getPrice())*p.getNumOfProduct();
        	Row rp =sheet.createRow(r);
        	Cell cpd1 = rp.createCell(0);
        	cpd1.getStringCellValue().getBytes(Charset.forName("UTF-8"));
        	cpd1.setCellValue(p.getName());
        	cpd1.setCellStyle(cellStyle2);
        	Cell cpd2 = rp.createCell(3);
        	cpd2.getStringCellValue().getBytes(Charset.forName("UTF-8"));
        	cpd2.setCellValue(p.getPrice()+" x "+p.getNumOfProduct());
        	cpd2.setCellStyle(cellStyle2);
        	Cell cpd3 = rp.createCell(6);
        	cpd3.getStringCellValue().getBytes(Charset.forName("UTF-8"));
        	cpd3.setCellValue(z);
        	cpd3.setCellStyle(cellStyle2);
        	tonggia +=z; 
        	r+=2;
        }
        //tong gia
        Row rowTotal= sheet.createRow(++r);
        Cell celltotal= rowTotal.createCell(0);
        celltotal.getStringCellValue().getBytes(Charset.forName("UTF-8"));
        celltotal.setCellValue("Giá trị đơn hàng:"+tonggia);
        celltotal.setCellStyle(cellStyle3);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 8));
        //ngay thang
        Row rowd= sheet.createRow(++r);
        Cell celld= rowd.createCell(0);
        celld.getStringCellValue().getBytes(Charset.forName("UTF-8"));
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        celld.setCellValue("Hà Nội, "+formatter.format(new Date()));
        celld.setCellStyle(cellStyle3);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 8));
        //end!!!
        for(int i=0;i<=8;i++)
        {
        	sheet.autoSizeColumn(i, true);
        }
        try {  // gửi kèm file thông qua response
            response.setHeader("Content-Disposition", "attachement; filename=\""
                    + java.net.URLEncoder.encode(name, "UTF-8") + "\";charset=\"UTF-8\"");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
	}

}
