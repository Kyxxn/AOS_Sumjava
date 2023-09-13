package com.example.sumjava_project
data class Data_1(val numbers: ArrayList<Int> = arrayListOf(),
                  val vib_onoff: Boolean = true,
                  val sound_onoff: Boolean = true,
                  val first_init :Int = 0,
                  )

data class Data_2(val min_index : Int = 0,
                  val sound_index : Int = 0)

data class Data_3(val numbers: ArrayList<Int>? = arrayListOf(),
                  val vib_onoff: Boolean = true,
                  val sound_onoff: Boolean = true,
                  val min_index : Int = 0,
                  val sound_index : Int = 0,
                  val first_check : Int =0
                  )