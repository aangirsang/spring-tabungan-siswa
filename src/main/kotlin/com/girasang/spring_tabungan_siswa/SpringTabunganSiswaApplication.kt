package com.girasang.spring_tabungan_siswa

import com.girasang.spring_tabungan_siswa.config.ApplicationContextProvider
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.util.Callback
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext

@SpringBootApplication(scanBasePackages = ["com.girasang.spring_tabungan_siswa"])
open class SpringTabunganSiswaApplication : Application(){

	private lateinit var applicationContext: ConfigurableApplicationContext
	override fun init() {
		applicationContext = SpringApplication.run(SpringTabunganSiswaApplication::class.java)
	}
	override fun start(primaryStage: Stage) {
		val fxml = javaClass.getResource("/view/dashboard.fxml")
		val loader = FXMLLoader(fxml)

		// Gunakan Spring untuk inject controller
		loader.controllerFactory = Callback { clazz ->
			ApplicationContextProvider.getContext().getBean(clazz)
		}
		val root: Parent = loader.load()
		val scene = Scene(root)

		scene.stylesheets.add(javaClass.getResource("/style/style-app.css").toExternalForm())

		primaryStage?.scene = scene
		primaryStage?.title = "POS App"
		primaryStage?.show()
		primaryStage?.isMaximized = true

	}
	override fun stop() {
		applicationContext.close()
	}

}

fun main(args: Array<String>) {
	Application.launch(SpringTabunganSiswaApplication::class.java, *args)
}
