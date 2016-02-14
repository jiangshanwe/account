package com.jiang.shanwe.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Jiang.Shanwe
 * 2015-11-5 下午11:09:51
 *
 */
public class Record {

    private int id;
    private int ownerId;
    private double count;
    private String comments;
    private Date consumeDate;
    private Date createdTime;
    private Date updatedTime;
    private int status;
    private int syncStatus;

    private List<Tag> tags;
    private List<Integer> tagIds;

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
    }

    public Record() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Date getConsumeDate() {
        return consumeDate;
    }

    public void setConsumeDate(Date consumeDate) {
        this.consumeDate = consumeDate;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getDesc() {
        String desc = "";
        desc += this.comments;
        for (int i = 0; i < tags.size(); i++) {
            desc += tags.get(i).getName();
        }
        return desc;
    }

    @Override
    public String toString() {
        return "Record [id=" + id + ", ownerId=" + ownerId + ", count=" + count
                + ", comments=" + comments + ", consumeDate=" + consumeDate
                + ", createdTime=" + createdTime + ", updatedTime=" + updatedTime
                + ", status=" + status + ", syncStatus=" + syncStatus + ", tags=" + tags
                + ", tagIds=" + tagIds + "]";
    }

}
