package com.goodrec.testdata;

import com.goodrec.config.ApplicationProperties;
import com.goodrec.security.JJWTTokenProvider;

public class TokenCreator extends JJWTTokenProvider {

    private static ApplicationProperties app = new ApplicationProperties();

    public TokenCreator() {
        super(app);
    }

    public static void setTokenSimpleConfig() {
        app.getAuth().setTokenSecret("secret");
        app.getAuth().setTokenExpirationMsec(600000);
    }
}
