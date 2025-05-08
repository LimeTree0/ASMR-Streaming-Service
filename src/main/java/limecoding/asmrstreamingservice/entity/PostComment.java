package limecoding.asmrstreamingservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class PostComment {

    @Id
    @GeneratedValue
    private Long id;

    private String comment;

    @Column(nullable = false)
    private String username;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;



}
