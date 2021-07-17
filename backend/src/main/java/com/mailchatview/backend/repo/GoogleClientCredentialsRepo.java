package com.mailchatview.backend.repo;

import com.mailchatview.backend.util.Utils;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
@Getter
@Setter
public class GoogleClientCredentialsRepo {

    @Value("${google.scope}")
    private String scope;

    private final String clientId;
    private final String clientSecret;
    private final String projectId;
    private final String authUri;
    private final String tokenUri;
    private final String authProviderX509CertUrl;
    private final String redirectUri;
    private final String javascriptOrigin;

    public GoogleClientCredentialsRepo(@Value("${google.client.credentials}") String credentialFile)
            throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject(Utils.readFile(credentialFile));
        JSONObject webJsonObject = jsonObject.getJSONObject("web");

        this.clientId = webJsonObject.getString("client_id");
        this.clientSecret = webJsonObject.getString("client_secret");
        this.projectId = webJsonObject.getString("project_id");
        this.authUri = webJsonObject.getString("auth_uri");
        this.tokenUri = webJsonObject.getString("token_uri");
        this.authProviderX509CertUrl = webJsonObject.getString("auth_provider_x509_cert_url");

        JSONArray redirectUrisObject = webJsonObject.getJSONArray("redirect_uris");
        JSONArray javascriptOriginsObject = webJsonObject.getJSONArray("javascript_origins");

        if (redirectUrisObject.length() != 1 || javascriptOriginsObject.length() != 1) {
            throw new JSONException("Multiples redirect URIs or Javascript Origins " +
                    "have been found. Only one is expected.");
        }

        this.redirectUri = redirectUrisObject.getString(0);
        this.javascriptOrigin = javascriptOriginsObject.getString(0);
    }
}
