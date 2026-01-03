package com.example.demo.member.application.service

import com.example.demo.member.application.port.`in`.*
import com.example.demo.member.application.port.out.MemberCommandPort
import com.example.demo.member.application.port.out.MemberQueryPort
import com.example.demo.member.domain.Member
import com.github.f4b6a3.ulid.UlidCreator
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberQueryPort: MemberQueryPort,
    private val memberCommandPort: MemberCommandPort,
    private val passwordEncoder: PasswordEncoder
) : CreateMemberUseCase, GetMemberUseCase, DeleteMemberUseCase, UpdateMemberUseCase, GetMembersUseCase {

    override fun getMembers(pageable: Pageable): Page<Member> =
        memberQueryPort.searchMembers(pageable)

    override fun getMember(id: String): Member =
        memberQueryPort.findById(id)

    override fun createMember(command: CreateMemberCommand): Member {
        val encodedPassword = passwordEncoder.encode(command.password)
        val member = Member(
            id = UlidCreator.getUlid().toString(),
            email = command.email,
            password = encodedPassword,
            name = command.name,
            gender = command.gender,
            phoneNumber = command.phoneNumber,
            address = command.address,
            isDeleted = false
        )
        return memberCommandPort.save(member)
    }

    override fun updateMember(command: UpdateMemberCommand): Member {

        val current = memberQueryPort.findById(command.id)

        val updated = current.copy(
            email = command.email ?: current.email,
            phoneNumber = command.phoneNumber ?: current.phoneNumber,
            address = command.address ?: current.address,
        )

        return memberCommandPort.update(updated)
    }

    override fun deleteMember(id: String) {
        memberCommandPort.softDeleteById(id)
    }
}
