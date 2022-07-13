package hr.asimr.shows_asim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.asimr.shows_asim.databinding.ActivityWelcomeBinding
import hr.asimr.shows_asim.utils.loseEmailDomain

const val WELCOME = "Welcome"
const val EMAIL_KEY = "Email"

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkIntentForExtras()
    }

    private fun checkIntentForExtras() {
        val name = intent.extras?.getString(EMAIL_KEY)?.let { it.loseEmailDomain() }
        name?.let { changeWelcomeMessage(it) }
    }

    private fun changeWelcomeMessage(name: String) {
        binding.tvWelcome.text = WELCOME.plus(", ").plus(name)
    }
}