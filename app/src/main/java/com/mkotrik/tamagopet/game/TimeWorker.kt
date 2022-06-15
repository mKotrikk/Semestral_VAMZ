package com.mkotrik.tamagopet.game

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mkotrik.tamagopet.R

class TimeWorker(context : Context, params: WorkerParameters) : Worker(context, params) {
    val cntx : Context = context


    /** funkcia popisujúca prácu, ktorá sa vykoná v daných intervaloch na pozadí aplikácie, popr. keď je aplikácia vypnutá*/
    override fun doWork(): Result {
        val sharedPref = cntx.getSharedPreferences("PetData", Context.MODE_PRIVATE)
        Game.setSharedPref(sharedPref)
        this.loadGame(sharedPref)
        if (Game.getAge() < 100)  Game.setAge(Game.getAge() + 1)
        if (Game.getHunger() >= 5) Game.setHunger(Game.getHunger() - 5)

        if ((Game.getEnergy() <= 95) && Game.isNight()) {
            Game.setEnergy(Game.getEnergy() + 5)
        } else if (Game.getEnergy() >= 5 && !Game.isNight()) Game.setEnergy(Game.getEnergy() - 5)


        if (Game.getHunger() < 15) this.hungerNotification()
        if ((Game.getEnergy() == 100) && Game.isNight()) this.energyNotification()
        if (Game.getAge() == 10) this.hatchNotification()
        Log.i("aaa" , "TEST WORK  " + Game.getHunger() + " " + Game.getAge() + " " + Game.getEnergy())

        this.testNotification()
        return Result.success()
    }


    /** funkcia, ktorá pošle notifikáciu o tom, že je zviaratko hladné*/
    private fun hungerNotification() {
        val builder = NotificationCompat.Builder(cntx, "statusChannelID")
            .setContentTitle("Feed me!")
            .setContentText(Game.getName() + " is hungry!")
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)

        val managerCompat = NotificationManagerCompat.from(cntx)
        managerCompat.notify(1, builder.build())
    }

    /** funkcia, ktorá pošle notifikáciu o tom, že je zviaratko vyspaté*/
    private fun energyNotification() {
        val builder = NotificationCompat.Builder(cntx, "statusChannelID")
            .setContentTitle("Wakey, wakey!")
            .setContentText(Game.getName() + " had a nice sleep and wants to play!")
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)

        val managerCompat = NotificationManagerCompat.from(cntx)
        managerCompat.notify(2, builder.build())
    }

    /** funkcia, ktorá pošle notifikáciu o tom, že zviaratko zachvíľu vyrastie*/
    private fun hatchNotification() {
        val builder = NotificationCompat.Builder(cntx, "statusChannelID")
            .setContentTitle("What's that cracking?")
            .setContentText(Game.getName() + " is about to hatch soon!")
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)

        val managerCompat = NotificationManagerCompat.from(cntx)
        managerCompat.notify(2, builder.build())
    }

    /** funkcia, ktorá pošle testovaciu notifikáciu*/
    private fun testNotification() {
        val builder = NotificationCompat.Builder(cntx, "statusChannelID")
            .setContentTitle("TEST")
            .setContentText("Ubehlo 15min")
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)

        val managerCompat = NotificationManagerCompat.from(cntx)
        managerCompat.notify(0, builder.build())
    }

    /** funkcia kde načíta všetky hodnoty zo sharedpref do hry*/
    private fun loadGame(sharedPref : SharedPreferences) {
        Game.setStartUp(sharedPref.getBoolean("StartUp", false))
        Game.setName(sharedPref.getString("Name", "Danny") ?: "Danny")
        Game.setType(sharedPref.getString("Type", "Grass") ?: "Grass")
        Game.setAge(sharedPref.getInt("Age", 0))
        Game.setEnergy(sharedPref.getInt("Energy", 100))
        Game.setHunger(sharedPref.getInt("Hunger", 100))
        Game.setNight(sharedPref.getBoolean("Night", false))
    }

}