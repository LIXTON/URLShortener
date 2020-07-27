package com.lxn.urlshortener;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

public class Application {
    public static void main(String[] args) {
        port(8082);
        
        get("/list", (request, response) -> UrlShortcutController.fetchAllUrlShortcut);
        get("/:alias", UrlShortcutController.handleRedirect);
        post("/newUrl", "application/json", UrlShortcutController.handleCreation);
    }
}
