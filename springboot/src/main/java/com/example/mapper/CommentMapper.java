package com.example.mapper;

import com.example.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论相关接口
 */
public interface CommentMapper {
    /**
     * 新增
     */
    int insert(Comment comment);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Comment comment);

    /**
     * 根据ID查询
     */
    Comment selectById(Integer id);

    /**
     * 查询所有
     */
    List<Comment> selectAll(Comment comment);

    /**
     * 查询前台展示评论的接口
     * @param comment
     * @return
     */
    List<Comment> selectForUser(Comment comment);

    /**
     * 查询所有评价数目
     * @param fid
     * @param module
     * @return
     */
    Integer selectCommentCount(@Param("fid") Integer fid, @Param("module") String module);
}
