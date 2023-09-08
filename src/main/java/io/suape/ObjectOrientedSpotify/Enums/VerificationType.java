package io.suape.ObjectOrientedSpotify.Enums;

public enum VerificationType {
    ACCOUNT("ACCOUNT"),
    PASSWORD("PASSWORD");

    private final String type;

    VerificationType(String type){
        this.type = type;
    }

    public String getType(){
        return type.toLowerCase();
    }
}
