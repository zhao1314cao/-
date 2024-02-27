package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.LikesModuleEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.mapper.ActivityMapper;
import com.example.mapper.ActivitySignMapper;
import com.example.mapper.CollectMapper;
import com.example.mapper.LikesMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动业务处理
 **/
@Service
public class ActivityService {

    @Resource
    private ActivityMapper activityMapper;
    @Resource
    private ActivitySignMapper activitySignMapper;
    @Resource
    private LikesMapper likesMapper;
    @Resource
    private CollectMapper collectMapper;

    /**
     * 新增
     */
    public void add(Activity activity) {
        activityMapper.insert(activity);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        activityMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            activityMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Activity activity) {
        activityMapper.updateById(activity);
    }

    /**
     * 根据ID查询
     */
    public Activity selectById(Integer id) {
        Activity activity = activityMapper.selectById(id);
        this.setActivity(activity,TokenUtils.getCurrentUser());
        //活动收藏量
        int collectCount = collectMapper.selectByFidAndModule(id, LikesModuleEnum.ACTIVITY.getValue());
        //活动点赞量
        int likeCount = likesMapper.selectByFidAndModule(id, LikesModuleEnum.ACTIVITY.getValue());
        activity.setCollectCount(collectCount);
        activity.setLikesCount(likeCount);

        //查询当前用户是否给当前活动点了赞
        Likes likes = new Likes();
        likes.setFid(id);
        likes.setUserId(TokenUtils.getCurrentUser().getId());
        likes.setModule(LikesModuleEnum.ACTIVITY.getValue());
        Likes likes1 = likesMapper.selectUserLikes(likes);
        activity.setIsLike(likes1!=null);

        //查询当前用户是否收藏了当前活动
        Collect collect = new Collect();
        collect.setModule(LikesModuleEnum.ACTIVITY.getValue());
        collect.setUserId(TokenUtils.getCurrentUser().getId());
        collect.setFid(id);
        Collect collect1 = collectMapper.selectUserCollect(collect);
        activity.setIsCollect(collect1!=null);
        return activity;
    }

    /**
     * 查询所有
     */
    public List<Activity> selectAll(Activity activity) {
        return activityMapper.selectAll(activity);
    }

    /**
     * 分页查询
     */
    public PageInfo<Activity> selectPage(Activity activity, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = activityMapper.selectAll(activity);
        PageInfo<Activity> pageInfo = PageInfo.of(list);
        List<Activity> activityList = pageInfo.getList();
        Account currentUser = TokenUtils.getCurrentUser();
        for (Activity act : activityList) {
            this.setActivity(act,currentUser);
        }
        return pageInfo;
    }
    public PageInfo<Activity> selectLike(Activity activity, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            activity.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = activityMapper.selectLike(activity);
        PageInfo<Activity> pageInfo = PageInfo.of(list);
        List<Activity> activityList = pageInfo.getList();
        for (Activity act : activityList) {
            this.setActivity(act, currentUser);
        }
        return pageInfo;
    }

    public PageInfo<Activity> selectCollect(Activity activity, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            activity.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = activityMapper.selectCollect(activity);
        PageInfo<Activity> pageInfo = PageInfo.of(list);
        List<Activity> activityList = pageInfo.getList();
        for (Activity act : activityList) {
            this.setActivity(act, currentUser);
        }
        return pageInfo;
    }

    public PageInfo<Activity> selectComment(Activity activity, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            activity.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = activityMapper.selectComment(activity);
        PageInfo<Activity> pageInfo = PageInfo.of(list);
        List<Activity> activityList = pageInfo.getList();
        for (Activity act : activityList) {
            this.setActivity(act, currentUser);
        }
        return pageInfo;
    }

    /** 查询出用户报名的活动列表
     * @param activity
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Activity> selectUser(Activity activity, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            activity.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = activityMapper.selectUser(activity);
        PageInfo<Activity> pageInfo = PageInfo.of(list);
        List<Activity> activityList = pageInfo.getList();
        for (Activity act : activityList) {
            this.setActivity(act, currentUser);
        }
        return pageInfo;
    }
    /**
     * 设置活动额外信息
     * @param act
     * @param currentUser
     */
    private void setActivity(Activity act, Account currentUser){
        act.setIsEnd(DateUtil.parse(act.getEnd()).isBefore(new Date())); //活动结束时间在当前时间之前
        //查询当前用户是否已经报名当前活动
        ActivitySign activitySign = activitySignMapper.selectByActivityIdAndUserId(act.getId(), currentUser.getId());
        act.setIsSign(activitySign!=null);
    }
    /**
     * 热门活动
     */
    public List<Activity> selectTop() {
        List<Activity> activityList = this.selectAll(null);
        activityList = activityList.stream().sorted((b1, b2) -> b2.getReadCount().compareTo(b1.getReadCount()))
                .limit(2)
                .collect(Collectors.toList());
        return activityList;
    }

    /**
     * 更新阅读量
     * @param activityId
     */
    public void updateReadCount(Integer activityId) {
        activityMapper.updateReadCount(activityId);
    }
}