package com.example.demo.member.application.port.`in`

import com.example.demo.member.domain.Member

interface CreateMemberUseCase {
    fun createMember(createMemberCommand: CreateMemberCommand): Member
}
