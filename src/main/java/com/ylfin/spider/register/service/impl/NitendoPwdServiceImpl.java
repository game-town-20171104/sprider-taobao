package com.ylfin.spider.register.service.impl;

import com.ylfin.spider.register.service.NitendoPwdService;
import com.ylfin.spider.register.vo.NintendoPwdVO;
import com.ylfin.spider.register.vo.bean.NintendoBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: godslhand
 * @date: 2018/8/14
 * @description:
 */
@Slf4j
@Service
public class NitendoPwdServiceImpl implements NitendoPwdService {
    @Override
    public List<NintendoPwdVO> getExcelList(String file) {
        List<NintendoPwdVO> list = new ArrayList<>();
        try (InputStream inp = new FileInputStream(file)) {
            //InputStream inp = new FileInputStream("workbook.xlsx");

            Workbook wb = WorkbookFactory.create(inp);
            Sheet sheet = wb.getSheetAt(0);
            for (Row row : sheet) {
                Cell account = row.getCell(8);
                Cell pwd = row.getCell(9);
                if (account == null || pwd == null) {
                    log.warn("此行空，跳过");
                    continue;
                }
                String accountVal = account.getStringCellValue();
                String pwdVal = pwd.getStringCellValue();
                if ("账号".equals(accountVal) || StringUtils.isBlank(accountVal) || StringUtils.isBlank(pwdVal)) {
                    continue;
                }
                NintendoPwdVO vo = new NintendoPwdVO();
                vo.setAccount(accountVal);
                vo.setPassword(pwdVal);
                vo.setPath(file);
                vo.setRowIndex(row.getRowNum());
                list.add(vo);

            }
            wb.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void save2Excel(NintendoPwdVO nintendoPwdVO) {

        try (InputStream inp = new FileInputStream(nintendoPwdVO.getPath())) {
            //InputStream inp = new FileInputStream("workbook.xlsx");

            Workbook wb = WorkbookFactory.create(inp);
            Sheet sheet = wb.getSheetAt(0);
            Row row = sheet.getRow(nintendoPwdVO.getRowIndex());
            Cell cell = row.getCell(1);
            if(cell!=null){
                System.out.println(nintendoPwdVO+"->原cell有如下数据："+cell.getStringCellValue());
            }else {
                cell =  row.createCell(16);
            }
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(nintendoPwdVO.getNewPwd());

            // Write the output to a file
            try (OutputStream fileOut = new FileOutputStream(nintendoPwdVO.getPath())) {
                wb.write(fileOut);
            }
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        NitendoPwdServiceImpl nitendoPwdService = new NitendoPwdServiceImpl();
       List<NintendoPwdVO> vos = nitendoPwdService.getExcelList("D:/Switch游戏账号列表.xlsx");
        System.out.println(vos);
       NintendoPwdVO v = vos.get(0);
       v.setNewPwd("xxxx");
       nitendoPwdService.save2Excel(v);
    }
}
