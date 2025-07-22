package com.girasang.spring_tabungan_siswa.controller

import com.girasang.spring_tabungan_siswa.model.Kelas
import com.girasang.spring_tabungan_siswa.model.TahunAjaran
import com.girasang.spring_tabungan_siswa.model.TingkatanKelas
import com.girasang.spring_tabungan_siswa.service.KelasService
import com.girasang.spring_tabungan_siswa.service.TahunAjaranService
import com.girasang.spring_tabungan_siswa.service.TingkatanKelasService
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
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
    val kelasService: KelasService,
    val tahunAjaranService: TahunAjaranService,
    val tingkatanKelasService: TingkatanKelasService
) : Initializable {


    @FXML private lateinit var txtNamaKelas: TextField
    @FXML private lateinit var txtTahunAjaran: TextField
    @FXML private lateinit var txtTingkatKelas: TextField
    @FXML private lateinit var cboKelas: ComboBox<String>
    @FXML private lateinit var cboTahunAjaran: ComboBox<String>

    @FXML private lateinit var tblKelas: TableView<Kelas>
    @FXML private lateinit var idKolom: TableColumn <Kelas, Long>
    @FXML private lateinit var kelasTahunAjaranKolom: TableColumn <Kelas, String>
    @FXML private lateinit var namaKelasKolom: TableColumn <Kelas, String>
    @FXML private lateinit var tingkatKolom: TableColumn <Kelas, String>

    @FXML private lateinit var tblTahunAjaran: TableView<TahunAjaran>
    @FXML private lateinit var idTahunAjaranKolom: TableColumn <TahunAjaran, Long>
    @FXML private lateinit var tahunAjaranKolom: TableColumn <TahunAjaran, String>

    @FXML private lateinit var tblTingkatKelas: TableView<TingkatanKelas>
    @FXML private lateinit var idtTingkatKelasKolom: TableColumn <TingkatanKelas, Long>
    @FXML private lateinit var tingkatKelasKolom: TableColumn <TingkatanKelas, String>

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        loadData()
    }

    fun bersih(){
        txtNamaKelas.clear()
        txtTahunAjaran.clear()
        txtTingkatKelas.clear()
        cboKelas.selectionModel.clearSelection()
        cboTahunAjaran.selectionModel.clearSelection()
    }
    fun loadData(){
        val daftarKelas = kelasService.cariSemua()
        val daftarTahunAjaran = tahunAjaranService.cariSemua()
        val daftarTingkatanKelas = tingkatanKelasService.cariSemua()
        tblKelas.items = FXCollections.observableArrayList(daftarKelas)
        tblTahunAjaran.items = FXCollections.observableArrayList(daftarTahunAjaran)
        tblTingkatKelas.items = FXCollections.observableArrayList(daftarTingkatanKelas)
        tblKelas.selectionModel.clearSelection()
        tblTahunAjaran.selectionModel.clearSelection()
        tblTingkatKelas.selectionModel.clearSelection()

        bersih()
        tampilTabelTahunAjaran()
        tampilTabelTingkatKelas()
        loadCombo()
    }
    fun loadCombo(){
        val daftarTingkatKelas =tingkatanKelasService.cariSemua()
        val daftarTahunAjaran = tahunAjaranService.cariSemua()
        val tingkatKelass = daftarTingkatKelas.map { it.tingkatanKelas }
        val tahunAjarans = daftarTahunAjaran.map { it.tahunAjaran }
        cboKelas.items = FXCollections.observableArrayList(tingkatKelass)
        cboTahunAjaran.items = FXCollections.observableArrayList( tahunAjarans)

        cboTahunAjaran.selectionModel.clearSelection()
        cboKelas.selectionModel.clearSelection()
    }
    fun tampilTabelKelas(){
        idKolom.setCellValueFactory { ReadOnlyObjectWrapper(it.value.id) }
        kelasTahunAjaranKolom.setCellValueFactory { ReadOnlyStringWrapper(it.value.tahunAjaran?.tahunAjaran)}
        namaKelasKolom.setCellValueFactory { ReadOnlyStringWrapper(it.value.namaKelas)}
        tingkatKolom.setCellValueFactory { ReadOnlyStringWrapper(it.value.tingkatanKelas?.tingkatanKelas) }
    }
    fun tampilTabelTahunAjaran(){
        idTahunAjaranKolom.setCellValueFactory{ReadOnlyObjectWrapper(it.value.id)}
        tahunAjaranKolom.setCellValueFactory{ReadOnlyStringWrapper(it.value.tahunAjaran)}
    }
    fun tampilTabelTingkatKelas(){
        idtTingkatKelasKolom.setCellValueFactory{ReadOnlyObjectWrapper(it.value.id)}
        tingkatKelasKolom.setCellValueFactory{ReadOnlyStringWrapper(it.value.tingkatanKelas)}
    }
    fun onSimpanKelas(){
//        if(txtNamaKelas.text == "" ||
//            cboTahunAjaran.value == "" ||
//            cboKelas.value ==""
//            ) {
//            tampilPeringatan("Data Tidak Lengkap")
//        }else{
//            val selected = tblKelas.selectionModel.selectedItem
//            if (selected == null) {
//                val konfirmasi = tampilKonfirmasi("Apakah Anda yakin ingin Menyimpan data ini?")
//                if(konfirmasi) {
//                    val kelas = Kelas(
//                        tahunAjaran = cboTahunAjaran.value,
//                        tingkatKelas = cboKelas.value,
//                        namaKelas = txtNamaKelas.text
//                    )
//                    kelasService.simpan(kelas)
//                }
//            } else {
//                val konfirmasi = tampilKonfirmasi("Apakah Anda yakin ingin Menyimpan perubahan data ini?")
//                if (konfirmasi) {
//                    val kelas = Kelas(
//                        id = selected.id,
//                        tahunAjaran = cboTahunAjaran.value,
//                        tingkatKelas = cboKelas.value,
//                        namaKelas = txtNamaKelas.text
//                    )
//                    kelasService.simpan(kelas)
//                }
//            }
//            println("Data Kelas ${txtNamaKelas.text} Disimpan")
//            loadData()
//        }
    }
    fun onHapusKelas(){
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
    fun onBatalKelas(){
        loadData()
    }

    fun onSimpanTahunAjaran(){
        if(txtTahunAjaran.text == "") return
        val tahunAjaran : TahunAjaran
        val selected = tblTahunAjaran.selectionModel.selectedItem
        if(selected==null){
            val konfirmasi = tampilKonfirmasi("Apakah Anda yakin ingin Menyimpan data ini ?")
            if(konfirmasi) {
                tahunAjaran = TahunAjaran(tahunAjaran = txtTahunAjaran.text)
                tahunAjaranService.simpan(tahunAjaran)
                println("Tahun Ajaran ${tahunAjaran.tahunAjaran} disimpan")
            }
        } else {
            val konfirmasi = tampilKonfirmasi("Apakah Anda yakin ingin Menyimpan perubahan data ini ?")
            if(konfirmasi) {
                tahunAjaran = TahunAjaran(id = selected.id, tahunAjaran = txtTahunAjaran.text)
                tahunAjaranService.simpan(tahunAjaran)
                println("Tahun Ajaran ${tahunAjaran.tahunAjaran} disimpan")
            }
        }
        loadData()
    }

    fun onHapusTahunAjaran(){
        val selected = tblTahunAjaran.selectionModel.selectedItem
        if(selected!=null){
            val konfirmasi = tampilKonfirmasi("Apakah Anda yakin ingin Menghapus data ini ?")
            if(konfirmasi) {
                tahunAjaranService.hapus(selected.id)
                println("Tahun Ajaran ${selected.tahunAjaran} disimpan")
            }
        }
        loadData()
    }

    fun onSimpanTingkatanKelas(){
        if(txtTingkatKelas.text=="") return
        val tingkatanKelas : TingkatanKelas
        val selected = tblTingkatKelas.selectionModel.selectedItem
        if(selected==null){
            val konfirmasi = tampilKonfirmasi("Apakah Anda yakin ingin Menyimpan data ini ?")
            if(konfirmasi) {
                tingkatanKelas = TingkatanKelas(tingkatanKelas = txtTingkatKelas.text)
                tingkatanKelasService.simpan(tingkatanKelas)
            }
        } else{
            val konfirmasi = tampilKonfirmasi("Apakah Anda yakin ingin Menyimpan perubahan data ini ?")
            if(konfirmasi) {
                tingkatanKelas = TingkatanKelas(id = selected.id, tingkatanKelas = txtTingkatKelas.text)
                tingkatanKelasService.simpan(tingkatanKelas)
            }
        }
        loadData()
    }

    fun onHapusTingkatanKelas(){
        val selected = tblTingkatKelas.selectionModel.selectedItem
        if(selected!=null){
            val konfirmasi = tampilKonfirmasi("Apakah Anda yakin ingin Menghapus data ini ?")
            if(konfirmasi){
                tingkatanKelasService.hapus(selected.id)
            }
        }
        loadData()
    }
    @FXML fun tblKelasKlik(event: MouseEvent){
//        if(event.clickCount == 1){
//            val selected = tblKelas.selectionModel.selectedItem
//            if(selected != null){
//                val kelas = kelasService.cariId(selected.id)
//                cboTahunAjaran.value = kelas.get().tahunAjaran
//                txtNamaKelas.text = kelas.get().namaKelas
//                cboKelas.value = kelas.get().tingkatKelas
//            }
//        }
    }
    fun tblTingkatanKelasKlik(event: MouseEvent){
        if(event.clickCount == 1){
            val selected = tblTingkatKelas.selectionModel.selectedItem
            if(selected != null){
                val tingkatanKelas = tingkatanKelasService.cariId(selected.id)
                txtTingkatKelas.text = tingkatanKelas.get().tingkatanKelas
            }
        }
    }fun tblTahunAjaranKlik(event: MouseEvent){
        if(event.clickCount == 1){
            val selected = tblTahunAjaran.selectionModel.selectedItem
            if(selected != null){
                val tahunAjaran = tahunAjaranService.cariId(selected.id)
                txtTahunAjaran.text = tahunAjaran.get().tahunAjaran
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