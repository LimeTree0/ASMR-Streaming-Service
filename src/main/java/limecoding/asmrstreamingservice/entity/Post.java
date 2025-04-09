package limecoding.asmrstreamingservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Post {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;


    private String content;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asmr_file_id")
    private ASMRFile asmrFile;

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updatePost(String title, String content, ASMRFile asmrFile) {
        this.title = title;
        this.content = content;
        this.asmrFile = asmrFile;
    }
}
