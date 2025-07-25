package com.girasang.spring_tabungan_siswa.controller

import com.girasang.spring_tabungan_siswa.config.ApplicationContextProvider
import com.girasang.spring_tabungan_siswa.model.Siswa
import com.girasang.spring_tabungan_siswa.service.SiswaService
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.input.MouseEvent
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Callback
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.net.URL
import java.time.LocalDate
import java.util.Optional
import java.util.ResourceBundle

@Component
class SiswaDaftarController @Autowired constructor(
    val siswaService: SiswaService
) : Initializable{

    @FXML private lateinit var tblSiswa: TableView<Siswa>
    @FXML private lateinit var idKolom: TableColumn <Siswa, Long>
    @FXML private lateinit var nisnKolom: TableColumn <Siswa, String>
    @FXML private lateinit var namaKolom: TableColumn <Siswa, String>
    @FXML private lateinit var jenisKelaminKolom: TableColumn <Siswa, String>
    @FXML private lateinit var tempatlLahirKolom: TableColumn <Siswa, String>
    @FXML private lateinit var tanggalLahirKolom: TableColumn <Siswa, LocalDate>
    @FXML private lateinit var agamaKolom: TableColumn <Siswa, String>
    @FXML private lateinit var tanggalMasukKolom: TableColumn <Siswa, LocalDate>
    @FXML private lateinit var statusKolom: TableColumn <Siswa, Boolean>
    @FXML private lateinit var btnTambah: Button

    val pathGambar = "images"
    override fun initialize(p0: URL?, p1: ResourceBundle?) {tampilData()
    }
    fun tampilData(){
        val daftarSiswa = siswaService.cariSemua()
        tblSiswa.items = FXCollections.observableArrayList(daftarSiswa)
        idKolom.setCellValueFactory{ReadOnlyObjectWrapper(it.value.id) }
        nisnKolom.setCellValueFactory{ReadOnlyStringWrapper(it.value.nisn)}
        namaKolom.setCellValueFactory{ReadOnlyStringWrapper(it.value.namaLengkap)}
        jenisKelaminKolom.setCellValueFactory{ReadOnlyStringWrapper(it.value.jenisKelamin)}
        tempatlLahirKolom.setCellValueFactory{ReadOnlyStringWrapper(it.value.tempatLahir)}
        tanggalLahirKolom.setCellValueFactory{ReadOnlyObjectWrapper(it.value.tanggalLahir)}
        agamaKolom.setCellValueFactory{ReadOnlyStringWrapper(it.value.agama)}
        tanggalMasukKolom.setCellValueFactory{ReadOnlyObjectWrapper(it.value.tanggalMasuk)}
        statusKolom.setCellValueFactory{ReadOnlyObjectWrapper(it.value.status)}

        tblSiswa.selectionModel.clearSelection()
        btnTambah.text = "Data Baru"
    }

    fun tampilSiswaDialog(siswa: Siswa){
        try {
            val loader = FXMLLoader(javaClass.getResource("/view/siswa-dialog.fxml"))
            loader.controllerFactory = Callback { clazz ->
                ApplicationContextProvider.getContext().getBean(clazz)
            }

            val root = loader.load<Parent>()

            val controller = loader.getController<SiswaDialogController>()
            controller.setSiswa(siswa)

            val stage = Stage()
            stage.scene = Scene(root)
            stage.initStyle(StageStyle.UNDECORATED)
            stage.initModality(Modality.WINDOW_MODAL)
            stage.initOwner(btnTambah.scene.window)
            stage.isResizable = false

            stage.setOnCloseRequest { it.consume() }
            stage.showAndWait()
            println("Ditutup")
            tampilData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun tampilKonfirmasi(message: String): Boolean {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Konfirmasi"
        alert.headerText = null
        alert.contentText = message

        val result: Optional<ButtonType> = alert.showAndWait()
        return result.isPresent && result.get() == ButtonType.OK
    }

    @FXML fun onData(event: ActionEvent){
        val selected = tblSiswa.selectionModel.selectedItem
        var siswa : Siswa
        if(selected ==null ){
            siswa = Siswa()
            tampilSiswaDialog(siswa)
        } else{
            tampilSiswaDialog(selected)
        }
    }
    fun onHapus(){
        val selected = tblSiswa.selectionModel.selectedItem
        if(selected!=null){
            val konfirmasi = tampilKonfirmasi("Apakah Anda yakin ingin menghapus data ini?")
            if(konfirmasi){
                siswaService.hapus(selected.id)
                hapusGambarSiswa(selected.id)
                tampilData()
            }
        }
    }
    fun onRefresh(){
        tampilData()
    }
    fun tblSiswaKlik(event: MouseEvent){
        if(event.clickCount==1){
            val selected = tblSiswa.selectionModel.selectedItem
            if(selected != null){
                btnTambah.text = "Ubah Data"
            }
        }
        if(event.clickCount==2){
            val selected = tblSiswa.selectionModel.selectedItem
            if(selected != null){
                val siswa = siswaService.cariId(selected.id)
                tampilSiswaDialog(selected)
            }
        }
    }
    fun hapusGambarSiswa(siswaId: Long) {
        val folder = File(pathGambar)
        println("Cek folder: ${folder.absolutePath}")

        if (!folder.exists()) {
            println("Folder tidak ditemukan.")
            return
        }

        val files = folder.listFiles()
        if (files.isNullOrEmpty()) {
            println("Folder kosong atau tidak bisa diakses.")
            return
        }

        val fileFoto = files.firstOrNull {
            it.nameWithoutExtension == siswaId.toString()
        }

        if (fileFoto == null) {
            println("Gambar siswa $siswaId tidak ditemukan di folder.")
        } else {
            if (fileFoto.exists()) {
                fileFoto.setWritable(true)
                fileFoto.setReadable(true)
                val deleted = fileFoto.delete()
                println("File ${fileFoto.name} dihapus paksa: $deleted")
            }
        }
    }

}