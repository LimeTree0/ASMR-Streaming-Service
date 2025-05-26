package limecoding.asmrstreamingservice.entity;

import jakarta.persistence.*;
import limecoding.asmrstreamingservice.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class ASMR {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Integer price;

    @OneToOne
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    @OneToOne
    @JoinColumn(name = "file_id", nullable = false)
    private FileEntity fileEntity;

    public void updatePost(Post post) {
        this.post = post;

    }
}