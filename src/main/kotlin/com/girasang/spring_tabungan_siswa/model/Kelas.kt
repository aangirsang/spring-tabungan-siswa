package com.girasang.spring_tabungan_siswa.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
data class Kelas (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long  = 0,

    @ManyToOne
    @JoinColumn(name = "tingkatankelas_id")
    var tingkatanKelas: TingkatanKelas? = null,

    @ManyToOne
    @JoinColumn(name = "tahunajaran_id")
    var tahunAjaran: TahunAjaran? = null,

    var namaKelas: String = "",

    @OneToMany(mappedBy = "kelas", cascade = [CascadeType.ALL], orphanRemoval = true)
    val daftarSiswa: MutableList<AnggotaKelas> = mutableListOf()

)