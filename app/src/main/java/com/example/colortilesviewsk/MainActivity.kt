package com.example.colortilesviewsk

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.random.Random


// тип для координат
data class Coord(val x: Int, val y: Int)
class MainActivity : AppCompatActivity() {

    private var FIELD_SIZE: Int = 4
    private var victoryColorTilesCount: Int = FIELD_SIZE*FIELD_SIZE

    private var gameScores: Int = 0

    private lateinit var mainLinearLayout: LinearLayout

    private lateinit var lampOn: Drawable
    private lateinit var lampOff: Drawable

    private lateinit var tiles: ArrayList<ArrayList<View>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLinearLayout = findViewById<LinearLayout>(R.id.mainLinearLayout)

        lampOn = ContextCompat.getDrawable(this, R.drawable.lamp_on)!!
        lampOff = ContextCompat.getDrawable(this, R.drawable.lamp_off)!!

        initField()
        initTilesColor()
    }


    private fun initField(){
        tiles = ArrayList()

        for (i in 0 until FIELD_SIZE) {
            val rowLinearLayout = LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    1f
                )
                orientation = LinearLayout.HORIZONTAL
            }

            val row = ArrayList<View>()
            for (j in 0 until FIELD_SIZE) {
                val view = View(this).apply {
                    id = View.generateViewId()
                    tag = "$i$j"
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1f
                    ).apply {
                        width = 0
                        height = LinearLayout.LayoutParams.MATCH_PARENT
                        weight = 1f
                    }
                    val power = Random.nextInt(0, 2)
                    setBackground(if (power == 0) lampOff else lampOn)
                    setOnClickListener {
                        onClick(it)
                    }
                }
                rowLinearLayout.addView(view)
                row.add(view)
            }
            tiles.add(row)
            mainLinearLayout.addView(rowLinearLayout)
        }
    }

    fun initTilesColor() {
        for (i in 0 until FIELD_SIZE){
            for (j in 0 until FIELD_SIZE){
                if (Random.nextBoolean()){
                    changeColor(tiles[i][j])
                }
            }
        }
    }

    fun getCoordFromString(s: String): Coord {
        val x: Int = s[0] - '0'
        val y: Int = s[1] - '0'
        Log.i("Coords","${x}, ${y}")
        return Coord(x,y) // вернуть полученные координаты
    }
    fun changeColor(view: View) {
        val drawable = view.background as Drawable
        if (drawable == lampOff ) {
            view.setBackground(lampOn)
        } else {
            view.setBackground(lampOff)
        }
    }

    fun onClick(v: View) {
        gameScores -= 1
        val coord = getCoordFromString(v.getTag().toString())
        changeColor(v)

        for (i in 0 until FIELD_SIZE) {
            changeColor(tiles[coord.x][i])
            changeColor(tiles[i][coord.y])
        }
        checkVictory()
    }

    fun checkVictory() {
        var countColorTiles: Int = getColorTiles()
        Log.i("countColorTiles", countColorTiles.toString())
        if (countColorTiles == 0 || countColorTiles == victoryColorTilesCount){
            gameScores += 50 * (FIELD_SIZE/2)
            showVictoryDialog()
        }
    }

    fun getColorTiles(): Int {
        var count: Int = 0
        tiles.forEach { row ->
            row.forEach { tile ->
                val drawable = tile.background as Drawable
                if (drawable == lampOn) {
                    count++
                }
            }
        }
        return count
    }

    private fun nextLevel(){
        FIELD_SIZE +=2
        victoryColorTilesCount = FIELD_SIZE*FIELD_SIZE
        mainLinearLayout.removeAllViews()
        initField()
        initTilesColor()
    }

    private fun showVictoryDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Congratulations!")
        builder.setMessage("You won! Your scores is $gameScores. Do you want to proceed to the next level?")

        builder.setPositiveButton("Next Level") { dialog, which ->
            nextLevel()
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
            finish()
        }

        val dialog = builder.create()
        dialog.show()
    }
}