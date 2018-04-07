package com.stalary.usercenter.service;

import com.stalary.usercenter.data.entity.Log;
import com.stalary.usercenter.repo.BaseRepo;
import com.stalary.usercenter.repo.LogRepo;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * LogService
 *
 * @author lirongqian
 * @since 2018/03/25
 */
@Service
public class LogService extends BaseService<Log, LogRepo> {


    @Autowired
    public LogService(LogRepo repo) {
        super(repo);
    }

    public Log findOldLog(Long commonId, String type) {
        return repo.findByCommonIdAndType(commonId, type);
    }
}