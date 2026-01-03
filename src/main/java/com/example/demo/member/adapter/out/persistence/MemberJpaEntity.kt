package com.example.demo.member.adapter.out.persistence

//import com.example.demo.board.adapter.out.persistence.BoardJpaEntity
import com.example.demo.common.jpa.BaseTimeEntity
import com.example.demo.member.domain.GenderEnum
import com.example.demo.member.domain.Member
import com.github.f4b6a3.ulid.UlidCreator
import jakarta.persistence.*
import org.hibernate.annotations.Where
import java.time.LocalDateTime
import java.time.ZoneId

@Entity
@Table(name = "member")
class MemberJpaEntity(

    @Id
    @Column(nullable = false, updatable = false)
    var id: String? = null,

    @Column(nullable = false, length = 100)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false, length = 25)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, length = 25)
    var gender: GenderEnum,

    @Column(nullable = false, length = 20)
    var phoneNumber: String,

    @Column(nullable = false, length = 200)
    var address: String,

    @Column(nullable = false)
    var isDeleted: Boolean = false,

    @Column
    var deletedAt: LocalDateTime? = null,

//    @OneToMany(mappedBy = "member", cascade = [CascadeType.ALL], orphanRemoval = true)
//    val boardList: MutableList<BoardJpaEntity> = mutableListOf()

) : BaseTimeEntity() {

    @PrePersist
    fun prePersist() {
        if (id.isNullOrBlank()) id = UlidCreator.getUlid().toString()
        isDeleted = false
    }

    fun softDelete() {
        isDeleted = true
        deletedAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
    }

    fun applyUpdate(member: Member) {
        email = member.email
        address = member.address
        phoneNumber = member.phoneNumber
    }
}
