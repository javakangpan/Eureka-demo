package demo.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import demo.repository.LogRepository;
import demo.test.logging.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/logs")
@Controller
public class LogController {

    @Autowired
    private LogRepository logRepository;


    @RequestMapping(value = "/download",method = RequestMethod.GET)
    public void download(HttpServletResponse response) throws IOException {
        downloadExcel(response);
    }

    public void downloadExcel(HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Logs> logs = logRepository.findAll();
        for (Logs log : logs) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("IP", log.getRequestIp());
            map.put("描述", log.getDescription());
            map.put("浏览器", log.getBrowser());
            map.put("请求耗时/毫秒", log.getTime());
            map.put("异常详情", log.getExceptionDetail()==null ? "" : log.getExceptionDetail());
            map.put("创建日期", log.getCreateTime());
            map.put("方法名称",log.getMethod());
            map.put("参数列表",log.getParams());
            map.put("返回值",log.getReturnValue()==null ? "" :log.getReturnValue());
            list.add(map);
            String tempPath =System.getProperty("java.io.tmpdir") + fastSimpleUUID() + ".xlsx";
            File file = new File(tempPath);
            BigExcelWriter writer= ExcelUtil.getBigWriter(file);
            // 一次性写出内容，使用默认样式，强制输出标题
            writer.write(list, true);
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition","attachment;filename=file.xlsx");
            ServletOutputStream out=response.getOutputStream();
            // 终止后删除临时文件
            file.deleteOnExit();
            writer.flush(out, true);
            if(out != null) {
                out.close();
            }
        }
    }

    public  String fastSimpleUUID() {
        return UUID.fastUUID().toString(true);
    }
}
