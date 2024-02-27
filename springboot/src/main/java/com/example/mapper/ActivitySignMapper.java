package com.example.mapper;

import com.example.entity.ActivitySign;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作activitySignSign相关数据接口
*/
public interface ActivitySignMapper {

    /**
      * 新增
    */
    int insert(ActivitySign activitySign);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(ActivitySign activitySign);

    /**
      * 根据ID查询
    */
    ActivitySign selectById(Integer id);

    /**
      * 查询所有
    */
    List<ActivitySign> selectAll(ActivitySign activitySign);
    /**
       * 根据活动id和用户id查询
     */
    ActivitySign selectByActivityIdAndUserId(@Param("activityId") Integer activityId, @Param("userId") Integer userId);

    /**
     * 根据activityId删除
     * @param activityId
     */
    void deleteByActivityId(Integer activityId);
}