package com.chenzhang.mvi

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.chenzhang.mvi.view.MainFragment
import com.chenzhang.recording_mvi_rx_kotlin.R
import org.apache.log4j.Logger

class MainActivity : AppCompatActivity() {

    private val LOG = Logger.getLogger(this::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        LOG.debug("onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(supportFragmentManager.findFragmentByTag("mainFragment") == null){
            supportFragmentManager.beginTransaction().apply {
                add(R.id.fragment_container, MainFragment(), "mainFragment")
                commit()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
