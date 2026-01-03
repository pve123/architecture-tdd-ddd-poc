package com.example.demo.member.adapter.`in`.web

import com.example.demo.member.adapter.`in`.web.request.CreateMemberRequest
import com.example.demo.member.adapter.`in`.web.request.UpdateMemberRequest
import com.example.demo.member.adapter.`in`.web.response.CreateMemberResponse
import com.example.demo.member.adapter.`in`.web.response.GetMemberResponse
import com.example.demo.member.adapter.`in`.web.response.UpdateMemberResponse
import com.example.demo.member.application.port.`in`.CreateMemberCommand
import com.example.demo.member.application.port.`in`.UpdateMemberCommand
import com.example.demo.member.domain.Member
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface MemberWebMapper {


    fun toCreateCommand(request: CreateMemberRequest): CreateMemberCommand
    fun toUpdateCommand(id: String, request: UpdateMemberRequest): UpdateMemberCommand

    fun toCreateMemberResponse(member: Member): CreateMemberResponse
    fun toGetMemberResponse(member: Member): GetMemberResponse
    fun toUpdateMemberResponse(member: Member): UpdateMemberResponse
}
