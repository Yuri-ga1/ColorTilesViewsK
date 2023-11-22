package com.example.colortilesviewsk

import android.R.attr.x
import android.R.attr.y
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.Console
import kotlin.random.Random


// тип для координат
data class Coord(val x: Int, val y: Int)
class MainActivity : AppCompatActivity() {

    lateinit var tiles: Array<Array<View>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //tiles = Array(4) { Array(4){View(this)} }
        tiles = arrayOf(
            arrayOf(findViewById(R.id.t00), findViewById(R.id.t01), findViewById(R.id.t02), findViewById(R.id.t03)),
            arrayOf(findViewById(R.id.t10), findViewById(R.id.t11), findViewById(R.id.t12), findViewById(R.id.t13)),
            arrayOf(findViewById(R.id.t20), findViewById(R.id.t21), findViewById(R.id.t22), findViewById(R.id.t23)),
            arrayOf(findViewById(R.id.t30), findViewById(R.id.t31), findViewById(R.id.t32), findViewById(R.id.t33)),
        )
        initField()
        // еще 15 строк тут, id у плиток нужно по образцу внести в разметку
    }

    fun getCoordFromString(s: String): Coord {
        val x: Int = s[0] - '0'
        val y: Int = s[1] - '0'
        Log.i("Coords","${x}, ${y}")
        return Coord(x,y) // вернуть полученные координаты
    }
    fun changeColor(view: View) {
        val brightColor = resources.getColor(R.color.bright)
        val darkColor = resources.getColor(R.color.dark)
        val drawable = view.background as ColorDrawable
        if (drawable.color == brightColor ) {
            view.setBackgroundColor(darkColor)
        } else {
            view.setBackgroundColor(brightColor)
        }
    }

    fun onClick(v: View) {
        val coord = getCoordFromString(v.getTag().toString())
        changeColor(v)

        for (i in 0..3) {
            changeColor(tiles[coord.x][i])
            changeColor(tiles[i][coord.y])
        }
        checkVictory()
    }

    fun checkVictory() {
        var countColorTiles: Int = getColorTiles()
        if (countColorTiles == 0 || countColorTiles == 16){
            Toast.makeText(this, "You won!", Toast.LENGTH_SHORT).show()
        }
        // TODO: проверка победы
    }

    fun getColorTiles(): Int {
        val brightColor = resources.getColor(R.color.bright)
        var count: Int = 0
        for (i in 0..3){
            for (j in 0..3){
                val drawable = tiles[i][j].background as ColorDrawable
                if (drawable.color == brightColor){
                    count++
                }
            }
        }
        return count
    }

    fun initField() {
        for (i in 0..3){
            for (j in 0..3){
                if (Random.nextBoolean()){
                    changeColor(tiles[i][j])
                }
            }
        }
    }


}