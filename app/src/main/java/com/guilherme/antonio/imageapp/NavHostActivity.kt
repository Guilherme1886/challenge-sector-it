package com.guilherme.antonio.imageapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.guilherme.antonio.imageapp.databinding.ActivityNavHostBinding

class NavHostActivity : AppCompatActivity() {

    private var binding: ActivityNavHostBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavHostBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}