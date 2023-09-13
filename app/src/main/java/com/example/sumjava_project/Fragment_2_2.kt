package com.example.sumjava_project
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sumjava_project.R


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Fragment_2_2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var stop_flag = false
    var vib_check = true
    var sound_check = true
    var set_min = 9999 // 디폴트는 '무한대 분'
    var set_music = 0 // 디폴트는 0번째 요소인 '좋은 날'
    var text_arr: ArrayList<Int>? = arrayListOf()
    var savedInstanceState22: Bundle? = null
    // 1_2 프래그먼트가 실행될 때, 메인에 있는 데이터 가져오기
    override fun onCreate(savedInstanceState: Bundle?) {
        savedInstanceState22 = savedInstanceState
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

            // 여기서부터 넘겨받은 데이터를 추출합니다.
            text_arr = it.getIntegerArrayList("integerArrayListKey") ?: arrayListOf()
            vib_check = it.getBoolean("vib_boolean_Key")
            sound_check = it.getBoolean("sound_boolean_Key")
            set_min = it.getInt("min_Key")
            set_music = it.getInt("sound_Key")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return  inflater.inflate(R.layout.fragment_2_2, container, false)
    }


    override fun onResume() {
        super.onResume()
        timer()
    }

    override fun onStop() {
        super.onStop()
        stop_flag = true
    }
    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_2_2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun timer() {
        val textTarget = requireView().findViewById<TextView>(R.id.textView_count)
        val numTarget = requireView().findViewById<TextView>(R.id.textView_count2)
        val minuteTarget = requireView().findViewById<TextView>(R.id.textView_left_session)
        val minuteTextTarget = requireView().findViewById<TextView>(R.id.textView31)

        if(text_arr!!.size == 0)
        {
            text_arr = savedInstanceState22?.getIntegerArrayList("integerArrayListKey") ?: arrayListOf()
            vib_check = savedInstanceState22?.getBoolean("vib_boolean_Key") ?: false
            sound_check = savedInstanceState22?.getBoolean("sound_boolean_Key") ?: false
            set_min = savedInstanceState22?.getInt("min_Key")?: -1
            set_music = savedInstanceState22?.getInt("sound_Key")?: -1
        }
        if(set_min == -1) {
            return
        }
        val breathe_sequence = arrayOf("들숨","숨참기","날숨","숨참기")
        var stopThreads = false
        var timerThread = Thread {
            for (i in 1..3) {
                if(stop_flag)
                {
                    break
                }
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                var num = numTarget.text.toString().toInt()
                numTarget.post {
                    numTarget.text = (--num).toString()
                }
            }
            if(!stop_flag) { alarm()
            }
            var index = 0
            while (!stopThreads) {
                if(stop_flag)
                {
                    break
                }
                // 스레드 종료 플래그를 확인하여 반복문을 종료
                // stopThreads 값이 true가 될 때까지 실행됨
                textTarget.post {
                    textTarget.text = breathe_sequence[index]
                    numTarget.text = text_arr?.get(index).toString()
                }
                for (i in 1..text_arr?.get(index).toString().toInt()) {
                    if(stop_flag || stopThreads)
                    {
                        break
                    }
                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                        return@Thread
                    }
                    var num = numTarget.text.toString().toInt()
                    numTarget.post {
                        numTarget.text = (--num).toString()
                    }
                    if(i == text_arr?.get(index).toString().toInt() && !stop_flag)
                    {
                        alarm()
                    }
                }
                index = (index + 1) % breathe_sequence.size
            }
        }

        timerThread?.start()
        if(set_min == 0)
        {
            minuteTarget.text = "무한"
            minuteTextTarget.text = "반복"
        }
        if(set_min != 0) {// 여기에 설정하고자 하는 분을 넣으세요.
            minuteTarget.text = set_min.toString()
            Handler(Looper.getMainLooper()).postDelayed({
                stopThreads = true
            }, 1000 * 60 * set_min.toLong())
            val minute_timer = Thread {
                Thread.sleep(3000)
                for (i in 2..minuteTarget.text.toString().toInt()) {
                    if(stop_flag)
                    {
                        break
                    }
                    try {
                        Thread.sleep(60000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    var minute = minuteTarget.text.toString().toInt()
                    minuteTarget.post {
                        minuteTarget.text = (--minute).toString()
                    }
                }
                minuteTarget.text = "60"
                minuteTextTarget.text = "초"
                for (i in 1..60) {
                    if(stop_flag)
                    {
                        break
                    }
                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    var minute = minuteTarget.text.toString().toInt()
                    minuteTarget.post {
                        minuteTarget.text = (--minute).toString()
                    }
                }
            }.start()
        }
    }
    fun alarm()
    {
        if(vib_check == true)
        {
            val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            // 2. 진동 구현: 1000ms 동안 100의 강도로 울린다.
            vibrator.vibrate(VibrationEffect.createOneShot(1000, 100));
        }
/*        val soundthread = Thread{if(sound_check == true)
        {
            var mMediaPlayer : MediaPlayer? = null
            when(set_music)
            {
                0 -> { mMediaPlayer = MediaPlayer.create(context, R.raw.nice_day)}
                1 -> { mMediaPlayer = MediaPlayer.create(context, R.raw.donut)}
                2 -> { mMediaPlayer = MediaPlayer.create(context, R.raw.summer_vacation)}
            }
            mMediaPlayer!!.start()
            Thread.sleep(1000)
            mMediaPlayer!!.stop()
            mMediaPlayer.release()
            mMediaPlayer=null
        }}.start()*/
    }
}
