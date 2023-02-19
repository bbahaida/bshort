package io.bbahaida.bshort.service;

import io.bbahaida.bshort.model.Url;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @InjectMocks
    private UrlService urlService;

    @Test
    public void shorten_shouldThrowException_ifNull () {
        assertThatThrownBy(() -> urlService.shorten(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    public void shorten_shouldThrowException_ifLessThan4 () {
        assertThatThrownBy(() -> urlService.shorten("htt"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shorten_shouldReturn7lengthKey () {
        Url url = urlService.shorten("https://github.com/");
        assertThat(url.getKey())
                .hasSize(6);
    }
}
