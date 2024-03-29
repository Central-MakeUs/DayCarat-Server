package com.example.daycarat.domain.activity.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@DynamoDBTable(tableName = "activityTagSearch")
public class ActivityTagSearch {

    @DynamoDBHashKey @DynamoDBAutoGeneratedKey
    private String activityTagSearchId;

    @DynamoDBAttribute
    private Long userId;

    @DynamoDBAttribute
    private String activityTag;

    @DynamoDBAttribute
    private String activityTagSearch;

    @DynamoDBAttribute
    private Boolean isDeleted;

    @Builder
    public ActivityTagSearch(Long userId, String activityTag, String activityTagSearch) {
        this.userId = userId;
        this.activityTag = activityTag;
        this.activityTagSearch = activityTagSearch;
        this.isDeleted = false;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void update(String activityTagName, String activityTagSearch) {
        this.activityTag = activityTagName;
        this.activityTagSearch = activityTagSearch;
    }
}
