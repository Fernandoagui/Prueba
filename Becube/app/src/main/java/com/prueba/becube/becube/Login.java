package com.prueba.becube.becube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {

    @SerializedName("Users_Delivery_Id")
    @Expose
    private Integer usersDeliveryId;
    @SerializedName("Users_Delivery_Alias")
    @Expose
    private String usersDeliveryAlias;
    @SerializedName("Users_Delivery_Availability")
    @Expose
    private Boolean usersDeliveryAvailability = false;
    @SerializedName("Users_Delivery_Topic")
    @Expose
    private String usersDeliveryTopic;
    @SerializedName("User_Type_Id")
    @Expose
    private Integer userTypeId;

    public Integer getUsersDeliveryId() {
        return usersDeliveryId;
    }

    public void setUsersDeliveryId(Integer usersDeliveryId) {
        this.usersDeliveryId = usersDeliveryId;
    }

    public String getUsersDeliveryAlias() {
        return usersDeliveryAlias;
    }

    public void setUsersDeliveryAlias(String usersDeliveryAlias) {
        this.usersDeliveryAlias = usersDeliveryAlias;
    }

    public Boolean getUsersDeliveryAvailability() {
        return usersDeliveryAvailability;
    }

    public void setUsersDeliveryAvailability(Boolean usersDeliveryAvailability) {
        this.usersDeliveryAvailability = usersDeliveryAvailability;
    }

    public String getUsersDeliveryTopic() {
        return usersDeliveryTopic;
    }

    public void setUsersDeliveryTopic(String usersDeliveryTopic) {
        this.usersDeliveryTopic = usersDeliveryTopic;
    }

    public Integer getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

}