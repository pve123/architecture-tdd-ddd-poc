package io.github.pve123.hexagonal.member.application.port.in;

import java.util.Collection;
import java.util.List;

/**
 * 다른 컨텍스트가 여러 회원의 공개 정보(이름)를 한 번에 가져갈 때 쓰는 입구입니다.
 * 회원마다 따로 부르면 N+1 쿼리가 되므로 ID를 모아서 한 번에 넘깁니다.
 */
public interface GetMemberProfilesUseCase {

    /** 탈퇴한 회원도 담아서, 부르는 쪽이 탈퇴 여부에 맞게 보여 줄 수 있게 합니다. */
    List<Profile> getProfiles(Collection<String> memberIds);

    record Profile(String id, String name, boolean withdrawn) {
    }
}
