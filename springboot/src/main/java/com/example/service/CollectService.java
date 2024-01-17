package com.example.service;

import com.example.entity.Account;
import com.example.entity.Collect;
import com.example.mapper.CollectMapper;
import com.example.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CollectService {

    @Resource
    CollectMapper collectMapper;

    public void set(Collect collect) {
        Account currentUser = TokenUtils.getCurrentUser();
        collect.setUserId(currentUser.getId());
        Collect collect1 = collectMapper.selectUserCollect(collect);
        if (collect1 == null){
            collectMapper.insert(collect);
        }else{
            collectMapper.deleteById(collect1.getId());
        }
    }
}
