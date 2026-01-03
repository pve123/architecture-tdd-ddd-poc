package com.example.demo.member.application.port.`in`

import com.example.demo.member.domain.Member

interface GetMemberUseCase {
    fun getMember(id: String): Member
}
