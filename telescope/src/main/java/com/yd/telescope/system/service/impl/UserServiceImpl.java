package com.yd.telescope.system.service.impl;

import com.yd.telescope.common.dto.DatatableRes;
import com.yd.telescope.common.enums.ResultEnums;
import com.yd.telescope.common.exception.ServiceException;
import com.yd.telescope.system.domain.User;
import com.yd.telescope.system.domain.UserCondition;
import com.yd.telescope.system.repository.UserRepository;
import com.yd.telescope.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl  implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Override
    public DatatableRes<List<User>> findAllByPaging(final UserCondition condition) throws ServiceException {
        try {
            Specification<User> specification = new Specification<User>() {
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    if(!StringUtils.isEmpty(condition.getCreate_start_time())){
                        predicates.add(criteriaBuilder.greaterThan(root.<Timestamp>get("create_time"), Timestamp.valueOf(condition.getCreate_start_time())));
                    }
                    if(!StringUtils.isEmpty(condition.getCreate_end_time())){
                        predicates.add(criteriaBuilder.lessThan(root.<Timestamp>get("create_time"), Timestamp.valueOf(condition.getCreate_end_time())));
                    }
                    if(!StringUtils.isEmpty(condition.getStatus()) && !condition.getStatus().equals("-1")){
                        predicates.add(criteriaBuilder.equal(root.<String>get("status"),condition.getStatus()));
                    }
                    if(!StringUtils.isEmpty(condition.getUsername())){
                        predicates.add(criteriaBuilder.like(root.<String>get("username"), "%"+ condition.getUsername() +"%"));
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                }
            };
            //创建分页对象
            PageRequest pageRequest = new PageRequest(condition.getPage(),condition.getSize());
            List<User> users = userRepository.findAll(specification,pageRequest).getContent();
            long total = userRepository.findAll(specification).size();

            DatatableRes<List<User>> pagingObject = new DatatableRes<>();
            pagingObject.setData(users);
            pagingObject.setDraw(condition.getDraw());
            pagingObject.setRecordsTotal(total);
            pagingObject.setRecordsFiltered(total);
            return pagingObject;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }


    @Override
    public boolean save(User user) throws ServiceException {
        try {
            return userRepository.saveAndFlush(user) != null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }

    }

    @Override
    public void delete(String username) throws ServiceException {
        try {
            userRepository.delete(username);
        } catch (Exception e) {
            e.printStackTrace();
           throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public boolean exists(String username) throws ServiceException {
        try {
            return userRepository.exists(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public void update(User user) throws ServiceException {
        try {
            userRepository.update(user.getUsername(),user.getPassword(),user.getStatus(),user.getUserdesc(),
                    user.getModify_time(), user.getModifier());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public User findByUsername(String username) throws ServiceException {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

}
