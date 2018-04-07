package com.stalary.usercenter.repo;

import com.stalary.usercenter.data.entity.Log;

/**
 * @author Stalary
 * @description
 * @date 2018/3/25
 */
public interface LogRepo extends BaseRepo<Log, Long> {

    Log findByCommonIdAndType(Long commonId, String type);
}
