package com.girasang.spring_tabungan_siswa.repository

import com.girasang.spring_tabungan_siswa.model.Kelas
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface KelasRepository : JpaRepository<Kelas, Long>