package com.girasang.spring_tabungan_siswa.controller

import com.girasang.spring_tabungan_siswa.config.ApplicationContextProvider
import com.girasang.spring_tabungan_siswa.model.Siswa
import com.girasang.spring_tabungan_siswa.service.SiswaService
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.util.Callback
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.URL
import java.time.LocalDate
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
    }

    fun tampilSiswaDialog(siswa: Siswa){
        val loader = FXMLLoader(javaClass.getResource("/org/girsang/pos/view/jabatan.fxml"))
        loader.controllerFactory = Callback { clazz ->
            ApplicationContextProvider.getContext().getBean(clazz)
        }

        val root = loader.load<Parent>()
        val stage = Stage()
        stage.title = "Jabatan"
        stage.scene = Scene(root)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.showAndWait()
    }

    fun onData(){
        val selected = tblSiswa.selectionModel.selectedItem
        if (selected==null){
            val siswa : Siswa
        }
    }
}