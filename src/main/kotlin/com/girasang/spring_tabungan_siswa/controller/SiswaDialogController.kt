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
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import javafx.stage.Stage
import javafx.util.StringConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Optional
import java.util.ResourceBundle
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.stream.FileImageOutputStream

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
    @FXML private lateinit var imgSiswa: ImageView
    @FXML private lateinit var btnCariGambar: Button

    private var pilihFile: File? = null
    private var pilihSiswa: Siswa? = null

    private val pathGambar = "images"

    fun setSiswa(siswa: Siswa){
        this.siswa = siswa
        isiData(siswa!!)
        loadImage(siswa)
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
                pilihFile?.let {
                    if (it.exists()) {
                        val ext = it.extension.lowercase()
                        val fileName = "${siswa.id}.$ext"
                        val targetPath = Paths.get(pathGambar, fileName)

                        Files.createDirectories(Paths.get(pathGambar)) // pastikan folder ada
                        Files.copy(it.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING)

                        imgSiswa.image = Image(FileInputStream(targetPath.toFile()))
                    }
                }
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
                pilihFile?.let {
                    if (it.exists()) {
                        hapusGambarSiswa(siswa.id) // ← tambahkan ini
                        val ext = it.extension.lowercase()
                        val fileName = "${siswa.id}.$ext"
                        val targetPath = Paths.get(pathGambar, fileName)

                        Files.createDirectories(Paths.get(pathGambar))
                        Files.copy(it.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING)

                        val bytes = targetPath.toFile().readBytes()
                        imgSiswa.image = Image(ByteArrayInputStream(bytes))
                    }
                }
            }
        }
        val stage = (btnBatal.scene.window as Stage)
        stage.close()
    }
    fun onBatal(){
        val stage = (btnBatal.scene.window as Stage)
        imgSiswa.image = null
        stage.close()
    }

    fun onBrowser(){
        val fileChooser = FileChooser()
        fileChooser.title = "Pilih Gambar"
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        )

        val file = fileChooser.showOpenDialog(btnCariGambar.scene.window)
        if (file != null && siswa?.id != null) {
            val fileName = "${siswa?.id}.jpg" // semua gambar akan disimpan sebagai JPG
            val simpanFile = File(pathGambar, fileName)

            hapusGambarSiswa(siswa!!.id) // hapus jika sudah ada
            kompresDanKonversiKeJpg(file, simpanFile, kualitas = 0.5f)

            val bytes = simpanFile.readBytes()
            imgSiswa.image = Image(ByteArrayInputStream(bytes))
        }
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
        loadImage(siswa)
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
    private fun showAlert(pesan: String) {
        val alert = Alert(Alert.AlertType.WARNING)
        alert.title = "Peringatan"
        alert.headerText = null
        alert.contentText = pesan
        alert.showAndWait()
    }
    private fun loadImage(siswa: Siswa?) {
        siswa ?: return
        val file = File("images/${siswa.id}.png")
        if (file.exists()) {
            val byteArray = file.readBytes()
            val inputStream = ByteArrayInputStream(byteArray)
            val image = Image(inputStream)
            imgSiswa.image = image
        } else {
            imgSiswa.image = null
        }
    }
    private fun hapusGambarSiswa(id: Long) {
        val folder = File(pathGambar)
        if (!folder.exists()) return

        folder.listFiles()?.forEach { file ->
            if (file.nameWithoutExtension == id.toString()) {
                try {
                    val deleted = file.delete()
                    if (deleted) {
                        println("Gambar ${file.name} berhasil dihapus")
                    } else {
                        println("Gagal menghapus gambar ${file.name}")
                    }
                } catch (e: Exception) {
                    println("Gagal menghapus gambar ${file.name}: ${e.message}")
                }
            }
        }
    }
    private fun kompresDanKonversiKeJpg(sumber: File, target: File, kualitas: Float = 0.5f) {
        try {
            val bufferedImage = ImageIO.read(sumber)

            // Konversi ke BufferedImage RGB (JPG tidak mendukung transparansi)
            val rgbImage = BufferedImage(bufferedImage.width, bufferedImage.height, BufferedImage.TYPE_INT_RGB)
            rgbImage.graphics.drawImage(bufferedImage, 0, 0, null)

            val writer = ImageIO.getImageWritersByFormatName("jpg").next()
            val param = writer.defaultWriteParam

            if (param.canWriteCompressed()) {
                param.compressionMode = ImageWriteParam.MODE_EXPLICIT
                param.compressionQuality = kualitas
            }

            FileImageOutputStream(target).use { output ->
                writer.output = output
                writer.write(null, javax.imageio.IIOImage(rgbImage, null, null), param)
                writer.dispose()
            }

        } catch (e: Exception) {
            println("Gagal konversi/kompres gambar: ${e.message}")
        }
    }
}