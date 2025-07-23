package com.girasang.spring_tabungan_siswa.controller

import com.girasang.spring_tabungan_siswa.SpringTabunganSiswaApplication
import com.girasang.spring_tabungan_siswa.config.ApplicationContextProvider
import com.girasang.spring_tabungan_siswa.service.KelasService
import javafx.animation.FadeTransition
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.util.Callback
import javafx.util.Duration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.net.URL
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.ResourceBundle

@Component
class DashboardController @Autowired constructor(
    val kelasService: KelasService

) : Initializable {
    @FXML private lateinit var sidebar: VBox
    @FXML private lateinit var mainContent: StackPane
    @FXML private lateinit var lblJam: Label

    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    private fun loadPage(page: String) {
        val resource = javaClass.getResource("/view/$page.fxml")
            ?: throw IllegalStateException("FXML /view/$page.fxml not found")

        val loader = FXMLLoader(resource)
        loader.controllerFactory = Callback { clazz ->
            ApplicationContextProvider.getContext().getBean(clazz)
        }

        val node: Node = loader.load()

        val fadeOut = FadeTransition(Duration.millis(150.0), mainContent)
        fadeOut.fromValue = 1.0
        fadeOut.toValue = 0.0
        fadeOut.setOnFinished {
            mainContent.children.setAll(node)

            val fadeIn = FadeTransition(Duration.millis(150.0), mainContent)
            fadeIn.fromValue = 0.0
            fadeIn.toValue = 1.0
            fadeIn.play()
        }
        fadeOut.play()
    }

    fun onMasterData() = loadPage("master-data")
    fun loadDataSiswa() = loadPage("siswa-daftar")
    fun loadPenjualan() = loadPage("penjualan")
    fun loadPembelian() = loadPage("pembelian")
    fun loadLainnya() = loadPage("lainnya")

    fun loadJam(){
        val timeline = Timeline(
            KeyFrame(Duration.seconds(0.0), EventHandler {
                lblJam.text = LocalTime.now().format(formatter)
            }),
            KeyFrame(Duration.seconds(1.0))
        )
        timeline.cycleCount = Timeline.INDEFINITE
        timeline.play()
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        loadJam()
    }
}