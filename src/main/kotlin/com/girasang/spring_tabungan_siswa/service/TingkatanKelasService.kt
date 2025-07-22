package com.girasang.spring_tabungan_siswa.service

import com.girasang.spring_tabungan_siswa.model.TingkatanKelas
import com.girasang.spring_tabungan_siswa.repository.TingkatanKelasRepository
import org.springframework.stereotype.Service

@Service
class TingkatanKelasService (private val tingkatanKelasRepository: TingkatanKelasRepository){
    fun cariSemua(): List<TingkatanKelas> =tingkatanKelasRepository.findAll()
    fun cariId(id: Long) = tingkatanKelasRepository.findById(id)
    fun simpan(tingkatanKelas: TingkatanKelas): TingkatanKelas = tingkatanKelasRepository.save(tingkatanKelas)
    fun hapus(id: Long) = tingkatanKelasRepository.deleteById(id)
}