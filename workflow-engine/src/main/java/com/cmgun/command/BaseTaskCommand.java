package com.cmgun.command;

import com.cmgun.entity.TaskContext;
import com.cmgun.repository.BaseMsgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author chenqilin
 * @Date 2019/7/11
 */
@Component
public class BaseTaskCommand implements TaskCommand {

    @Autowired
    private BaseMsgRepository baseMsgRepository;

    @Override
    public void execute(TaskContext context) {
        // update one row
        baseMsgRepository.updateTime(1, new Date());
        // throw runtime exception
//        throw new RuntimeException("mock exception");
    }
}
