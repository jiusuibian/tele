package com.yd.telescope.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ts_system_role")
@EntityListeners(AuditingEntityListener.class)
public class Role implements Serializable{
    @Id
    @NotBlank(message = "用户id不能为空。")
    private String role_id;
    @NotBlank(message = "用户名不能为空。")
    @Length(min = 2)
    private String role_name;
    @Length(max = 50)
    private String remark;

    @CreatedDate
    private Date create_time;

//    @LastModifiedDate
    private Timestamp modify_time;

    @CreatedBy
    private String creator;

//    @LastModifiedBy
    private String modifier;

    @ManyToMany(fetch= FetchType.LAZY)
    @JoinTable(name = "ts_system_role2menu", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
            @JoinColumn(name = "menu_id") })
    @JsonIgnore
    private Set<Menu> menus;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<User> users;

    @Transient
    private boolean checked = false;

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Timestamp getModify_time() {
        return modify_time;
    }

    public void setModify_time(Timestamp modify_time) {
        this.modify_time = modify_time;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Collection<String> getMenuNames() {
        Set<String> set = new HashSet<>();
        Set<Menu> perlist = getMenus();
        for (Menu menu : perlist) {
            set.add(menu.getPerms());
        }
        return set;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(null == obj) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }

        Role role = (Role) obj;
        if(!role_id.equals(role.role_id)) {
            return false;
        }
        return true;
    }
}

