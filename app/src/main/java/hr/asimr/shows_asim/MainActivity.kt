package hr.asimr.shows_asim

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.asimr.shows_asim.databinding.ActivityMainBinding
import hr.asimr.shows_asim.fragments.LOGIN_PREFERENCES
import hr.asimr.shows_asim.networking.ApiModule

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}