package code.domain.store.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "store")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {

    // TODO 매장 오픈 시간과 마감 시간 정보를 어떻게 관리할 지 속성, 로직 구성 필요

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id", length = 30)
    private Long id;

    @Column(nullable = false, length = 30) // 한글 10자
    private String name;

    @Column(nullable = false, length = 100)
    private String location;

    @Column(nullable = false, name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "instagram_id", length = 30)
    private String instagramId;

    @Column(name = "review_count")
    private Long reviewCount = 0L;

    @Column
    private Double rating;

    @Column(nullable = false, name = "owner_id", length = 30)
    private Long ownerId;

    @Builder
    private Store(String name, String location, String phoneNumber, String instagramId,
                  Long reviewCount, Double rating, Long ownerId) {
        this.name = name;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.instagramId = instagramId;
        this.reviewCount = reviewCount;
        this.rating = rating;
        this.ownerId = ownerId;
    }
}
