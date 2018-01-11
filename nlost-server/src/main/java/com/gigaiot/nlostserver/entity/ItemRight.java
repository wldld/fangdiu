package com.gigaiot.nlostserver.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by zz on 2017/5/28.
 */
@Entity(name = "t_right")
@Setter
@Getter
@ToString(exclude = "item")
@NoArgsConstructor
@Table(indexes = {@Index( columnList = "user_id")})


@NamedEntityGraph(name = "ItemRight.detail",
        attributeNodes ={
        @NamedAttributeNode(value = "item", subgraph = "subItem")
},
        subgraphs = {
                @NamedSubgraph(name = "subItem", attributeNodes = {
                        @NamedAttributeNode("properties"),
                        @NamedAttributeNode("fields"),
                        @NamedAttributeNode("itemRights")}),
        })


public class ItemRight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "v")
    private long value;  //权限值{View=1，Edit=2，Manage access=4，Delete=8}

    @ManyToOne(cascade = {CascadeType.REFRESH})
    private  Item item;

    /*@ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinTable(name="t_item_right",
            joinColumns = {@JoinColumn(name = "right_id")},
            inverseJoinColumns = {@JoinColumn(name="item_id")})
    //@JoinColumn(name = "item_id")  //itemRights 表里加一个外键 item_id
    private Item item;*/

}
