package com.mkotrik.tamagopet.selection

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mkotrik.tamagopet.R
import com.mkotrik.tamagopet.game.Game
import com.mkotrik.tamagopet.game.GameActivity
import com.mkotrik.tamagopet.game.MusicPlayer
import com.mkotrik.tamagopet.game.TimeWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPref : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

        override fun onCreate(savedInstanceState: Bundle?) {

            this.sharedPref = getSharedPreferences("PetData", Context.MODE_PRIVATE)
            this.editor = sharedPref.edit()
            Game.setSharedPref(this.sharedPref)

            this.loadGame()

            if (Game.getStartUp()) this.next()
            else {
                val workRequest = PeriodicWorkRequestBuilder<TimeWorker>(15, TimeUnit.MINUTES).build()
                WorkManager.getInstance(this).enqueue(workRequest)
            }

            MusicPlayer.playSong(this ,R.raw.calm)

            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
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

    /** funkcia obsluhy tlačidla pomocou ktorého zvolíme typ zvieraťa*/
    fun selectGrass (view: View) {
        Game.setType("Grass")
        this.nextFrag()
    }
    /** funkcia obsluhy tlačidla pomocou ktorého zvolíme typ zvieraťa*/
    fun selectWater (view: View) {
        Game.setType("Water")
        this.nextFrag()
    }
    /** funkcia obsluhy tlačidla pomocou ktorého zvolíme typ zvieraťa*/
    fun selectFire (view: View) {
        Game.setType("Fire")
        this.nextFrag()
    }
    /** funkcia obsluhy tlačidla pomocou ktorého potvrdíme meno, ktoré hráč zadal*/
    fun submitName (view: View) {
        val nameEditText = findViewById<EditText>(R.id.name_editText)
        val name = nameEditText.text.toString()
        if (name.length in 2..10) {
            Game.setName(name)

            this.next()
        } else {
            Toast.makeText(application,"Name must be 2 to 10 characters long",Toast.LENGTH_LONG).show()
        }
    }

    /** funkcia prechodu na ďaľší fragment*/
    private fun nextFrag() {
        findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.action_typeSelectionFragment_to_nameSelectionFragment)
    }

    /** funkcia prechodu na ďaľšiu aktivitu*/
    private fun next() {
        Game.setStartUp(true)

        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    /** funkcia kde načíta všetky hodnoty z sharedpref do hry*/
    private fun loadGame() {
        Game.setStartUp(sharedPref.getBoolean("StartUp", false))
        Game.setName(sharedPref.getString("Name", "Danny") ?: "Danny")
        Game.setType(sharedPref.getString("Type", "Grass") ?: "Grass")
        Game.setAge(sharedPref.getInt("Age", 0))
        Game.setEnergy(sharedPref.getInt("Energy", 100))
        Game.setHunger(sharedPref.getInt("Hunger", 100))
        Game.setNight(sharedPref.getBoolean("Night", false))
    }
}