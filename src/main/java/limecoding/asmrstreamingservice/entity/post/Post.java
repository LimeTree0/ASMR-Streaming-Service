package limecoding.asmrstreamingservice.entity.post;

import jakarta.persistence.*;
import limecoding.asmrstreamingservice.entity.ASMR;
import limecoding.asmrstreamingservice.entity.User;
import limecoding.asmrstreamingservice.entity.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;            // 게시글 아이디

    @Column(nullable = false)
    private String title;       // 게시글 제목

    private String content;     // 게시글 내용

    @Enumerated(EnumType.STRING)
    private PostType postType;  // 게시글 타입

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;          // 게시글 작성 유저 정보

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
