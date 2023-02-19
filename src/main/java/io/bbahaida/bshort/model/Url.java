package io.bbahaida.bshort.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name = "keyIndex",columnList = "key"))
public class Url {
    @Id
    @GeneratedValue
    private Long id;
    private String original;
    @Column(unique = true)
    private String key;
    private Long redirectionTimes;

    @ManyToOne
    private User user;

    public synchronized void incrementRead() {
        redirectionTimes++;
    }
}
