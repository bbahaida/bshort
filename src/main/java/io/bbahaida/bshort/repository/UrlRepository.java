package io.bbahaida.bshort.repository;

import io.bbahaida.bshort.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Url findByKey(String key);
}
