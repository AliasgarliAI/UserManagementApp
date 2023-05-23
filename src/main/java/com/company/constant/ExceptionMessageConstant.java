package com.company.constant;

public class ExceptionMessageConstant {

    public static final String USERNAME_NOT_FOUND = "There is not existing any user with specified username";
    public static final String ACCOUNT_DISABLED = "Your account has been disabled. Please contact administrator.";
    public static final String NOT_ENOUGH_PERMISSION = "You do not have enough permissions";
    public static final String ACCOUNT_LOCKED = "`Your account has been locked. Please contact administrator";
    public static final String METHOD_NOT_ALLOWED = "This request method is not allowed on this endpoint. Please send a '%s' request";
    public static final String EMAIL_NOT_FOUND = "User not found with specified email";
    public static final String USERNAME_EXISTS = "This username is already exists";
    public static final String EMAIL_EXISTS = "This email is already taken";
    public static final String USER_OR_PASSWORD_INCORRECT = "User or password is incorrect. Please try again.";
    public static final String CONFIRMATION_TOKEN_IS_EXPIRED = "Confirmation token is expired for this user.";
    public static final String ACCOUNT_NOT_VERIFIED = "Account can not ber verified. Please contact administrator";
    public static final String PASSWORD_MISMATCH="Specified password and repetition of password do not match. Please enter valid passwords";


    private ExceptionMessageConstant(){}

}
