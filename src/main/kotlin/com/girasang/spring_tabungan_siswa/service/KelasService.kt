package com.girasang.spring_tabungan_siswa.service

import com.girasang.spring_tabungan_siswa.model.Kelas
import com.girasang.spring_tabungan_siswa.repository.KelasRepository
import org.springframework.stereotype.Service


@Service
open class KelasService (private val kelasRepository: KelasRepository){
    fun cariSemua(): List<Kelas> =kelasRepository.findAll()
    fun cariId(id: Long) = kelasRepository.findById(id)
    fun simpan(kelas: Kelas): Kelas = kelasRepository.save(kelas)
    fun hapus(id: Long) = kelasRepository.deleteById(id)
}