package com.example.ardrillmodel

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class ARView : AppCompatActivity() {

    private lateinit var arFragment: ArFragment
    private var modelRenderable: ModelRenderable? = null
    private var selectedDrillId: Int = 1 // default drill

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedDrillId = intent.getIntExtra("SELECTED_DRILL_ID", 1)

        setContentView(R.layout.activity_ar_view)

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment

        loadSelectedDrillRenderable(selectedDrillId)

        arFragment.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane, _ ->
            if (plane.type != Plane.Type.HORIZONTAL_UPWARD_FACING) {
                Toast.makeText(this, "Tap on a horizontal surface please", Toast.LENGTH_SHORT).show()
                return@setOnTapArPlaneListener
            }

            val renderable = modelRenderable
            if (renderable == null) {
                Toast.makeText(this, "Model not loaded yet", Toast.LENGTH_SHORT).show()
                return@setOnTapArPlaneListener
            }

            placeObject(hitResult, renderable)
        }
    }

    private fun loadSelectedDrillRenderable(drillId: Int) {
        val color = when (drillId) {
            1 -> Color.BLUE
            2 -> Color.GREEN
            3 -> Color.RED
            else -> Color.BLUE
        }

        val shapeLoader = when (drillId) {
            1 -> { // Cube
                { material: com.google.ar.sceneform.rendering.Material ->
                    ShapeFactory.makeCube(Vector3(0.2f, 0.2f, 0.2f), Vector3.zero(), material)
                }
            }
            2 -> { // Sphere
                { material: com.google.ar.sceneform.rendering.Material ->
                    ShapeFactory.makeSphere(0.15f, Vector3.zero(), material)
                }
            }
            3 -> { // Cylinder
                { material: com.google.ar.sceneform.rendering.Material ->
                    ShapeFactory.makeCylinder(0.05f, 0.3f, Vector3.zero(), material)
                }
            }
            else -> {
                { material: com.google.ar.sceneform.rendering.Material ->
                    ShapeFactory.makeCube(Vector3(0.1f, 0.3f, 0.1f), Vector3.zero(), material)
                }
            }
        }

        MaterialFactory.makeOpaqueWithColor(this, com.google.ar.sceneform.rendering.Color(color))
            .thenAccept { material ->
                modelRenderable = shapeLoader(material)
            }
    }

    private fun placeObject(hitResult: HitResult, renderable: ModelRenderable) {
        val anchor = hitResult.createAnchor()
        val anchorNode = AnchorNode(anchor).apply {
            parent = arFragment.arSceneView.scene
        }

        val node = TransformableNode(arFragment.transformationSystem).apply {
            this.renderable = renderable
            parent = anchorNode
            select()
        }
    }
}
