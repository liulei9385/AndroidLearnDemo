package com.leileitest.paperdemo.entity;

/**
 * sunjie
 * 2015/11/27 0027
 * 17:17
 */
public class CollectItem {
    private String id;
    private String customerId;
    private int gate;
    private int uid;
    private String name;
    private String accountType;
    private String zoneType;

    private int iconPath = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getIconPath() {
        return iconPath;
    }

    public void setIconPath(int iconPath) {
        this.iconPath = iconPath;
    }

    public int getGate() {
        return gate;
    }

    public void setGate(int gate) {
        this.gate = gate;
    }

    /**
     * 主要是为了方便找顺序
     *
     * @param gate
     * @param uid
     * @return
     */
    public static CollectItem newIndexItem(int gate, int uid) {
        CollectItem item = new CollectItem();
        item.setGate(gate);
        item.setUid(uid);
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        else if (o instanceof CollectItem) {
            CollectItem ohterItem = (CollectItem) o;
            //uid 和 gata 都相等才行。
            return ohterItem.getUid() == this.getUid()
                    && ohterItem.getGate() == this.getGate();
        } else return false;
    }

    public String getZoneType() {
        return zoneType;
    }

    public void setZoneType(String zoneType) {
        this.zoneType = zoneType;
    }
}
