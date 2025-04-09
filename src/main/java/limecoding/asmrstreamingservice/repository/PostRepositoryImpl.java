package limecoding.asmrstreamingservice.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import limecoding.asmrstreamingservice.dto.post.PostSearchCondition;
import limecoding.asmrstreamingservice.entity.Post;
import limecoding.asmrstreamingservice.entity.QPost;
import limecoding.asmrstreamingservice.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Post> search(PostSearchCondition condition, Pageable pageable) {
        QPost post = QPost.post;
        QUser user = QUser.user;

        List<Post> posts = queryFactory
                .selectFrom(post)
                .join(post.user, user).fetchJoin()
                .where(
                        titleContains(condition.getTitle()),
                        userIdEq(condition.getAuthor())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .join(post.user, user)
                .where(
                        titleContains(condition.getTitle()),
                        userIdEq(condition.getAuthor())
                )
                .fetchOne();


        long safeTotal = total == null ? 0L : total;

        return new PageImpl<>(posts, pageable, safeTotal);
    }

    private BooleanExpression titleContains(String title) {
        return StringUtils.hasText(title) ? QPost.post.title.containsIgnoreCase(title) : null;
    }

    private BooleanExpression userIdEq(String userId) {
        return StringUtils.hasText(userId) ? QPost.post.user.userId.eq(userId) : null;
    }
}
