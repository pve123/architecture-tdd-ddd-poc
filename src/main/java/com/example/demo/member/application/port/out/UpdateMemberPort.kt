package com.example.demo.member.application.port.out

import com.example.demo.member.domain.Member

interface UpdateMemberPort {

    fun update(member: Member) : Member
}