package com.girasang.spring_tabungan_siswa.controller

import com.girasang.spring_tabungan_siswa.model.Kelas
import com.girasang.spring_tabungan_siswa.service.KelasService
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ComboBox
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.URL
import java.util.ResourceBundle

@Component
class MasterDataController @Autowired constructor(
    val kelasService: KelasService
) : Initializable {


    @FXML private lateinit var txtNamaKelas: TextField
    @FXML private lateinit var txtTahunAjaran: TextField
    @FXML private lateinit var cboKelas: ComboBox<String>

    @FXML private lateinit var tblKelas: TableView<Kelas>
    @FXML private lateinit var idKolom: TableColumn <Kelas, Long>
    @FXML private lateinit var tahunAjaranKolom: TableColumn <Kelas, String>
    @FXML private lateinit var namaKelasKolom: TableColumn <Kelas, String>
    @FXML private lateinit var tingkatKolom: TableColumn <Kelas, Int>

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        dataTabel()
        loadData()
    }

    fun refresh(){
        val tingkatan = listOf(
            "I",
            "II",
            "III",
            "IV",
            "V",
            "VI")
        cboKelas.items.addAll(tingkatan)

        txtNamaKelas.clear()
        txtTahunAjaran.clear()

    }
    fun loadData(){
        val daftarKelas = kelasService.cariSemua()
        tblKelas.items.addAll(daftarKelas)
        tblKelas.selectionModel.clearSelection()

        refresh()
    }
    fun dataTabel(){
        idKolom.setCellValueFactory { ReadOnlyObjectWrapper(it.value.id) }
        tahunAjaranKolom.setCellValueFactory { ReadOnlyStringWrapper(it.value.tahunAjaran)}
        namaKelasKolom.setCellValueFactory { ReadOnlyStringWrapper(it.value.namaKelas)}
        tingkatKolom.setCellValueFactory { ReadOnlyObjectWrapper(it.value.tingkatKelas) }
    }
    fun onSimpan(){
        val kelas = Kelas(
            tahunAjaran = txtTahunAjaran.text,
            tingkatKelas = cboKelas.selectionModel.selectedIndex,
            namaKelas = txtNamaKelas.text
            )
        kelasService.simpan(kelas)
        println("Data Kelas ${txtNamaKelas.text} Disimpan")
        loadData()
    }
    fun onHapus(){

    }
    fun onBatal(){
        loadData()
    }
}