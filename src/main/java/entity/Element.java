package entity;

import java.util.Objects;

public class Element {
    private int itemId;
    private int groupId;

    public Element(int itemId, int groupId) {
        this.itemId = itemId;
        this.groupId = groupId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "" + itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Element)) return false;
        Element element = (Element) o;
        return itemId == element.itemId &&
                groupId == element.groupId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(itemId, groupId);
    }
}
