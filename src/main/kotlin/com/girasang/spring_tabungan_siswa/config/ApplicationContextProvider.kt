package com.girasang.spring_tabungan_siswa.config

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
object ApplicationContextProvider : ApplicationContextAware {

    private lateinit var context: ApplicationContext

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

    fun getContext(): ApplicationContext = context
}
