package com.example.demo.member.application.port.out

import com.example.demo.member.domain.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface MemberQueryPort {





    open fun searchMembers(pageable: Pageable): Page<Member>
    fun findById(id: String): Member
}