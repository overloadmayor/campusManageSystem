package com.campus.userservice.listener;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.campus.common.constants.UserConstants;
import com.campus.model.user.pojos.BatchNotification;
import com.campus.model.user.pojos.SendMQPojos;
import com.campus.model.user.pojos.StudentExcel;
import com.campus.model.user.pojos.Students;
import com.campus.userservice.service.IStudentsService;
import com.campus.utils.thread.UserThreadLocalUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BatchDataListener extends AnalysisEventListener<Students> {

    private Map<String, Long> majorMap;
    private Map<String, Long> deptMap;
    private Map<Long, Long> majorToDept;
    private List<Integer> failRows = new ArrayList<>();
    private List<Students> studentsList = new ArrayList<>();

//    @Override
//    public boolean hasNext(AnalysisContext context) {
//        return true;
//    }

    //    private long time;
//    private String fileOriginName;
    private SendMQPojos pojo;
    private IStudentsService studentsService;
    private StringRedisTemplate stringRedisTemplate;
    public BatchDataListener(Map<String, Long> majorMap, Map<String, Long> deptMap, Map<Long,
            Long> majorToDept,  IStudentsService studentsService,
                             StringRedisTemplate stringRedisTemplate,SendMQPojos pojo
            ) {
        this.majorMap = majorMap;
        this.deptMap = deptMap;
        this.majorToDept = majorToDept;
        this.studentsService = studentsService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.pojo = pojo;
    }

    @Override
    public void invoke(Students record, AnalysisContext context) {
        Integer rowIndex = context.readRowHolder().getRowIndex() + 1;

        Long majorId = majorMap.get(record.getMajorName());
        Long deptId = deptMap.get(record.getDeptName());
        if (majorId == null || deptId == null || !deptId.equals(majorToDept.get(majorId))) {
            failRows.add(rowIndex);
            return;
        }
        record.setDept(deptId);
        record.setMajor(majorId);
        record.setPassword(DigestUtils.md5DigestAsHex(UserConstants.DEFAULT_PASSWORD.getBytes()));
        studentsList.add(record);
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        Integer rowIndex = context.readRowHolder().getRowIndex() + 1;
        failRows.add(rowIndex);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        studentsService.saveBatch(studentsList);
        BatchNotification batchNotification = new BatchNotification();
        batchNotification.setTime(pojo.getTime());
        batchNotification.setFileOriginName(pojo.getFileOriginName());
        if (failRows == null || failRows.isEmpty()) {
            batchNotification.setStatus(1);
        } else {
            batchNotification.setStatus(2);
            batchNotification.setRows(failRows);
        }
        stringRedisTemplate.opsForHash().put(UserConstants.Admin_UPLOAD_NOTIFICATION+pojo.getUserId(),
                String.valueOf(pojo.getTime()), JSON.toJSONString(batchNotification));
    }

}