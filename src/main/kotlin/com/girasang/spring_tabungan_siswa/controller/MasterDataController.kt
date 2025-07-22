package com.girasang.spring_tabungan_siswa.controller

import com.girasang.spring_tabungan_siswa.model.Kelas
import com.girasang.spring_tabungan_siswa.service.KelasService
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.ComboBox
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.URL
import java.util.Optional
import java.util.ResourceBundle

@Component
class MasterDataController @Autowired constructor(
    val kelasService: KelasService
) : Initializable {


    @FXML private lateinit var txtNamaKelas: TextField
    @FXML private lateinit var cboKelas: ComboBox<String>
    @FXML private lateinit var cboTahunAjaran: ComboBox<String>

    @FXML private lateinit var tblKelas: TableView<Kelas>
    @FXML private lateinit var idKolom: TableColumn <Kelas, Long>
    @FXML private lateinit var tahunAjaranKolom: TableColumn <Kelas, String>
    @FXML private lateinit var namaKelasKolom: TableColumn <Kelas, String>
    @FXML private lateinit var tingkatKolom: TableColumn <Kelas, String>

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        val tingkatan = listOf(
            "I",
            "II",
            "III",
            "IV",
            "V",
            "VI")
        cboKelas.items.addAll(tingkatan)
        dataTabel()
        loadData()
    }

    fun refresh(){
        txtNamaKelas.clear()
        cboKelas.selectionModel.clearSelection()
        cboTahunAjaran.selectionModel.clearSelection()
    }
    fun loadData(){
        val daftarKelas = kelasService.cariSemua()
        tblKelas.items = FXCollections.observableArrayList(daftarKelas)
        tblKelas.selectionModel.clearSelection()

        refresh()
    }
    fun dataTabel(){
        idKolom.setCellValueFactory { ReadOnlyObjectWrapper(it.value.id) }
        tahunAjaranKolom.setCellValueFactory { ReadOnlyStringWrapper(it.value.tahunAjaran)}
        namaKelasKolom.setCellValueFactory { ReadOnlyStringWrapper(it.value.namaKelas)}
        tingkatKolom.setCellValueFactory { ReadOnlyStringWrapper(it.value.tingkatKelas) }
    }
    fun onSimpan(){
        if(txtNamaKelas.text == "" ||
            cboTahunAjaran.value == "" ||
            cboKelas.value ==""
            ) {
            tampilPeringatan("Data Tidak Lengkap")
        }else{
            val selected = tblKelas.selectionModel.selectedItem
            if (selected == null) {
                val konfirmasi = tampilKonfirmasi("Apakah Anda yakin ingin Menyimpan data ini?")
                if(konfirmasi) {
                    val kelas = Kelas(
                        tahunAjaran = cboTahunAjaran.value,
                        tingkatKelas = cboKelas.value,
                        namaKelas = txtNamaKelas.text
                    )
                    kelasService.simpan(kelas)
                }
            } else {
                val konfirmasi = tampilKonfirmasi("Apakah Anda yakin ingin Menyimpan perubahan data ini?")
                if (konfirmasi) {
                    val kelas = Kelas(
                        id = selected.id,
                        tahunAjaran = cboTahunAjaran.value,
                        tingkatKelas = cboKelas.value,
                        namaKelas = txtNamaKelas.text
                    )
                    kelasService.simpan(kelas)
                }
            }
            println("Data Kelas ${txtNamaKelas.text} Disimpan")
            loadData()
        }
    }
    fun onHapus(){
        val selected = tblKelas.selectionModel.selectedItem
        if(selected != null){
            val konfirmasi = tampilKonfirmasi("Apakah Anda yakin ingin menghapus data ini?")
            if(konfirmasi) {
                kelasService.hapus(selected.id)
                println("Data Kelas ${txtNamaKelas.text} Dihapus")
            }
            loadData()
        }
    }
    fun onBatal(){
        loadData()
    }
    @FXML fun tabelClick(event: MouseEvent){
        if(event.clickCount == 1){
            val selected = tblKelas.selectionModel.selectedItem
            if(selected != null){
                val kelas = kelasService.cariId(selected.id)
                cboTahunAjaran.value = kelas.get().tahunAjaran
                txtNamaKelas.text = kelas.get().namaKelas
                cboKelas.value = kelas.get().tingkatKelas
            }
        }
    }
    fun tampilPeringatan(message: String) {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = "Peringatan"
        alert.contentText = message
        alert.showAndWait()
    }
    fun tampilKonfirmasi(message: String): Boolean {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Konfirmasi"
        alert.headerText = null
        alert.contentText = message

        val result: Optional<ButtonType> = alert.showAndWait()
        return result.isPresent && result.get() == ButtonType.OK
    }
}