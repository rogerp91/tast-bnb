package com.github.testairbnd.util;

import com.facebook.AccessToken;

/**
 * Created by AndrewX on 13/12/2016.
 */

public class AccessTokenFacebook {

  public static boolean isLoggedIn() {
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    return accessToken != null;
  }

}
