package com.girasang.spring_tabungan_siswa.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity

data class Siswa (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var namaSiswa: String="",
    var nisSiswa: String=""

)