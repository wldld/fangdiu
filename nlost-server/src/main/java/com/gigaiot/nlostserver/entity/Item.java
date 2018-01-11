package com.gigaiot.nlostserver.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zz on 2017/5/28.
 */
@Entity(name = "t_item")
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = "itemRights")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedEntityGraph(name = "Item.detail",
        attributeNodes = {
                @NamedAttributeNode("properties"),
                @NamedAttributeNode("fields"),
                @NamedAttributeNode("itemRights")}
)

@Table(indexes = {
        @Index(columnList = "cls, nm"),
        @Index(columnList = "crts"),
        @Index(columnList = "nm")}
)

public class Item {

    public Item(int id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nm")
    private String name;

    @Column(name = "crtnm", updatable = false)
    private String creatorName;

    @Column(name = "actid")
    private int accountId;

    @Column(name = "actnm")
    private String accountName;

    @Column(updatable = false)
    private int cls;

    @Column(name = "crts", updatable = false)
    private String creators;

    @Column(name = "ct")
    private long createTime = System.currentTimeMillis(); //创建时间


    //@OneToMany(cascade = {CascadeType.ALL}, mappedBy = "item") //ItemProperty类的item 字段
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "item_id")  //在ItemProperty 表里加一个外键 item_id
    private Set<ItemProperty> properties = new HashSet<>();


    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "item_id")  //ItemField 表里加一个外键 item_id
    private Set<ItemField> fields = new HashSet<>();


    //删除item 会级联删除它所有的itemright  ，反过来则不会
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "item_id")  //itemRights 表里加一个外键 item_id
    private Set<ItemRight> itemRights = new HashSet<>();


}
