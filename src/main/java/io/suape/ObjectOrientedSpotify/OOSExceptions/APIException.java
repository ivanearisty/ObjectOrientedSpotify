package io.suape.ObjectOrientedSpotify.OOSExceptions;

public class APIException extends RuntimeException{
    public APIException(String message){
        super(message);
    }
}
