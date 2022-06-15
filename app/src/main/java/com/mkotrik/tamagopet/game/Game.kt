package com.mkotrik.tamagopet.game

import android.content.SharedPreferences

/** Objekt, ktorý uchováva všetky podstatné informácie o hre*/
object Game {
    private var name: String = "Danny" //Default meno
    private var type: String = "Grass" //Default typ
    private var age: Int = 0    //vek vždy začne od 0
    private var energy: Int = 100 //energia vždy začína ako hodnota 100 (%)
    private var hunger: Int = 100 //hlad vždy začína ako hodnota 100(%) v našom prípade sa jedna o to ako moc je zviera nasýtené

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var night: Boolean = false

    private var startUp: Boolean = false //ak je atribút true tak hra bola spustená viac ako jedenkrát

    fun setSharedPref(sharedPreferences: SharedPreferences) {
        this.sharedPreferences = sharedPreferences
        this.editor = sharedPreferences.edit()
    }
    fun getSharedPref() : SharedPreferences {
        return this.sharedPreferences
    }

    fun setName(name : String) {
        this.name = name
        editor.putString("Name", name)
        editor.apply()
    }
    fun getName() : String {
        return this.name
    }

    fun setType(type: String) {
        this.type = type
        editor.putString("Type", type)
        editor.apply()
    }
    fun getType() : String {
        return this.type
    }

    fun setAge(age: Int) {
        this.age = age
        editor.putInt("Age", age)
        editor.apply()
    }
    fun getAge() : Int {
        return this.age
    }

    fun setEnergy(energy: Int) {
        this.energy = energy
        editor.putInt("Energy", energy)
        editor.apply()
    }
    fun getEnergy() : Int {
        return this.energy
    }

    fun setHunger(hunger: Int) {
        this.hunger = hunger
        editor.putInt("Hunger", hunger)
        editor.apply()
    }
    fun getHunger() : Int {
        return this.hunger
    }

    fun isNight() : Boolean {
        return this.night
    }
    fun setNight(night : Boolean) {
        this.night = night
        editor.putBoolean("Night", night)
        editor.apply()
    }

    fun setStartUp(startUp : Boolean) {
        this.startUp = startUp
        editor.putBoolean("StartUp", startUp)
        editor.apply()
    }
    fun getStartUp() : Boolean {
        return this.startUp
    }

}