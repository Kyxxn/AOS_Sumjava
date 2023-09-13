package com.example.sumjava_project

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import com.example.sumjava_project.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Fragment_1_1 : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    val imageViews = arrayOfNulls<ImageView>(8)
    val textViews = arrayOfNulls<TextView>(4)

    var number_arr: ArrayList<Int>? = arrayListOf()
    var set_vib = true
    var set_sound = true

    var first_init = 1

    fun getData1(): Data_1 {
        val numbers = ArrayList<Int>(4)
        for(textView in textViews){
            val number = textView?.text.toString().toIntOrNull()
            if(number != null){
                numbers.add(number)
            }
        }
        return Data_1(numbers, set_vib, set_sound, first_init)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        instance = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

            set_vib = it.getBoolean("vib_boolean_Key")
            set_sound = it.getBoolean("sound_boolean_Key")
            number_arr = it.getIntegerArrayList("integerArrayListKey") ?: arrayListOf()
            first_init = it.getInt("first_Key")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_1_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 토글버튼 처리
        val vib_toggleButton = view.findViewById<ToggleButton>(R.id.toggleButton1)
        val sound_toggleButton = view.findViewById<ToggleButton>(R.id.toggleButton2)

        // 처음 앱을 실행했을 때, 디폴트로 true
        if(first_init == 1){
            vib_toggleButton.isChecked = true
            sound_toggleButton.isChecked = true
        }
        else{
            vib_toggleButton.isChecked = set_vib
            sound_toggleButton.isChecked = set_sound
        }
        vib_toggleButton.setOnCheckedChangeListener { compoundButton1, onoff_check ->
            set_vib = onoff_check
        }
        sound_toggleButton.setOnCheckedChangeListener { compoundButton2, onoff_check ->
            set_sound = onoff_check
        }

        // 플러스, 마이너스 이미지 배열 초기화
        for (i in imageViews.indices) {
            val id = resources.getIdentifier("image$i", "id", context?.packageName)
            imageViews[i] = view.findViewById(id)
        }

        // 텍스트뷰 4개 배열에 초기화
        for (i in textViews.indices) {
            val id = resources.getIdentifier("number$i", "id", context?.packageName)
            textViews[i] = view.findViewById(id)
        }

        // first_init 값이 1이 아니면 number_arr로 초기화
        if (first_init != 1) {
            number_arr?.let { numbers ->
                for (i in numbers.indices) {
                    if (i < textViews.size) {
                        textViews[i]?.text = numbers[i].toString()
                    }
                }
            }
        }

        // 플마 이미지에 대한 텍스트뷰 증감 처리
        for (i in imageViews.indices) {
            imageViews[i]?.setOnClickListener {
                val index = i.div(2)
                if (index in textViews.indices) {
                    textViews[index]?.let {
                        var currentValue = it.text.toString().toInt()
                        if (i % 2 == 0 && currentValue > 0 && currentValue < 10) {
                            currentValue--
                        } else if (i % 2 == 1 && currentValue >= 0 && currentValue < 10) {
                            if(currentValue == 8){
                                currentValue = 0
                            }else{
                                currentValue++
                            }
                        }
                        it.text = currentValue.toString()
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        set_vib = view?.findViewById<ToggleButton>(R.id.toggleButton1)?.isChecked ?: false
        set_sound = view?.findViewById<ToggleButton>(R.id.toggleButton2)?.isChecked ?: false
        first_init++
    }


    override fun onDetach() {
        super.onDetach()
        instance = null
    }

    companion object {
        var instance: Fragment_1_1? = null
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_1_1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

//val mActivity = activity as MainActivity
//val mainstart = mActivity.findViewById<ImageView>(R.id.start_img)
//        mainstart!!.setOnClickListener {
//            val numbers = ArrayList<Int>(4)
//            for(textView in textViews){
//                val number = textView?.text.toString().toIntOrNull()
//                if(number != null){
//                    numbers.add(number)
//                }
//            }
//            mActivity.first_frag_Data(numbers, vib_onoff, sound_onoff)
//        }


