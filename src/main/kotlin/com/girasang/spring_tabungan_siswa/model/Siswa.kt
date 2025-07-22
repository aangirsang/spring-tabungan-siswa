package com.girasang.spring_tabungan_siswa.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDate


@Entity

data class Siswa (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var namaLengkap: String="",
    var jenisKelamin: String="",
    var nisn: String="",
    var tempatLahir: String="",
    var tanggalLahir: LocalDate= LocalDate.now(),
    var agama: String="",
    var alamatLengkap: String="",
    var tanggalMasuk: LocalDate = LocalDate.now(),

    @ManyToOne
    @JoinColumn(name = "kelas_id")
    var kelas: Kelas? = null
)