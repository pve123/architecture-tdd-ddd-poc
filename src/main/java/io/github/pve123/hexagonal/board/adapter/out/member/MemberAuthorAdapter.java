package io.github.pve123.hexagonal.board.adapter.out.member;

import io.github.pve123.hexagonal.board.application.port.out.AuthorPort;
import io.github.pve123.hexagonal.board.domain.Author;
import io.github.pve123.hexagonal.member.application.port.in.CheckActiveMemberUseCase;
import io.github.pve123.hexagonal.member.application.port.in.GetMemberProfilesUseCase;
import io.github.pve123.hexagonal.member.application.port.in.GetMemberProfilesUseCase.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 게시판의 출구(AuthorPort)를 회원 컨텍스트의 입구(유스케이스)에 잇습니다.
 * 두 컨텍스트가 만나는 곳은 이 클래스 하나뿐입니다.
 */
@Component
@RequiredArgsConstructor
class MemberAuthorAdapter implements AuthorPort {

    private final CheckActiveMemberUseCase checkActiveMemberUseCase;
    private final GetMemberProfilesUseCase getMemberProfilesUseCase;

    @Override
    public void checkCanWrite(String memberId) {
        checkActiveMemberUseCase.checkActive(memberId);
    }

    @Override
    public Map<String, Author> findAuthors(Collection<String> memberIds) {
        return getMemberProfilesUseCase.getProfiles(memberIds).stream()
                .collect(Collectors.toMap(Profile::id, MemberAuthorAdapter::toAuthor, (first, second) -> first));
    }

    private static Author toAuthor(Profile profile) {
        return profile.withdrawn() ? Author.withdrawn(profile.id()) : new Author(profile.id(), profile.name());
    }
}
