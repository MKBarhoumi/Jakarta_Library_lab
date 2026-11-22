package edu.campus.library.service;

import edu.campus.library.domain.Member;
import edu.campus.library.domain.NewMember;
import edu.campus.library.repository.MemberRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
@Logged
public class MemberService {
    private MemberRepository repository;
    protected MemberService() {
    }

    @Inject
    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public List<Member> findAll() {
        return repository.findAll();
    }

    public Member create(NewMember request) {
        Member member = new Member(null, request.name(), request.email());
        return repository.save(member);
    }

}