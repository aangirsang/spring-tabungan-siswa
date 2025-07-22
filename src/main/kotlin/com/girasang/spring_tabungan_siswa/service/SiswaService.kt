package com.girasang.spring_tabungan_siswa.service

import com.girasang.spring_tabungan_siswa.model.Siswa
import com.girasang.spring_tabungan_siswa.repository.SiswaRepository
import org.springframework.stereotype.Service

@Service
class SiswaService (private val siswaRepository: SiswaRepository) {
    fun cariSemua(): List<Siswa> = siswaRepository.findAll()
    fun cariId(id: Long) = siswaRepository.findById(id)
    fun simpan(siswa: Siswa) : Siswa = siswaRepository.save(siswa)
    fun hapus(id: Long) = siswaRepository.deleteById(id)
}