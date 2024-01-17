package com.example.service;

import com.example.entity.Account;
import com.example.entity.Likes;
import com.example.mapper.LikesMapper;
import com.example.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LikesService {

    @Resource
    LikesMapper likesMapper;

    public void set(Likes likes) {
        Account currentUser = TokenUtils.getCurrentUser();
        likes.setUserId(currentUser.getId());
        Likes likes1 = likesMapper.selectUserLikes(likes);
        if (likes1 == null){
            likesMapper.insert(likes);
        }else{
            likesMapper.deleteById(likes1.getId());
        }
    }
}
