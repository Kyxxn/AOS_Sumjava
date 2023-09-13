package com.example.sumjava_project

import android.app.AlertDialog
import android.content.DialogInterface
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.sumjava_project.R

class MainActivity : AppCompatActivity() {
    var flag1 = 1
    var flag2 = 1
    var start_stop = 1
    var sound_index_main = 0
    var sound_on_off_main = true
    lateinit var frag11: Fragment_1_1
    lateinit var frag21: Fragment_2_1
    var mMediaPlayer : MediaPlayer? = null
    var stopflag = false
    var num = 100

//    var vib_check = true
//    var sound_check = true
//    var set_min = 9999 // 디폴트는 '무한대 분'
//    var set_music = 0 // 디폴트는 0번째 요소인 '좋은 날'
//    var text_arr = arrayListOf<Int>(4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragment_open()
    }

    override fun onResume() {
        super.onResume()
        val start = findViewById<ImageView>(R.id.start_img)
        val createright = findViewById<TextView>(R.id.textView_createright)
        createright.setOnClickListener(){
            switchFragment_createrright()
            switchimage(start,createright)
        }
        start.setOnClickListener(){
            when(flag1){
                1 -> {
                    val dataFromFragment1 = Fragment_1_1.instance?.getData1()
                    val dataFromFragment2 = Fragment_2_1.instance2?.getData2()
                    setDataAtFragment_1to2(
                        dataFromFragment1,
                        dataFromFragment2,
                        Fragment_1_2(),
                        Fragment_2_2()
                    )
                }

                2 -> {
                    val dataFromFrgment1 = Fragment_1_2.instance?.getData()
                    setDataAtFragment_2to1(
                        dataFromFrgment1,
                        Fragment_1_1(),
                        Fragment_2_1()
                    )
                }
            }
            switchimage(start,createright)
        }
    }

    fun setDataAtFragment_2to1(data: Data_3?, fragment11: Fragment_1_1, fragment22: Fragment_2_1) {
        val bundle1 = Bundle()
        val bundle2 = Bundle()

        data?.let{
            bundle1.putIntegerArrayList("integerArrayListKey", it.numbers)
            bundle1.putBoolean("vib_boolean_Key", it.vib_onoff)
            bundle1.putBoolean("sound_boolean_Key", it.sound_onoff)
            bundle1.putInt("first_Key", num)

            bundle2.putInt("min_Key", it.min_index)
            bundle2.putInt("sound_Key", it.sound_index)
            bundle2.putInt("first_Key", num)
        }

        fragment11.arguments = bundle1
        fragment22.arguments = bundle2
        switchFragment_11(fragment11)
        switchFragment_22(fragment22)
    }

    fun switchFragment_11(fragment: Fragment_1_1) {
        val transaction = supportFragmentManager.beginTransaction()
        when (flag1) {
            1 -> {
                transaction.replace(R.id.fragmentContainer1, Fragment_1_2())
                flag1 = 2
                bgm_start()
                stopflag = false
            }

            2 -> {
                transaction.replace(R.id.fragmentContainer1, fragment)
                flag1 = 1
                mMediaPlayer?.stop()
                stopflag = true
            }

            3 -> {
                transaction.replace(R.id.fragmentContainer1, Fragment_1_1())
                flag1 = 1
            }
        }
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun switchFragment_22(fragment22: Fragment_2_1) {
        val transaction = supportFragmentManager.beginTransaction()
        when (flag2) {
            1 -> {
                transaction.replace(R.id.fragmentContainer2, Fragment_2_2())
                flag2 = 2
            }

            2 -> {
                transaction.replace(R.id.fragmentContainer2, fragment22)
                flag2 = 1
            }
            3 -> {
                flag2 = 1
                findViewById<View>(R.id.fragmentContainer2).visibility =View.VISIBLE
            }
        }
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun setDataAtFragment_1to2(data_1: Data_1?, data_2: Data_2?, fragment: Fragment_1_2, fragment22: Fragment_2_2) {
        val bundle = Bundle()

        // 1_1 프래그먼트 데이터
        data_1?.let {
            bundle.putIntegerArrayList("integerArrayListKey", it.numbers)
            bundle.putBoolean("vib_boolean_Key", it.vib_onoff)
            bundle.putBoolean("sound_boolean_Key", it.sound_onoff)
            sound_on_off_main= it.sound_onoff
        }

        // 2_1 프래그먼트 데이터
        data_2?.let {
            bundle.putInt("min_Key", it.min_index)
            bundle.putInt("sound_Key", it.sound_index)
            sound_index_main = it.sound_index
        }

        fragment.arguments = bundle
        fragment22.arguments = bundle
        switchFragment(fragment)
        switchFragment2(fragment22)
    }

    fun switchFragment(fragment: Fragment_1_2) {
        val transaction = supportFragmentManager.beginTransaction()
        when (flag1) {
            1 -> {
                transaction.replace(R.id.fragmentContainer1, fragment)
                flag1 = 2
                bgm_start()
                stopflag = false
            }

            2 -> {
                transaction.replace(R.id.fragmentContainer1, Fragment_1_1())
                flag1 = 1
                mMediaPlayer?.stop()
                stopflag = true
            }
            3 -> {
                transaction.replace(R.id.fragmentContainer1, Fragment_1_1())
                flag1 = 1
            }
        }
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun switchFragment2(fragment22: Fragment_2_2) {
        val transaction = supportFragmentManager.beginTransaction()
        when (flag2) {
            1 -> {
                transaction.replace(R.id.fragmentContainer2, fragment22)
                flag2 = 2
            }

            2 -> {
                transaction.replace(R.id.fragmentContainer2, Fragment_2_1())
                flag2 = 1
            }
            3 -> {
                flag2 = 1
                findViewById<View>(R.id.fragmentContainer2).visibility =View.VISIBLE
            }
        }
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onPause() {
        super.onPause()
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
        }
    }

    override fun onBackPressed() {
        // 종료 여부를 묻는 다이얼로그를 띄웁니다.
        showExitConfirmationDialog()
    }

    private fun showExitConfirmationDialog() {
        // 어플리케이션 종료 여부를 물어보는 다이얼로그를 생성합니다.
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("어플리케이션 종료")
        alertDialogBuilder.setMessage("어플리케이션을 종료하시겠습니까?")
        alertDialogBuilder.setPositiveButton("종료") { dialogInterface: DialogInterface, _: Int ->
            // 어플리케이션 종료
            finish()
        }
        alertDialogBuilder.setNegativeButton("취소") { dialogInterface: DialogInterface, _: Int ->
            // 다이얼로그를 닫고 종료하지 않음
            dialogInterface.dismiss()
        }

        // 다이얼로그를 보여줍니다.
        alertDialogBuilder.show()
    }

    fun fragment_open() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer1, Fragment_1_1())
        transaction.replace(R.id.fragmentContainer2, Fragment_2_1())
        transaction.commit()
    }

    fun switchFragment_createrright() {
        val transaction = supportFragmentManager.beginTransaction()
        when (flag1) {
            1 -> {
                transaction.replace(R.id.fragmentContainer1, Fragment_1_3())
                flag1 = 3
                flag2 = 3
                findViewById<View>(R.id.fragmentContainer2).visibility =View.GONE
            }

            3 -> {
                transaction.replace(R.id.fragmentContainer1, Fragment_1_1())
                flag1 = 1
                flag2 = 1
                findViewById<View>(R.id.fragmentContainer2).visibility =View.VISIBLE
            }
        }
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun switchimage(start: ImageView, view1:TextView) {
        when (start_stop) {
            1 -> {
                view1.visibility = View.GONE
                start.setImageResource(R.drawable.stop)
                start_stop = 2

            }

            2 -> {
                view1.visibility = View.VISIBLE
                start.setImageResource(R.drawable.start_btn)
                start_stop = 1
            }
        }
    }
    private fun bgm_start() {
        if (sound_on_off_main) {
            when (sound_index_main) {
                0 -> {
                    mMediaPlayer = MediaPlayer.create(applicationContext, R.raw.water1)
                }

                1 -> {
                    mMediaPlayer = MediaPlayer.create(applicationContext, R.raw.water2)
                }

                2 -> {
                    mMediaPlayer = MediaPlayer.create(applicationContext, R.raw.water3)
                }

                3 -> {
                    mMediaPlayer = MediaPlayer.create(applicationContext, R.raw.rain)
                }

                4 -> {
                    mMediaPlayer = MediaPlayer.create(applicationContext, R.raw.slime)
                }
            }
            mMediaPlayer!!.start()
            mMediaPlayer!!.setOnCompletionListener {
                if (!stopflag) {
                    mMediaPlayer!!.start()
                }
            }
        }
    }
}

