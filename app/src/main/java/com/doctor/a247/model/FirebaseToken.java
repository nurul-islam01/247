package com.doctor.a247.model;

/**
 * Created by Nurul Islam on 4/30/19
 */
public class FirebaseToken {

    private String tokenId;
    private String usersId;

    public FirebaseToken() {
    }

    public FirebaseToken(String tokenId, String usersId) {
        this.tokenId = tokenId;
        this.usersId = usersId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUsersId() {
        return usersId;
    }

    public void setUsersId(String usersId) {
        this.usersId = usersId;
    }
}
