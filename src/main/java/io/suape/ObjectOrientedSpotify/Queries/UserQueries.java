package io.suape.ObjectOrientedSpotify.Queries;

public class UserQueries {

    public static final String INSERT_USER_QUERY = "INSERT INTO Users(user_id, first_name, last_name, email, password) VALUES (:userId, :firstName, :lastName, :email, :password)";
    public static final String COUNT_USER_EMAIL_QUERY = "SELECT COUNT(*) FROM Users WHERE email = :email";
    public static final String INSERT_ACCOUNT_VERIFICATION_URL_QUERY = "INSERT INTO Account_Verifications (user_id, url) VALUES(:userId, :url)";
}
