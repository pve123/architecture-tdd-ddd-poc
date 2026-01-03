package com.example.demo.member.adapter.out.persistence

import com.example.demo.common.exception.BusinessException
import com.example.demo.common.exception.MemberErrorCodeEnum
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<MemberJpaEntity, String> {


    fun findByIdOrThrow(id: String): MemberJpaEntity =
        findById(id).orElseThrow {
            BusinessException(MemberErrorCodeEnum.NOT_FOUND)
        }


}