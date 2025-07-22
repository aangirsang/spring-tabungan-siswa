package com.girasang.spring_tabungan_siswa.controller

import com.girasang.spring_tabungan_siswa.SpringTabunganSiswaApplication
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.util.Callback
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext

class AppController : Application() {
    companion object {
        lateinit var context: ApplicationContext
    }

    override fun init() {
        context = runApplication<SpringTabunganSiswaApplication>()
    }

    override fun start(stage: Stage) {
        val fxml = javaClass.getResource("/view/dashboard.fxml")
            ?: throw IllegalStateException("main.fxml not found")

        val loader = FXMLLoader(fxml)
        loader.controllerFactory = Callback { clazz ->
            context.getBean(clazz as Class<*>) as Any
        }

        val scene = Scene(loader.load())
        stage.title = "Aplikasi POS"
        stage.scene = scene
        stage.isMaximized = true
        stage.show()
    }
}