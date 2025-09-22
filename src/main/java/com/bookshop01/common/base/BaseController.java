package com.bookshop01.common.base;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop01.goods.vo.ImageFileVO;

@Controller
public abstract class BaseController {

    private static final String CURR_IMAGE_REPO_PATH = "C:\\shopping\\file_repo";

    protected List<ImageFileVO> upload(MultipartHttpServletRequest multipartRequest) throws Exception {
        List<ImageFileVO> fileList = new ArrayList<>();
        Iterator<String> fileNames = multipartRequest.getFileNames();

        while (fileNames.hasNext()) {
            ImageFileVO imageFileVO = new ImageFileVO();
            String fileName = fileNames.next();
            imageFileVO.setFileType(fileName);
            MultipartFile mFile = multipartRequest.getFile(fileName);
            String originalFileName = mFile.getOriginalFilename();
            imageFileVO.setFileName(originalFileName);
            fileList.add(imageFileVO);

            File file = new File(CURR_IMAGE_REPO_PATH + File.separator + fileName);
            if (mFile.getSize() != 0) {
                if (!file.exists()) {
                    if (file.getParentFile().mkdirs()) {
                        file.createNewFile();
                    }
                }
                mFile.transferTo(new File(CURR_IMAGE_REPO_PATH + File.separator + "temp" + File.separator + originalFileName));
            }
        }
        return fileList;
    }

    protected void deleteFile(String fileName) {
        File file = new File(CURR_IMAGE_REPO_PATH + File.separator + fileName);
        try {
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/*.do", method = {RequestMethod.GET, RequestMethod.POST})
    protected ModelAndView viewForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String viewName = (String) request.getAttribute("viewName");
        return new ModelAndView(viewName);
    }

    protected String calcSearchPeriod(String fixedSearchPeriod) {
        String beginDate;
        String endDate;

        DecimalFormat df = new DecimalFormat("00");
        Calendar cal = Calendar.getInstance();

        String endYear = Integer.toString(cal.get(Calendar.YEAR));
        String endMonth = df.format(cal.get(Calendar.MONTH) + 1);
        String endDay = df.format(cal.get(Calendar.DATE));
        endDate = endYear + "-" + endMonth + "-" + endDay;

        if (fixedSearchPeriod == null) {
            cal.add(Calendar.MONTH, -4);
        } else {
            switch (fixedSearchPeriod) {
                case "one_week":
                    cal.add(Calendar.DAY_OF_YEAR, -7);
                    break;
                case "two_week":
                    cal.add(Calendar.DAY_OF_YEAR, -14);
                    break;
                case "one_month":
                    cal.add(Calendar.MONTH, -1);
                    break;
                case "two_month":
                    cal.add(Calendar.MONTH, -2);
                    break;
                case "three_month":
                    cal.add(Calendar.MONTH, -3);
                    break;
                case "four_month":
                    cal.add(Calendar.MONTH, -4);
                    break;
                default:
                    cal.add(Calendar.MONTH, -4);
            }
        }

        String beginYear = Integer.toString(cal.get(Calendar.YEAR));
        String beginMonth = df.format(cal.get(Calendar.MONTH) + 1);
        String beginDay = df.format(cal.get(Calendar.DATE));
        beginDate = beginYear + "-" + beginMonth + "-" + beginDay;

        return beginDate + "," + endDate;
    }
}
