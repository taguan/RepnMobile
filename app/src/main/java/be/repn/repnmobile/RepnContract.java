package be.repn.repnmobile;

import android.provider.BaseColumns;

public class RepnContract {

    private RepnContract() {}

    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "User";
        public static final String COLUMN_NAME_LAST_NAME = "lastName";
        public static final String COLUMN_NAME_FIRST_NAME = "firstName";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_STREET = "street";
        public static final String COLUMN_NAME_STREET_NUMBER = "streetNumber";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_MOBILE = "mobile";
        public static final String COLUMN_NAME_BIRTH_DATE = "birthDate";
        public static final String COLUMN_NAME_EMAIL = "email";
    }
}
