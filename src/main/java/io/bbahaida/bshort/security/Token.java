package io.bbahaida.bshort.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {
    public boolean authenticated = false;
    public String authToken;
    public TokenUser user;
    public String refreshToken;
}
