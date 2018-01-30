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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ts_system_user")
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable{
    @Id
    @NotBlank(message = "用户名不能为空。")
    @Length(min = 2)
    private String username;

    @NotBlank(message = "密码不能为空。")
    private String password;


    private Timestamp lastlogintime;

    private int loginattemps;

    private String status;

    @Length(max = 50)
    private String userdesc;

    @CreatedDate
    private Date create_time;

//    @LastModifiedDate
    private Timestamp modify_time;

    @CreatedBy
    private String creator;

//    @LastModifiedBy
    private String modifier;

    @ManyToMany(fetch= FetchType.LAZY)
    @JoinTable(name = "ts_system_user2role", joinColumns = { @JoinColumn(name = "username") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    @JsonIgnore
    private Set<Role> roles;


    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Timestamp getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(Timestamp lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    public int getLoginattemps() {
        return loginattemps;
    }

    public void setLoginattemps(int loginattemps) {
        this.loginattemps = loginattemps;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserdesc() {
        return userdesc;
    }

    public void setUserdesc(String userdesc) {
        this.userdesc = userdesc;
    }

    @Transient
    public Set<String> getRoleNames() {
        Set<Role> roles = getRoles();
        Set<String> set = new HashSet<String>();
        for (Role role : roles) {
            set.add(role.getRole_name());
        }
        return set;
    }
}
