package com.example.ardrillmodel

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class DrillSelectionActivity : AppCompatActivity() {

    private lateinit var spinnerDrills: Spinner
    private lateinit var btnStartDrill: Button
    private lateinit var imgDrill: ImageView
    private lateinit var tvDrillDesc: TextView

    private val drills = listOf(
        DrillModel("Drill 1", "A cube used for basic placement practice.", R.drawable.drill_cube),
        DrillModel("Drill 2", "A perfect sphere for AR motion interaction.", R.drawable.drill_sphere),
        DrillModel("Drill 3", "A cylinder used to simulate vertical drilling.", R.drawable.drill_cylinder)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drill_selection)

        spinnerDrills = findViewById(R.id.spinner_drills)
        btnStartDrill = findViewById(R.id.btn_start_drill)
        imgDrill = findViewById(R.id.img_drill)
        tvDrillDesc = findViewById(R.id.tv_drill_description)


        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, drills.map { it.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDrills.adapter = adapter

        updateDrillPreview(0)

        spinnerDrills.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long
            ) {
                updateDrillPreview(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnStartDrill.setOnClickListener {
            val selectedIndex = spinnerDrills.selectedItemPosition + 1 // 1-based drill ID
            val intent = Intent(this, ARView::class.java)
            intent.putExtra("SELECTED_DRILL_ID", selectedIndex)
            startActivity(intent)
        }
    }

    private fun updateDrillPreview(index: Int) {
        val drill = drills[index]
        imgDrill.setImageResource(drill.imageRes)
        tvDrillDesc.text = drill.description
    }
}
