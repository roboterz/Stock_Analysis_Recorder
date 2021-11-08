package com.example.option.data.entities

import androidx.room.*

@Entity(indices = [Index(value = ["id"], unique = true)])

data class Stock(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    var code: String,
    var sell: Double,
    var buy_bottom: Double,
    var buy_top: Double,
    var breakthrough: Double,
    var stress: Double,
    var direction: Boolean,
    var star: Boolean
){
    constructor() : this(
        0,
        "",
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        false,
        false
    )
}
