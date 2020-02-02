package com.rajorpay.hex.nmtoolmaster.Utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.rajorpay.hex.nmtoolmaster.Models.Customer;
import com.rajorpay.hex.nmtoolmaster.R;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExcelUtil {
    public void exportUserInfoToExcel(Context context, HashMap<String, List<Customer>> customerAreaMap){
        Workbook workbook =new HSSFWorkbook();

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillBackgroundColor(HSSFColor.GREEN.index);

        //Now create table cells and columns..
        Sheet sheet = workbook.createSheet("Cus Info");

        int i = 0, j = 0;
        Cell cell = null;
        Row row = null;

        Iterator it = customerAreaMap.entrySet().iterator();
        while(it.hasNext()){
            j = 0;
            Map.Entry pair = (Map.Entry)it.next();
            sheet.setColumnWidth(j, 10*400);
            row = sheet.createRow(i++);
            cell = row.createCell(j++);
            cell.setCellStyle(headerStyle);
            cell.setCellValue((String)pair.getKey());
            j = 0;
            //setting headers for each area..
            row = sheet.createRow(i++);
            sheet.setColumnWidth(j, 10*400);
            cell = row.createCell(j++);
            cell.setCellValue("STB Number");
            cell.setCellStyle(cellStyle);
            sheet.setColumnWidth(j, 10*400);
            cell = row.createCell(j++);
            cell.setCellValue("Name");
            cell.setCellStyle(cellStyle);
            sheet.setColumnWidth(j, 10*300);
            cell = row.createCell(j++);
            cell.setCellValue("Phone Number");
            cell.setCellStyle(cellStyle);
            sheet.setColumnWidth(j, 10*200);
            cell = row.createCell(j++);
            cell.setCellValue("Box Type");
            cell.setCellStyle(cellStyle);
            sheet.setColumnWidth(j, 10*200);
            cell = row.createCell(j++);
            cell.setCellValue("Package");
            cell.setCellStyle(cellStyle);
            sheet.setColumnWidth(j, 10*500);
            cell = row.createCell(j++);
            cell.setCellValue("Address");
            cell.setCellStyle(cellStyle);
            sheet.setColumnWidth(j, 10*250);
            cell = row.createCell(j++);
            cell.setCellValue("Locality");
            cell.setCellStyle(cellStyle);
            sheet.setColumnWidth(j, 10*200);
            cell = row.createCell(j++);
            cell.setCellValue("Payment Status");
            cell.setCellStyle(cellStyle);
            sheet.setColumnWidth(j, 10*250);
            cell = row.createCell(j++);
            cell.setCellValue("PaidTill");
            cell.setCellStyle(cellStyle);

            //Value entry
            for(Customer customer : (List<Customer>) pair.getValue()){
                j=0;
                row =  sheet.createRow(i++);
                cell = row.createCell(j++);
                cell.setCellValue(customer.getBoxNumber());
                cell = row.createCell(j++);
                cell.setCellValue(customer.getName());
                cell = row.createCell(j++);
                cell.setCellValue(customer.getPhoneNumber());
                cell = row.createCell(j++);
                cell.setCellValue(customer.getBoxType());
                Log.d("NM_TOOLstr","value of j :"+j+"and i : "+i);
                cell = row.createCell(j++);
                cell.setCellValue(context.getResources().getString(R.string.Rs) +" "+customer.getPackageAmount());
                cell = row.createCell(j++);
                cell.setCellValue(customer.getAddress());
                cell = row.createCell(j++);
                cell.setCellValue(customer.getLocality());
                cell = row.createCell(j++);
                cell.setCellValue(customer.getPaymentStatus());
                cell = row.createCell(j++);
                cell.setCellValue(customer.getPaidTill());
            }
            row = sheet.createRow(i++);
        }



        Log.d("NM_TOOLstr","File generation about to be in progress");
        File file = new File(Environment.getExternalStorageDirectory().toString(), "Customer_report.xls");
        if(file != null){
            String path = file.getPath();
            Toast.makeText(context,path, Toast.LENGTH_SHORT).show();
        }
        FileOutputStream outputStream = null;
        try {
                outputStream = new FileOutputStream(file);
                workbook.write(outputStream);
                Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
                outputStream.close();

            }catch(Exception e){
                Toast.makeText(context, "NOT OK", Toast.LENGTH_SHORT).show();
            }

    }
}
