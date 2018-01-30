package com.yd.telescope.system.domain;


import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "ts_system_menu")
@EntityListeners(AuditingEntityListener.class)
public class Menu implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long menu_id;
    private long parent_id;
    private String name;
    private String url;
    private String perms;
    private String type;
    private String icon;
    private int order_num;

    @CreatedDate
    private Date create_time;

//    @LastModifiedDate
    private Timestamp modify_time;

    @CreatedBy
    private String creator;

//    @LastModifiedBy
    private String modifier;

    @Transient
    private boolean checked;

    @OneToMany(cascade={CascadeType.ALL},fetch=FetchType.EAGER)
    @JoinColumn(name = "parent_id" )
    private Set<Menu> ChildMenus;

    public long getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(long menu_id) {
        this.menu_id = menu_id;
    }

    public long getParent_id() {
        return parent_id;
    }

    public void setParent_id(long parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Set<Menu> getChildMenus() {
        return ChildMenus;
    }

    public void setChildMenus(Set<Menu> childMenus) {
        ChildMenus = childMenus;
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

        Menu menu = (Menu) obj;
        if(menu_id != menu.menu_id) {
            return false;
        }
        return true;
    }

}


