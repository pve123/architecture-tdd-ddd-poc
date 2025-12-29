package com.example.demo.member.application.port.`in`

import com.example.demo.member.domain.Member

interface UpdateMemberUseCase {

    fun updateMember(member: Member): Member
}