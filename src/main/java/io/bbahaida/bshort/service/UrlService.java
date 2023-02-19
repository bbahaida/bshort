package io.bbahaida.bshort.service;

import io.bbahaida.bshort.model.Url;
import io.bbahaida.bshort.model.User;
import io.bbahaida.bshort.repository.UrlRepository;
import io.bbahaida.bshort.repository.UserRepository;
import io.seruco.encoding.base62.Base62;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UrlService {
    private final Base62 base62 = Base62.createInstance();
    @Autowired
    private UrlRepository repository;
    @Autowired
    private UserRepository userRepository;
    public Url shorten(String longUrl) {
        if (longUrl == null || longUrl.length() < 4) {
            throw new IllegalArgumentException("url is not valid");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(RuntimeException::new);

        Url url = Url.builder()
                .original(longUrl)
                .key(toShort(longUrl))
                .redirectionTimes(0L)
                .user(user)
                .build();
        return repository.save(url);
    }

    private String toShort(String longUrl) {
        String generatedUrl = new String(base62.encode(longUrl.getBytes()));
        int length = generatedUrl.length();
        return length > 6 ? generatedUrl.substring(length - 6, length): generatedUrl;
    }

    public Url findByKey(String key) {
        Url url = repository.findByKey(key);
        url.incrementRead();
        repository.save(url);
        return url;
    }
}
