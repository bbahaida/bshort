package io.bbahaida.bshort.web;


import io.bbahaida.bshort.model.Url;
import io.bbahaida.bshort.service.UrlService;
import io.bbahaida.bshort.web.dto.UrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/url")
public class UrlController {
    @Autowired
    private UrlService service;
    @PostMapping("shortener")
    public Url shortener(@RequestBody UrlRequest request) {
        return service.shorten(request.getUrl());
    }

    @GetMapping("/{key}")
    public Url getOriginalUrl(@PathVariable String key) {
        return service.findByKey(key);
    }

}
