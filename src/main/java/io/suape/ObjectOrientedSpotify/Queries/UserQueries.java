package io.suape.ObjectOrientedSpotify.Queries;

public class UserQueries {

    public static final String INSERT_USER_QUERY = "INSERT INTO Users(user_id, first_name, last_name, email, password) VALUES (:userId, :firstName, :lastName, :email, :password)";
    public static final String COUNT_USER_EMAIL_QUERY = "SELECT COUNT(*) FROM Users WHERE email = :email";
    public static final String INSERT_ACCOUNT_VERIFICATION_URL_QUERY = "INSERT INTO Account_Verifications (user_id, url) VALUES(:userId, :url)";
    public static final String DELETE_VERIFICATION_CODE_BY_USER_ID = "DELETE FROM TwoFactorVerifications WHERE user_id = :userId";
    public static final String INSERT_VERIFICATION_CODE_QUERY = "INSERT INTO TwoFactorVerifications (user_id, code, expiration_date) VALUES (:userId, :code, expirationDate";
    public static final String SELECT_USER_BY_EMAIL_QUERY = "SELECT * From Users WHERE email = :email";

    public static final String SELECT_USER_BY_USER_CODE_QUERY = "SELECT * FROM Users WHERE id = (SELECT user_id FROM ";
}
