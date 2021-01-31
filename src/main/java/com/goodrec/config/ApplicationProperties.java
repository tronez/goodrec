package com.goodrec.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {

    private final Auth auth = new Auth();

    public static class Auth {

        private String tokenSecret;
        private long tokenExpirationMsec;

        public String getTokenSecret() {
            return tokenSecret;
        }

        public Auth setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
            return this;
        }

        public long getTokenExpirationMsec() {
            return tokenExpirationMsec;
        }

        public Auth setTokenExpirationMsec(long tokenExpirationMsec) {
            this.tokenExpirationMsec = tokenExpirationMsec;
            return this;
        }
    }

    public Auth getAuth() {
        return auth;
    }
}
