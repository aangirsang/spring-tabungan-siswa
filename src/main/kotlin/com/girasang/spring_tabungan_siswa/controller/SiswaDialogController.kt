package com.girasang.spring_tabungan_siswa.controller

import com.girasang.spring_tabungan_siswa.model.Siswa
import com.girasang.spring_tabungan_siswa.service.SiswaService
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.DatePicker
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.stage.Stage
import javafx.util.StringConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Optional
import java.util.ResourceBundle

@Component
class SiswaDialogController @Autowired constructor (
    val siswaService: SiswaService
) : Initializable{

    private var siswa: Siswa? = null
    @FXML private lateinit var btnBatal: Button
    @FXML private lateinit var txtNISN: TextField
    @FXML private lateinit var txtNamaLengkap: TextField
    @FXML private lateinit var txtTempatLahir: TextField
    @FXML private lateinit var dpTanggalLahir: DatePicker
    @FXML private lateinit var cboJenisKelamin: ComboBox<String>
    @FXML private lateinit var cboAgama: ComboBox<String>
    @FXML private lateinit var txtAlamat: TextArea
    @FXML private lateinit var dpMulaiSekolah: DatePicker
    @FXML private lateinit var cbStatus: CheckBox
    @FXML private lateinit var txtKeterangan: TextField

    fun setSiswa(siswa: Siswa){
        this.siswa = siswa
        isiData(siswa!!)
        println("Ada data Siswa ${siswa!!.namaLengkap}")
    }
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        formatTanggal(dpTanggalLahir)
        formatTanggal(dpMulaiSekolah)
        isiCombo()
    }
    fun onSimpan(){
        validasiData()
        if(siswa==null){
            val konfirmasi = tampilKonfirmasi("Apakah Anda yakin ingin Menyimpan data ini ?")
            if(konfirmasi) {
                val siswa = Siswa (
                    namaLengkap = txtNamaLengkap.text,
                    jenisKelamin = cboJenisKelamin.selectionModel.selectedItem,
                    nisn = txtNISN.text,
                    tempatLahir = txtTempatLahir.text,
                    tanggalLahir = dpTanggalLahir.value,
                    agama = cboAgama.selectionModel.selectedItem,
                    alamatLengkap = txtAlamat.text,
                    tanggalMasuk = dpMulaiSekolah.value,
                    status = cbStatus.isSelected,
                    keterangan = txtKeterangan.text
                )
                siswaService.simpan(siswa)
            }
        }else{
            val konfirmasi = tampilKonfirmasi("Apakah Anda yakin ingin Merubah data ini ?")
            if(konfirmasi) {
                val siswa = Siswa (
                    id = siswa!!.id,
                    namaLengkap = txtNamaLengkap.text,
                    jenisKelamin = cboJenisKelamin.selectionModel.selectedItem,
                    nisn = txtNISN.text,
                    tempatLahir = txtTempatLahir.text,
                    tanggalLahir = dpTanggalLahir.value,
                    agama = cboAgama.selectionModel.selectedItem,
                    alamatLengkap = txtAlamat.text,
                    tanggalMasuk = dpMulaiSekolah.value,
                    status = cbStatus.isSelected,
                    keterangan = txtKeterangan.text
                )
                siswaService.simpan(siswa)
            }
        }
        val stage = (btnBatal.scene.window as Stage)
        stage.close()
    }
    fun onBatal(){
        val stage = (btnBatal.scene.window as Stage)
        stage.close()
    }

    fun formatTanggal(datePicker: DatePicker){
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

        datePicker.converter = object : StringConverter<LocalDate>() {
            override fun toString(date: LocalDate?): String {
                return date?.format(formatter) ?: ""
            }

            override fun fromString(string: String?): LocalDate? {
                return if (string.isNullOrBlank()) null else LocalDate.parse(string, formatter)
            }
        }

        datePicker.promptText = "dd MMMM yyyy"
    }

    fun isiCombo(){
        val jenisKelaminList = listOf("Laki-laki", "Perempuan")
        cboJenisKelamin.items = FXCollections.observableArrayList(jenisKelaminList)
        cboJenisKelamin.selectionModel.clearSelection()

        val agamaList = listOf("Islam", "Protestan", "Katolik", "Hindu", "Buddha", "Konghucu")
        cboAgama.items = FXCollections.observableArrayList(agamaList)
        cboAgama.selectionModel.clearSelection()
    }
    fun isiData(siswa: Siswa){
        txtNISN.text = siswa.nisn
        txtNamaLengkap.text = siswa.namaLengkap
        txtTempatLahir.text = siswa.tempatLahir
        dpTanggalLahir.value = siswa.tanggalLahir
        cboJenisKelamin.selectionModel.select(siswa.jenisKelamin)
        cboAgama.selectionModel.select(siswa.agama)
        txtAlamat.text = siswa.alamatLengkap
        dpMulaiSekolah.value = siswa.tanggalMasuk
        cbStatus.isSelected = siswa.status
        txtKeterangan.text = siswa.keterangan
    }
    fun validasiData(){
        if(txtNISN.text == "") return
        if(txtNamaLengkap.text == "") return
        if(txtTempatLahir.text == "") return
        if(dpTanggalLahir.value == null) return
        if (cboJenisKelamin.selectionModel.selectedItem == null) return
        if (cboAgama.selectionModel.selectedItem == null) return
        if (txtAlamat.text == "") return
        if (dpMulaiSekolah.value == null) return
        if (txtKeterangan.text == "") return
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