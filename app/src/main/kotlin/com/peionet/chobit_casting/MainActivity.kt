package com.peionet.chobit_casting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.peionet.chobit_casting.ui.MovieListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MovieListFragment())
            .commit()
    }

}
