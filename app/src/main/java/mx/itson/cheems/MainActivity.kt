package mx.itson.cheems

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var gameOverCard = 0
    var conteo = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        start()

        findViewById<Button>(R.id.button_restart).setOnClickListener {
            restart()
        }
    }

    fun start() {
        for(i in 1..6){
            val btnCard = findViewById<View>(
                resources.getIdentifier("card$i", "id", this.packageName)
            ) as ImageButton
            btnCard.setOnClickListener(this)
            btnCard.setBackgroundResource(R.drawable.icon_pregunta)
            btnCard.isEnabled = true
        }
        gameOverCard = (1 .. 6).random()

        Log.d("El valor de la carta", "La carta perdedora es ${gameOverCard.toString()}")
    }

    fun flip(card : Int){
        if(card == gameOverCard){
            // Ya perdió

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                // Si la versión de Android del usuario es mayor o igual a la versión 12
                val vibratorAdmin = applicationContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                val vibrator = vibratorAdmin.defaultVibrator
                vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                // Si es menor a la 12, lo va a hacer de esta manera
                val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(1000)
            }


            Toast.makeText(this, getString(R.string.text_game_over), Toast.LENGTH_LONG).show()

            for(i in 1..6){
                val btnCard = findViewById<View>(
                    resources.getIdentifier("card$i", "id", this.packageName)
                ) as ImageButton
                if(i == card) {
                    btnCard.setBackgroundResource(R.drawable.icon_chempe)
                }else {
                    btnCard.setBackgroundResource(R.drawable.icon_cheems)
                }
            }
        } else {
            //Continúa en el juego
            val btnCard = findViewById<View>(
                resources.getIdentifier("card$card", "id", this.packageName)
            ) as ImageButton
            btnCard.setBackgroundResource(R.drawable.icon_cheems)
            btnCard.isEnabled = false

            conteo++
            if(conteo == 5){
                //Ya gano
                Toast.makeText(this, getString(R.string.text_win), Toast.LENGTH_LONG).show()

                for (i in 1..6) {
                    val btnCard = findViewById<View>(
                        resources.getIdentifier("card$i", "id", this.packageName)
                    ) as ImageButton
                    if (btnCard.isEnabled) {
                        btnCard.isEnabled = false
                        break
                    }
                }
            }
        }
    }

    fun restart() {
        conteo = 0
        start()
    }


    override fun onClick(v: View) {
        when(v.id){
            R.id.card1 -> { flip(1)}
            R.id.card2 -> { flip(2)}
            R.id.card3 -> { flip(3)}
            R.id.card4 -> { flip(4)}
            R.id.card5 -> { flip(5)}
            R.id.card6 -> { flip(6)}


        }
    }
}