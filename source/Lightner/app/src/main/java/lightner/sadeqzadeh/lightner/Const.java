package lightner.sadeqzadeh.lightner;

/**
 * Created by reza on 11/12/16.
 */
public class Const {
    public static final String APP_CONFIG = "lightner";
    public static final String VERSION = "0.1";
    //public static final String SERVER_URL  = "http://192.168.177.104:7777";
    //public static final String SERVER_URL  = "http://209.59.209.181:7777";
    public static final String SERVER_URL  = "http://192.168.43.172:8000/api/";

    public static final String LIST_ITEMS_URL  = SERVER_URL  + "/item/list";

    public static final String LIST_CATEGORY_URL = SERVER_URL + "/category/list";
    public static final String ADD_ITEM_URL = SERVER_URL + "/item/add";

    private static final String STATIC_URL = "/static";
    public static final String THUMBNAIL_URL = STATIC_URL + "/t";
    public static final String FULL_IMAGE_URL = STATIC_URL + "/f";
    public static final String DETAIL_ITEM_URL = SERVER_URL + "/item/detail";
    public static final String SEND_PASS_TO_EMAIL_URL = SERVER_URL + "/auth/sendPassToEmail";
    public static final String AUTH_USER_URL = SERVER_URL + "/auth/validateUserPass";
    public static final String NEW_USER_URL = SERVER_URL + "/auth/newUser";
    public static final String UPDATE_FIREBASE_TOKEN = SERVER_URL + "/auth/update/firebaseToken";
    public static final String MY_ITEMS_URL = SERVER_URL + "/item/my";
    public static final String SEND_MESSAGE_URL = SERVER_URL + "/item/message/add";


    public static final String TITLE  = "Title";
    public static final String DESCRIPTION  = "Description";
    public static final String DATE = "Date";
    public static final String CATEGORIES = "Categories";
    public static final String PROVINCE_ID = "ProvinceId";
    public static final String PROVINCE_TITLE  = "ProvinceTitle";
    public static final String CITY_ID = "CityId";
    public static final String CITY_TITLE =  "CityTitle";
    public static final String IMAGE_FILE =  "ImageFile";
    public static final String ITEM_TYPE =  "ItemType";
    public static final String UTF8 = "UTF-8";
    public static final int FOUND = 2;
    public static final int LOST = 1;
    public static final String ID = "Id";
    public static final String CATEGORY_TITLES = "CategoryTitles";
    public static final String MSISDN = "MSISDN";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String ADDRESS = "Address";
    public static final String APPROVED = "Approved";
    public static final String EMAIL = "Email";
    public static final String TELEGRAM_ID = "TelegramId";
    public static final String TOKEN = "token";
    public static final String PASSWORD = "Password";
    public static final String USERNAME = "username";
    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BODY = "Body";
    public static final String FIREBASE_TOKEN = "FirebaseToken";
    public static final String REDIRECT_TO_NEW = "RedirectToNew";
    public static final String REDIRECT_TO_MY = "RedirectToMy";
    public static final String ALIAS = "lightner";
    public static final String ENCRYPTION_IV = "ENCRYPTION_IV";
    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String NAME = "NAME";
    public static final String EDIT = "edit";
    public static final String COLOR = "COLOR";
    public static final String CATEGORY = "CATEGORY";
    public static final String BOX1 = "BOX1";

    public static final String BOX_ID = "BOX_ID";
    public static final String REVIEW_MODE = "REVIEW_MODE";
    public static final String USER_CODE = "USER_CODE";
}
