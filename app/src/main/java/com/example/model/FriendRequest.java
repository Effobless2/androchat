package com.example.model;

public class FriendRequest {
    public static final String COLLECTION_DATABASE_NAME = "friend_requests";
    public static final String FROM_DATABASE_FIELD = "from";
    public static final String TO_DATABASE_FIELD = "to";

    private String from;
    private String to;
    private String documentId;

    public FriendRequest() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
