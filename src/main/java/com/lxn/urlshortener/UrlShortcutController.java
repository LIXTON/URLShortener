package com.lxn.urlshortener;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class UrlShortcutController {
    public static Route fetchAllUrlShortcut = (Request request, Response response) -> {
        response.status(200);
        List<UrlShortcut> urlShortcutList = UrlShortcutDAO.getAll();
        return new Gson().toJson(urlShortcutList);
    };
    
    public static Route handleRedirect = (Request request, Response response) -> {
        Map<String, String> responseBody = new HashMap();
        String alias = request.params(":alias");
        
        if (isAliasValid(alias)) {
            UrlShortcut urlShortcut = UrlShortcutDAO.findByAlias(alias);

            if (urlShortcut != null)
                response.redirect(urlShortcut.getUrl());
            
            response.status(200);
            responseBody.put("status", "SUCCESS");
            responseBody.put("message", "Nothing found try a different alias");
        } else {
            response.status(400);
            responseBody.put("status", "Bad Request");
        }
        
        return new Gson().toJson(responseBody);
    };

    public static Route handleCreation = (Request request, Response response) -> {
        response.type("application/json");
        
        Map<String, String> bodyResponse = new HashMap();
        String alias;
        boolean isSaved = false;
        UrlShortcut newUrlShortcut = new Gson().fromJson(request.body(), UrlShortcut.class);

        if (isUrlValid(newUrlShortcut.getUrl())) {
            UrlShortcut oldUrlShortcut = UrlShortcutDAO.findByUrl(newUrlShortcut.getUrl());
            
            if (oldUrlShortcut != null) {
                alias = oldUrlShortcut.getAlias();
            } else {
                alias = createAlias(newUrlShortcut.getUrl());
                newUrlShortcut.setAlias(alias);
                isSaved = UrlShortcutDAO.save(newUrlShortcut);
            }


            if (isSaved) {
                response.status(200);
                bodyResponse.put("alias", alias);
            } else {
                response.status(500);
                bodyResponse.put("status", "Internal Server Error");
            }
        } else {
            response.status(400);
            bodyResponse.put("status", "Bad Request");
        }

        return new Gson().toJson(bodyResponse);
    };
    
    private static boolean isAliasValid(String alias) {
        if (alias != null)
            return !alias.trim().isEmpty();
        
        return false;
    }
    
    private static boolean isUrlValid(String url) {
        String regex = "^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))" +
			"(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" +
			"([).!';/?:,][[:blank:]])?$";
        Pattern pattern = Pattern.compile(regex);
        if (url != null)
            return pattern.matcher(url).matches();
        
        return false;
    }

    private static String createAlias(String url) {
        String alias;

        if (url.contains("google")) {
            alias = randomAlpha(5);
        } else if (url.contains("yahoo")) {
            alias = randomAlphaNumeric(7);
        } else {
            alias = getConsonants(url);
        }

        return alias;
    }

    private static String randomAlpha(int size) {
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvxyz";
        Random random = new Random();
        
        return random.ints(0, alpha.length() + 1)
                .limit(size)
                .collect(
                        StringBuilder::new,
                        (x, y) -> x.append(alpha.charAt(y)),
                        StringBuilder::append)
                .toString();
    }

    private static String randomAlphaNumeric(int size) {
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        Random random = new Random();
        
        return random.ints(0, alphanumeric.length() + 1)
                .limit(size)
                .collect(
                        StringBuilder::new,
                        (x, y) -> x.append(alphanumeric.charAt(y)),
                        StringBuilder::append)
                .toString();
    }

    private static String getConsonants(String url) {
        return url.replaceAll("^https|^http|[^BCDFGHJKLMNPQRSTVWXYZbcdfghjklmnpqrstvxyz]", "");
    }
}
