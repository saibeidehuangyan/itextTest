package com.macro.mall.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class ItextTest {
    public static void main(String[] args) throws Exception {
        //1、建立Document对象的实例。
        Document document = createDocumentByRectangle();
        //2、建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File("D:\\test.pdf")));
        writer.setPageEvent(new MyHeaderFooter());// 页眉/页脚
        writer.setPageEvent(new Watermark("创建文件时水印"));//水印，所有页面
        //3、打开
        document.open();
        //添加文件属性
        AddProperties(document);
        //4、添加内容
        AddPDFContent(document,writer);
        //5、关闭
        document.close();


        //对已有文件操作
        String srcFile="D:\\test.pdf";
        String destFile="D:\\testDest.pdf";
        String text="绝 密 文 件";
        String imagePath="D:\\test.jpg";
        addWaterMarkAfter(srcFile,destFile,text,imagePath);
    }



    /**
     * 设置全局字体、字号
     */
    private static BaseFont baseFont;
    private static Font font20;
    private static Font font18;
    private static Font font14;
    private static Font font12;
    private static Font font14Under;
    static {
        try {
            //使用iTextAsian.jar中的字体、并设置编码
            baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            font20 = new Font(baseFont, 20,Font.NORMAL);
            font18 = new Font(baseFont, 18,Font.NORMAL);
            font14 = new Font(baseFont, 14,Font.NORMAL);
            font12 = new Font(baseFont, 12,Font.NORMAL);
            font14Under = new Font(baseFont, 14, Font.UNDERLINE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 1、创建document
     * @return
     */
    public static Document createDocumentByRectangle() {
        //设置页面尺寸
//        Rectangle rectangle = new Rectangle(PageSize.A4.rotate());//横打
        Rectangle rectangle = new Rectangle(PageSize.A4);
        //设置背景色
//        rectangle.setBackgroundColor(BaseColor.WHITE);//会影响水印的显示
        Document document = new Document(rectangle);
        //设置页边距
        document.setMargins(40,40,40,40);
        return document;
    }

    /**
     * 添加文件属性
     * @param document
     */
    private static void AddProperties(Document document) {
        //设置文件属性
        document.addTitle("Title@PDF-Java");// 标题
        document.addAuthor("Author@umiz");// 作者
        document.addSubject("Subject@iText pdf sample");// 主题
        document.addKeywords("Keywords@iTextpdf");// 关键字
        document.addCreator("Creator@umiz`s");// 创建者
    }


    /**
     * 添加PDF文件内容
     * @param document
     * @throws DocumentException
     */
    private static void AddPDFContent(Document document,PdfWriter writer) throws DocumentException {
        //标题
        Paragraph title = new Paragraph("长发飘飘", font18);
        title.setAlignment(Element.ALIGN_CENTER);//居中
        document.add(title);
        //空行
        Paragraph blankLine = new Paragraph("\n", font14);
        document.add(blankLine);
        //段落内容
        Paragraph ph1 = new Paragraph("我的小鱼你醒了，" +
                "还认识早晨吗？" +
                "昨夜你曾经说，" +
                "愿夜幕永不开启。" +
                "你的香腮边轻轻滑落的，" +
                "是你的泪，还是我的泪？" +
                "初吻吻别的那个季节，" +
                "不是已经哭过了吗？" +
                "我的指尖还记忆着，" +
                "你慌乱的回心跳。" +
                "温柔的体香里，" +
                "那一缕长发飘飘......", font14);
        ph1.setLeading(25);//设置段落内行间距，也可使用构造器设置
        ph1.setFirstLineIndent(30);//首行缩进
        document.add(ph1);
        //添加Page，第二页
        document.newPage();
        document.add(new Paragraph("第二页 样式测试",font14));
        //实线
        Paragraph p1 = new Paragraph("左",font14);
        p1.add(new Chunk(new LineSeparator()));
        p1.add("右");
        document.add(p1);
        //点线
        Paragraph p2 = new Paragraph("左",font14);
        p2.add(new Chunk(new DottedLineSeparator()));
        p2.add("右");
        document.add(p2);
        //下划线
        Paragraph p3 = new Paragraph("潇洒来去山水间，谈笑声中江湖远； 码到功成人未老，白发归来仍少年....潇洒来去山水间，谈笑声中江湖远； 码到功成人未老，白发归来仍少年....",font14Under);
        document.add(p3);
        //第三页
        document.newPage();
        document.add(new Paragraph("第三页 表格测试",font14));
        document.add(new Paragraph("\n",font14));
        //创建表格
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setTotalWidth(500);
        float[] widths = {20, 15, 15, 15, 15, 20};//百分比
        table.setWidths(widths);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);//表格整体 水平居左
        //设置标题
        PdfPCell cell = new PdfPCell();
        cell.setColspan(6);//必须设置跨列
        cell.setMinimumHeight(30);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);//水平居中
        cell.setUseAscender(true);//垂直居中
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);//垂直居中
        Paragraph paragraph = new Paragraph("考试成绩单", font14);
        cell.setPhrase(paragraph);
        table.addCell(cell);
        //第一列
        cell = new PdfPCell();
        cell.setColspan(1);//设置跨列，所有列数总和必须是行列数的整数倍
        cell.setMinimumHeight(50);
        paragraph=new Paragraph("               科目\n\n   姓名     成绩",font14);
        cell.setPhrase(paragraph);
        table.addCell(cell);
        //单元格画斜线
        PdfContentByte cb = writer.getDirectContent();
        float xFrom=40;
        float yFrom=document.getPageSize().getHeight()-112;
        float xTo=100;
        float yTo=yFrom - 50;//加行高
        drawLine(cb, xFrom, yFrom, xTo, yTo);
        xTo=xFrom + 500*widths[0]/100;//加第一列宽
        yTo=yTo+20;
        drawLine(cb, xFrom,yFrom, xTo, yTo);
        //第二列
        cell = new PdfPCell();
        cell.setColspan(1);
        cell.setMinimumHeight(50);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);//水平居中
        cell.setUseAscender(true);//垂直居中
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);//垂直居中
        paragraph=new Paragraph("语文",font14);
        cell.setPhrase(paragraph);
        table.addCell(cell);
        //第三列
        cell = new PdfPCell();
        cell.setColspan(1);
        cell.setMinimumHeight(50);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);//水平居中
        cell.setUseAscender(true);//垂直居中
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);//垂直居中
        paragraph=new Paragraph("数学",font14);
        cell.setPhrase(paragraph);
        table.addCell(cell);
        //第四列
        cell = new PdfPCell();
        cell.setColspan(1);
        cell.setMinimumHeight(50);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);//水平居中
        cell.setUseAscender(true);//垂直居中
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);//垂直居中
        paragraph=new Paragraph("英语",font14);
        cell.setPhrase(paragraph);
        table.addCell(cell);
        //第五列
        cell = new PdfPCell();
        cell.setColspan(1);
        cell.setMinimumHeight(50);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);//水平居中
        cell.setUseAscender(true);//垂直居中
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);//垂直居中
        paragraph=new Paragraph("综合",font14);
        cell.setPhrase(paragraph);
        table.addCell(cell);
        //第六列
        cell = new PdfPCell();
        cell.setColspan(1);
        cell.setMinimumHeight(50);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);//水平居中
        cell.setUseAscender(true);//垂直居中
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);//垂直居中
        paragraph=new Paragraph("合计",font14);
        cell.setPhrase(paragraph);
        table.addCell(cell);
        //加数据
        addCellData(table, "张三", 30);
        addCellData(table, "130", 30);
        addCellData(table, "130", 30);
        addCellData(table, "130", 30);
        addCellData(table, "300", 30);
        addCellData(table, "690", 30);

        //添加条形码
        addCellData(table, "测试条形码", 40);
        String barcodenfo="202005310001";
        boolean hidnCode=false;
        Image barcodeImage = createBarcode(writer, barcodenfo, hidnCode);
        cell = new PdfPCell(barcodeImage ,true);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);//居左
        cell.setUseAscender(true);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setFixedHeight(60);//同一行高度不同时，高度由大的决定
        table.addCell(cell);

        //添加二维码
        addCellData(table, "测试二维码", 50);
        String QRCodeInfo="https://blog.csdn.net/saibeidehuangyan/article/details/106447750";
        Image qrCodeImage = createQRCode(writer,QRCodeInfo);
        cell = new PdfPCell(qrCodeImage ,true);//是否自适应
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);//水平居中
        cell.setUseAscender(true);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setFixedHeight(200);//自适应时，不设置固定高度，高度由图片大小决定
        table.addCell(cell);

        document.add(table);
    }


    /**
     * 表格划线设置表头
     * @param cb
     * @param xFrom
     * @param yFrom
     * @param xTo
     * @param yTo
     */
    public static void drawLine(PdfContentByte cb, float xFrom, float yFrom, float xTo, float yTo) {
        cb.saveState();
        cb.moveTo(xFrom,yFrom);//起点
        cb.lineTo(xTo,yTo);//终点
        cb.stroke();
        cb.restoreState();
    }

    /**
     * 添加表格数据
     * @param table
     * @param cont
     * @param i
     */
    private static void addCellData(PdfPTable table, String cont, int i) {
        PdfPCell cell1 = new PdfPCell();
        cell1.setColspan(1);
        cell1.setMinimumHeight(i);
        cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);//水平居中
        cell1.setUseAscender(true);//垂直居中
        cell1.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);//垂直居中
        Paragraph paragraph1 = new Paragraph(cont, font14);
        cell1.setPhrase(paragraph1);
        table.addCell(cell1);
    }


    /**
     * 创建条形码
     *
     * @param writer
     * @param barcodenfo  条形码(数字)
     * @param hidnCode 是否隐藏条形码(数字)
     * @return
     */
    public static Image createBarcode(PdfWriter writer, String barcodenfo, Boolean hidnCode){
        Barcode128 barcode128 = new Barcode128();
        barcode128.setCode(barcodenfo);//设置数字
        barcode128.setCodeType(Barcode128.CODE128);
        if (hidnCode) barcode128.setFont(null);//设置字体
        return barcode128.createImageWithBarcode(writer.getDirectContent(),null,null);
    }

    /**
     * 生成二维码
     * @param writer
     * @param QRCodenfo
     * @return
     * @throws BadElementException
     */
    public static Image createQRCode(PdfWriter writer, String QRCodenfo) throws BadElementException {
        BarcodeQRCode qrCode = new BarcodeQRCode(QRCodenfo, 60, 60, null);
        return qrCode.getImage();
    }



    /**
     * 内部类
     * 添加页眉、页脚
     */
    public static class MyHeaderFooter extends PdfPageEventHelper {
        // 总页数
        PdfTemplate totalPage;
        Font font12=ItextTest.font12;
        // 打开文档时，创建一个总页数的模版
        public void onOpenDocument(PdfWriter writer, Document document) {
            PdfContentByte cb =writer.getDirectContent();
            totalPage = cb.createTemplate(30, 16);
        }
        // 一页加载完成触发，写入页眉和页脚
        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable table = new PdfPTable(3);
            try {
                table.setTotalWidth(PageSize.A4.getWidth() - 80);
                table.setWidths(new int[] { 24, 24, 3});
                table.setLockedWidth(true);
                table.getDefaultCell().setFixedHeight(-10);
                table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                table.getDefaultCell().setBorderWidth(0.5f);

                table.addCell(new Paragraph("这是页眉/页脚", font12));// 可以直接使用addCell(str)，不过不能指定字体，中文无法显示
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(new Paragraph("第" + writer.getPageNumber() + "页/", font12));
                // 总页数
                PdfPCell cell = new PdfPCell(Image.getInstance(totalPage));
                cell.setBorder(Rectangle.BOTTOM);
                table.addCell(cell);
                // 将页眉写到document中，位置可以指定，指定到下面就是页脚
                table.writeSelectedRows(0, -1, 40, PageSize.A4.getHeight() - 20, writer.getDirectContent());
                table.writeSelectedRows(0, -1, 40, 40, writer.getDirectContent());
            } catch (Exception de) {
                throw new ExceptionConverter(de);
            }
        }
        // 全部完成后，将总页数的pdf模版写到指定位置
        public void onCloseDocument(PdfWriter writer, Document document) {
            String text = "总" + (writer.getPageNumber()) + "页";
            ColumnText.showTextAligned(totalPage, Element.ALIGN_LEFT, new Paragraph(text,font12), 2, 2, 0);
        }
    }

    /**
     * 内部类
     * 创建文件时添加水印
     */
    public static class Watermark extends PdfPageEventHelper {
        Font font20=ItextTest.font20;
        private String waterContent;//水印内容

        public Watermark() {
        }

        public Watermark(String waterCont) {
            this.waterContent = waterCont;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte contentUnder = writer.getDirectContentUnder();
            contentUnder.setFontAndSize(baseFont,60);//设置水印字体字号
            PdfGState gState = new PdfGState();//设置透明度
            gState.setFillOpacity(0.4f);//填充字体透明度 0.4
            contentUnder.setColorFill(BaseColor.PINK);//水印颜色
            contentUnder.setGState(gState);//设置水印透明度
            for(int i=0 ; i<5; i++) {
                for(int j=0; j<5; j++) {
                    ColumnText.showTextAligned(
                            contentUnder,
                            Element.ALIGN_CENTER,
                            new Phrase(this.waterContent == null ? "WHAT？" : this.waterContent, font20),
                            (100+i*350),
                            (100+j*150),
                            writer.getPageNumber() % 2 == 1 ? 45 : -45);
                }
            }
        }
    }


    /**
     * 对已有文件添加文本、图片水印
     * @param srcFile 原文件路径
     * @param destFile 生成文件路径
     * @param text 水印文本
     */
    public static void addWaterMarkAfter(String srcFile,String destFile,String text,String imagePath) throws Exception {
        //读取原文件
        PdfReader reader = new PdfReader(srcFile);
        //存入目标文件
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destFile));
        //加密文件，需另引jar
        stamper.setEncryption("123".getBytes(), "456".getBytes(),  PdfWriter.ALLOW_COPY, PdfWriter.ENCRYPTION_AES_128);
        //总页数
        int total = reader.getNumberOfPages();
        //文件内容
        PdfContentByte content;
        //设置透明度
        PdfGState gs = new PdfGState();
        gs.setFillOpacity(0.1f);
        //遍历每页，添加水印
        for (int i=1;i<=total;i++){
            //获取每页内容
            content = stamper.getOverContent(i);//从 1 开始
            content.beginText();
            //透明度
            content.setGState(gs);
            if (i==2)continue;
            //字体字号
            content.setFontAndSize(baseFont,60);
            //水印颜色
            content.setColorFill(BaseColor.GREEN);
            //水印文本设置
            content.showTextAligned(Element.ALIGN_MIDDLE,text,60,540,45);//原点在左下角
            content.showTextAligned(Element.ALIGN_MIDDLE, UUID.randomUUID().toString(),60,280,45);//UUID
            content.endText();
        }
        //添加背景图
        Image image = Image.getInstance(imagePath);
        image.scaleToFit(PageSize.A4);
        image.setAbsolutePosition(0,0);
        stamper.getOverContent(2).addImage(image);
        //关闭
        stamper.close();
        reader.close();
    }

}


