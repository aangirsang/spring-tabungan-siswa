package com.girasang.spring_tabungan_siswa.repository

import com.girasang.spring_tabungan_siswa.model.TahunAjaran
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TahunAjaranRepository : JpaRepository<TahunAjaran, Long>