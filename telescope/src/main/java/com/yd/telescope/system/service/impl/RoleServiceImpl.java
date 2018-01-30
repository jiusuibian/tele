package com.yd.telescope.system.service.impl;

import com.yd.telescope.common.dto.DatatableRes;
import com.yd.telescope.common.enums.ResultEnums;
import com.yd.telescope.common.exception.ServiceException;
import com.yd.telescope.system.domain.Role;
import com.yd.telescope.system.domain.RoleCondition;
import com.yd.telescope.system.repository.RoleRepository;
import com.yd.telescope.system.service.RoleService;
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
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findAll() throws ServiceException {
        try {
            return  roleRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public Set<Role> findByRole_ids(List<String> role_ids) throws ServiceException {

        try {
            return roleRepository.findByRole_ids(role_ids);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public DatatableRes<List<Role>> findAllByPaging(final RoleCondition condition) throws ServiceException {
        try {
            Specification<Role> specification = new Specification<Role>() {
                @Override
                public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    if(!StringUtils.isEmpty(condition.getCreate_start_time())){
                        predicates.add(criteriaBuilder.greaterThan(root.<Timestamp>get("create_time"), Timestamp.valueOf(condition.getCreate_start_time())));
                    }
                    if(!StringUtils.isEmpty(condition.getCreate_end_time())){
                        predicates.add(criteriaBuilder.lessThan(root.<Timestamp>get("create_time"), Timestamp.valueOf(condition.getCreate_end_time())));
                    }
                    if(!StringUtils.isEmpty(condition.getRole_name())){
                        predicates.add(criteriaBuilder.like(root.<String>get("role_name"), "%"+ condition.getRole_name() +"%"));
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                }
            };
            //创建分页对象
            PageRequest pageRequest = new PageRequest(condition.getPage(),condition.getSize());
            List<Role> roles = roleRepository.findAll(specification,pageRequest).getContent();
            long total = roleRepository.findAll(specification).size();

            DatatableRes<List<Role>> pagingObject = new DatatableRes<>();
            pagingObject.setData(roles);
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
    public boolean exists(String role_id) throws ServiceException {
        try {
            return roleRepository.exists(role_id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public boolean save(Role role) throws ServiceException {
        try {
            return roleRepository.saveAndFlush(role) != null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public void delete(String role_id) throws ServiceException {
        try {
            roleRepository.delete(role_id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public Role findByRole_id(String role_id) throws ServiceException {
        try {
            return roleRepository.findOne(role_id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public void update(Role role) throws ServiceException {
        try {
           roleRepository.update(role.getRole_name(),role.getRemark(),role.getModify_time(),role.getModifier(),role.getRole_id());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }
}
