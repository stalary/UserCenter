package com.stalary.usercenter.repo;

import com.stalary.usercenter.data.entity.Log;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Stalary
 * @description
 * @date 2018/3/25
 */
public interface LogRepo extends BaseRepo<Log, Long> {

    Log findByCommonIdAndTypeAndContent(Long commonId, String type, String content);

    @Query("select l from Log l where l.type=?1 and l.commonId in ?2 order by l.updateTime desc")
    List<Log> findLogByProject(String type, List<Long> id);
}
