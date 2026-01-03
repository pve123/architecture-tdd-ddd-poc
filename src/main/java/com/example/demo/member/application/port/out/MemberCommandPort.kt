package com.example.demo.member.application.port.out

import com.example.demo.member.domain.Member

interface MemberCommandPort {


    fun save(member: Member): Member
    fun update(member: Member): Member
    fun softDeleteById(id: String)
}