package com.bintangtimurlangit.coinsave.models

import androidx.compose.ui.graphics.Color
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

// Define the Category class that extends RealmObject
class Category() : RealmObject {
    // Define primary key
    @PrimaryKey
    var _id: ObjectId = ObjectId.create()

    // Default color value as string
    private var _colorValue: String = "0,0,0"
    var name: String = ""

    // Color property as Compose Color
    val color: Color
        get() {
            // Split color components from color value
            val colorComponents = _colorValue.split(",")
            val (red, green, blue) = colorComponents
            // Create Color object from components
            return Color(red.toFloat(), green.toFloat(), blue.toFloat())
        }

    // Secondary constructor to initialize name and color
    constructor(
        name: String,
        color: Color
    ) : this() {
        this.name = name
        // Store color value as string
        this._colorValue = "${color.red},${color.green},${color.blue}"
    }
}
