package com.example.SchoolApp.service;

import com.example.SchoolApp.model.LinkHash;
import com.example.SchoolApp.repository.LinkRepository;

import com.google.gson.Gson;
import io.swagger.v3.core.util.Json;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Base64;

/***@Author Ayomide
 * send links to the mailing api and save the reset links to the database
 */
@Service
@Slf4j
public class LinkService {

    @Value("${shared.key}")
    private String sharedKey;

    private LinkRepository linkRepository;
    @Autowired
    public void setLinkRepository(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }


    public void save(LinkHash linkhash) {
        linkRepository.save(linkhash);
    }

    public Boolean linkExist(String linkhash) {
        String result ="";
        try{
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha.digest(linkhash.getBytes());
            result = new String(Hex.encode(hash));
            return linkRepository.existsByLink(result) &&
                    !(linkRepository.findByLink(result).isExpired());

        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void sendEmail(LinkHash linkHash) {
        LinkHashRequest request = new LinkHashRequest(
                linkHash.getEmail(), linkHash.getUsername(), linkHash.getLink()
        );

        try {
            String credentials = "user:" + sharedKey;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
            HttpPost post = new HttpPost("https://api-gateway-sxrh.onrender.com/mailing-service/send_password_link");
            Gson gson = new Gson();
            StringEntity stringEntity = new StringEntity(gson.toJson(request), "UTF-8");
            CloseableHttpClient httpclient = HttpClients.createDefault();
            post.setHeader("Authorization", "Basic " + encodedCredentials);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(stringEntity);
            httpclient.execute(post);
        } catch (Exception e) {
            log.error(e.getMessage());

        }
    }

    public String findByLinkToken(String linkToken) {
        String result ="";
        try{
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha.digest(linkToken.getBytes());
            result = new String(Hex.encode(hash));
            return linkRepository.findByLink(result).getUsername();

        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public class LinkHashRequest{
        public String email;
        public String username;
        public String link;

        public LinkHashRequest(String email, String username, String link) {
            this.email = email;
            this.username = username;
            this.link = link;
        }
        public String getEmail() {
            return email;
        }
        public String getUsername() {
            return username;
        }
        public String getLink() {
            return link;
        }
    }
}
