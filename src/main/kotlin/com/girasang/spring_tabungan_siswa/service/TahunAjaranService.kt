package com.girasang.spring_tabungan_siswa.service

import com.girasang.spring_tabungan_siswa.model.TahunAjaran
import com.girasang.spring_tabungan_siswa.repository.TahunAjaranRepository
import org.springframework.stereotype.Service

@Service
class TahunAjaranService (private val tahunAjaranRepository: TahunAjaranRepository){
    fun cariSemua(): List<TahunAjaran> =tahunAjaranRepository.findAll()
    fun cariId(id: Long) = tahunAjaranRepository.findById(id)
    fun simpan(tahunAjaran: TahunAjaran): TahunAjaran = tahunAjaranRepository.save(tahunAjaran)
    fun hapus(id: Long) = tahunAjaranRepository.deleteById(id)
}