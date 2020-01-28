package com.example.cemilanku.Util;

public class ServerAPI {
    public static final String BASE_URL = "http://192.168.100.14/";

    public  static final String LOGIN = BASE_URL+"android/Rest/login.php";
    public  static final String REGISTER = BASE_URL+"android/Rest/register.php";

    public static final String URL_DASHBOARD = BASE_URL+"android/Rest/view_data.php";
    public static final String URL_JUAL = BASE_URL+"android/Rest/penjualan/create_jual.php";
    public static final String URL_DETAILJUAL = BASE_URL+"Rest/view_data.php";

    public static final String URL_IMAGE = BASE_URL+"android/upload/produk/";
}
