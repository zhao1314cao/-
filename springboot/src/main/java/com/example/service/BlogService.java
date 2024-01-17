package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.example.common.enums.LikesModuleEnum;
import com.example.entity.*;
import com.example.mapper.BlogMapper;
import com.example.mapper.CollectMapper;
import com.example.mapper.LikesMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 博客业务处理
 **/
@Service
public class BlogService {

    @Resource
    private BlogMapper blogMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private LikesMapper likesMapper;
    @Resource
    private CollectMapper collectMapper;

    /**
     * 新增
     */
    public void add(Blog blog) {
        Account currentUser = TokenUtils.getCurrentUser();
        if(!currentUser.getRole().equals("ADMIN")){
            blog.setUserId(currentUser.getId());
        }
        blog.setDate(DateUtil.today());
        blogMapper.insert(blog);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        blogMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            blogMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Blog blog) {
        blogMapper.updateById(blog);
    }

    /**
     * 根据ID查询
     */
    @Transactional
    public Blog selectById(Integer id) {
        Blog blog = blogMapper.selectById(id);
        User user = userMapper.selectById(blog.getUserId());
        blog.setUser(user);   //设置作者信息
        //查询当前博客的点赞数据
        int likesCount = likesMapper.selectByFidAndModule(id, LikesModuleEnum.BLOG.getValue());
        blog.setLikesCount(likesCount);
        //查询当前用户是否给当前博客点过赞
        Likes likes = new Likes();
        likes.setUserId(TokenUtils.getCurrentUser().getId());
        likes.setFid(id);
        likes.setModule(LikesModuleEnum.BLOG.getValue());
        Likes userLikes = likesMapper.selectUserLikes(likes);
        //查询当前博客的收藏数据
        int collectCount = collectMapper.selectByFidAndModule(id, LikesModuleEnum.BLOG.getValue());
        blog.setCollectCount(collectCount);
        //查询当前用户是否收藏当前博客
        Collect collect = new Collect();
        collect.setUserId(TokenUtils.getCurrentUser().getId());
        collect.setFid(id);
        collect.setModule(LikesModuleEnum.BLOG.getValue());
        Collect userCollect = collectMapper.selectUserCollect(collect);
        //设置blog的userLike
        blog.setUserLike(userLikes!=null);
        //设置blog的userCollect
        blog.setUserCollect(userCollect!=null);
        return blog;
    }

    /**
     * 查询所有
     */
    public List<Blog> selectAll(Blog blog) {
        return blogMapper.selectAll(blog);
    }

    /**
     * 分页查询
     */
    public PageInfo<Blog> selectPage(Blog blog, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> list = blogMapper.selectAll(blog);
        for (Blog b : list) {
            int likesCount = likesMapper.selectByFidAndModule(b.getId(), LikesModuleEnum.BLOG.getValue());
            b.setLikesCount(likesCount);
            int collectCount = collectMapper.selectByFidAndModule(b.getId(), LikesModuleEnum.BLOG.getValue());
            b.setCollectCount(collectCount);
        }
        return PageInfo.of(list);
    }

    public List<Blog> selectTop() {
        List<Blog> blogList = this.selectAll(null);
        blogList=blogList.stream().sorted((b1,b2)->b2.getReadCount().compareTo(b1.getReadCount()))
                .limit(20)
                .collect(Collectors.toList());
        return blogList;
    }

    public Set<Blog> selectRecommend(Integer blogId) {
        Blog blog = this.selectById(blogId);
        String tags = blog.getTags();
        Set<Blog>blogSet=new HashSet<>();
        if(ObjectUtil.isNotEmpty(tags)){
            List<Blog> blogList = this.selectAll(null);
            for (Blog b : blogList) {
                int likesCount = likesMapper.selectByFidAndModule(b.getId(), LikesModuleEnum.BLOG.getValue());
                b.setLikesCount(likesCount);
                int collectCount = collectMapper.selectByFidAndModule(b.getId(), LikesModuleEnum.BLOG.getValue());
                b.setCollectCount(collectCount);
            }
            JSONArray tagsArray = JSONUtil.parseArray(tags);
            for (Object tag : tagsArray) {
                //筛选出其他包含当前标签的博客列表
                blogSet.addAll(blogList.stream().filter(b->b.getTags().contains(tag.toString())&&!blogId.equals(b.getId()))
                        .collect(Collectors.toSet()));
            }
        }
        return blogSet.stream().limit(5).collect(Collectors.toSet());
    }
}