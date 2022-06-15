package com.mkotrik.tamagopet.game

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.navigation.findNavController
import com.mkotrik.tamagopet.R

class GameActivity : AppCompatActivity() {
    private var currentFrag = 0
    private var music = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        this.createChannel()
        MusicPlayer.playSong(this, R.raw.idle)

        if (Game.isNight()) {
            val bedroomBackground = findViewById<ImageView>(R.id.bedroom_screen_iv)
            bedroomBackground.setImageResource(R.drawable.bedroom_night_screen_scaled)
            val nightLayer = findViewById<View>(R.id.night_layer)
            nightLayer.setBackgroundColor(resources.getColor(R.color.night_layer))
            MusicPlayer.playSong(this, R.raw.calm)
        }

        this.refreshTextures()

    }
    /** funkciu meníme aby sme hráčovi znemožnili používať krok späť*/
    override fun onBackPressed() {
    }
    /** funkciu meníme aby minimalizovaní aplikácie došlo k pozastavení hudby*/
    override fun onPause() {
        MusicPlayer.pauseSong()
        super.onPause()
    }
    /** funkciu meníme po minimalizovaní aplikácie došlo k opätovnému spusteniu hudby*/
    override fun onResume() {
        MusicPlayer.resumeSong()
        super.onResume()
    }

    /** funkcia obsluhy tlačidla pre vypínanie a zapínanie svetla (noci)*/
    fun toggleLight (view: View) {
        this.refreshTextures()
        Game.setNight(!Game.isNight())
        val nightLayer = findViewById<View>(R.id.night_layer)
        val bedroomScreen = findViewById<ImageView>(R.id.bedroom_screen_iv)

        if (Game.isNight()) {
            MusicPlayer.playSong(this, R.raw.calm)
            bedroomScreen.setImageResource(R.drawable.bedroom_night_screen_scaled)
            nightLayer.setBackgroundColor(resources.getColor(R.color.night_layer))
        } else {
            MusicPlayer.playSong(this, R.raw.idle)
            bedroomScreen.setImageResource(R.drawable.bedroom_screen_scaled)
            nightLayer.setBackgroundColor(resources.getColor(R.color.transparent))
        }
    }

    /** funkcia obsluhy tlačidla pre vypínanie a zapínanie hudby */
    fun toggleMusic(view: View) {
        this.music = !this.music
        if (this.music) {
            findViewById<ImageButton>(R.id.soundButton).setImageResource(R.drawable.unmute_scaled)
            MusicPlayer.unmuteSong()
        } else {
            findViewById<ImageButton>(R.id.soundButton).setImageResource(R.drawable.mute_scaled)
            MusicPlayer.muteSong()
        }
    }

    /** funkcia obsluhy tlačidla pre urýchlene starnutie zviarata */
    fun growPet(view : View) {
        if (Game.getAge() < 100) {
            Game.setAge(Game.getAge() + 1)
        } else Toast.makeText(application,Game.getName() + " is fully grown!", Toast.LENGTH_SHORT).show()
        this.refreshTextures()
    }
    /** funkcia obsluhy tlačidla pre nakŕmenie zvieraťa*/
    fun feedPet (view: View) {
        if (Game.getHunger() in 0..95) {
            Game.setHunger(Game.getHunger() + 5)
        } else Toast.makeText(application,Game.getName() + " is not hungry!", Toast.LENGTH_SHORT).show()
        this.refreshTextures()
    }
    /** funkcia obsluhy tlačidla pomocou ktorého prejdeme na ďaľší fragment*/
    fun nextFragment (view: View) {
        this.refreshTextures()
        when (this.currentFrag % 2) {
            0 -> {
                if (Game.isNight()) this.toggleLight(view)
                findNavController(R.id.nav_host_fragment_game_activity).navigate(R.id.action_bedroomFragment_to_kitchenFragment)
            }
            1 -> {
                findNavController(R.id.nav_host_fragment_game_activity).navigate(R.id.action_kitchenFragment_to_bedroomFragment)
            }
        }
        this.currentFrag++
    }
    /** funkcia obsluhy tlačidla pomocou ktorého prejdeme na predchadzajúci fragment*/
    fun prevFragment (view: View) {
        this.refreshTextures()
        when (this.currentFrag % 2) {
            0 -> {
                if (Game.isNight()) this.toggleLight(view)
                findNavController(R.id.nav_host_fragment_game_activity).navigate(R.id.action_bedroomFragment_to_kitchenFragment)
            }
            1 -> {
                findNavController(R.id.nav_host_fragment_game_activity).navigate(R.id.action_kitchenFragment_to_bedroomFragment)
            }
        }
        this.currentFrag++
    }

    /** funkcia, ktorá slúži na obnou textúr a textu v aplikácii*/
    private fun refreshTextures() {
        val energyEditText = findViewById<TextView>(R.id.energyEditText)
        energyEditText.text = Game.getEnergy().toString()
        val hungerEditText = findViewById<TextView>(R.id.hungerEditText)
        hungerEditText.text = Game.getHunger().toString()
        val ageEditText = findViewById<TextView>(R.id.ageEditText)
        ageEditText.text = Game.getAge().toString()


        val petTexture = findViewById<ImageView>(R.id.pet_iv)
        val pillowTexture = findViewById<ImageView>(R.id.pillow_iv)
        if (Game.getAge() in 0..10) {
            pillowTexture.setImageResource(R.drawable.pillow_scaled)
            when (Game.getType()) {
                "Grass" -> petTexture.setImageResource(R.drawable.grass_egg_scaled)
                "Water" -> petTexture.setImageResource(R.drawable.water_egg_scaled)
                "Fire"  -> petTexture.setImageResource(R.drawable.fire_egg_scaled)
            }
        }

        if (Game.getAge() in 10..14) {
            pillowTexture.setImageResource(R.drawable.pillow_scaled)
            when (Game.getType()) {
                "Grass" -> petTexture.setImageResource(R.drawable.grass_egg_cracked_scaled)
                "Water" -> petTexture.setImageResource(R.drawable.water_egg_cracked_scaled)
                "Fire"  -> petTexture.setImageResource(R.drawable.fire_egg_cracked_scaled)
            }
        }

        if (Game.getAge() in 15..49) {
            pillowTexture.setImageResource(R.drawable.blank)
            when (Game.getType()) {
                "Grass" -> petTexture.setImageResource(R.drawable.grass_frog_juvenile_scaled)
                "Water" -> petTexture.setImageResource(R.drawable.water_bird_juvenile_scaled)
                "Fire"  -> petTexture.setImageResource(R.drawable.fire_fox_juvenile_scaled)
            }
        }
    }

    /** funkcia, ktorá vytvára kanál pre notifikácie*/
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("statusChannelID", "statusChannel", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }
}