package edu.campus.library.repository;
import edu.campus.library.domain.Member;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class InMemoryMemberRepository implements MemberRepository {

    private final Map<Long, Member> store = new ConcurrentHashMap<>();

    private final AtomicLong ids = new AtomicLong();

    @Override
    public List<Member> findAll() {

        return List.copyOf(store.values());

    }

    @Override
    public Optional<Member> find(long id) {

        return Optional.ofNullable(store.get(id));

    }

    @Override
    public Member save(Member member) {

        Long id = member.id();
        if (id == null) {
            id = ids.incrementAndGet();
        }

        Member saved = new Member(
                id,
                member.name(),
                member.email()
        );

        store.put(id, saved);
        return saved;

    }

}