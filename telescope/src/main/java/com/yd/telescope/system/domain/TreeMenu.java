package com.yd.telescope.system.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "ts_system_menu")
public class TreeMenu implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private long nodeId;
    @Column(name = "name")
    private String text;

    @Transient
    private State state = new State();

    @OneToMany
    @JoinColumn(name = "parent_id" )
    private List<TreeMenu> nodes;

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public List<TreeMenu> getNodes() {
        return nodes;
    }

    public void setNodes(List<TreeMenu> nodes) {
        this.nodes = nodes;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}


