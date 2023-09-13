package com.example.sumjava_project

import android.animation.*
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Fragment_1_2 : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    var set_min = 9999 // 디폴트는 '무한대 분'
    var text_arr: ArrayList<Int>? = arrayListOf()
    var height: View? = null
    var movingButton: Button? = null
    var flag: Int = 0
    var savedInstanceState12: Bundle? = null

    var min_idx: Int = 0
    var sound_idx: Int = 0
    var number_arr: ArrayList<Int>? = arrayListOf()
    var vib_bool: Boolean = true
    var sound_bool: Boolean = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        instance = this
    }

    // 1_2 프래그먼트가 실행될 때, 메인에 있는 데이터 가져오기
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState12 = savedInstanceState
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

            // 여기서부터 넘겨받은 데이터를 추출합니다.
            text_arr = it.getIntegerArrayList("integerArrayListKey") ?: arrayListOf()
            set_min = it.getInt("min_Key")

            min_idx = it.getInt("min_Key")
            sound_idx = it.getInt("sound_Key")
            number_arr = it.getIntegerArrayList("integerArrayListKey") ?: arrayListOf()
            vib_bool = it.getBoolean("vib_boolean_Key")
            sound_bool = it.getBoolean("sound_boolean_Key")
        }
    }

    fun getData(): Data_3 {
        return Data_3(number_arr, vib_bool, sound_bool, min_idx, sound_idx)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_1_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        height = view
    }

    override fun onResume() {
        movingButton = requireView().findViewById<Button>(R.id.button)
        super.onResume()
        if (null != text_arr?.get(0)!!.toInt() && null != text_arr?.get(2)!!.toInt()) {
            drawPlusLine(text_arr!!.get(0)!!.toInt())
            drawMinusLine(text_arr!!.get(2)!!.toInt())
        }
        Handler().postDelayed({
            movingBar()
        }, 3000L)

    }

    companion object {
        var instance: Fragment_1_2? = null

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_1_2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun movingBar() {
        if (set_min != 0) {
            Handler().postDelayed({
                flag = 1
            }, 1000L * 60 * set_min)//
        }
        val unit: Float = height!!.height.toFloat() / 17f

        if (text_arr!!.size == 0) {
            text_arr =
                savedInstanceState12?.getIntegerArrayList("integerArrayListKey") ?: arrayListOf()
            set_min = savedInstanceState12?.getInt("min_Key") ?: -1
        }
        if (set_min == -1) {
            return
        }
        // 위로 올라가는 애니메이션
        val slideUpAnimator =
            ObjectAnimator.ofFloat(movingButton, "translationY", -unit * text_arr!![0])
        slideUpAnimator.duration = 1000L * text_arr!![0] // 최초 실행시 0에서 위로 움지이는 행위

        //  아래로 내려가는 애니메이션
        val slideDownAnimator =
            ObjectAnimator.ofFloat(movingButton, "translationY", unit * text_arr!![2])
        slideDownAnimator.duration = 1000L * text_arr!![2] // 하강은 일정함

        val slideDownAnimator2 =
            ObjectAnimator.ofFloat(movingButton, "translationY", 0f)
        slideDownAnimator2.duration = 0L// 하강은 일정함

        // AnimatorSet을 사용하여 애니메이션을 정의하고 실행
        val animatorSet1 = AnimatorSet()
        animatorSet1.playSequentially(
            slideUpAnimator,
            pauseAnimator(movingButton!!, text_arr!![1]),
            slideDownAnimator2,
            slideDownAnimator,
            pauseAnimator(movingButton!!, text_arr!![3])
        )

        animatorSet1.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {}

            override fun onAnimationEnd(p0: Animator) {
                //p0.start() // 애니메이션이 끝난 후 다시 시작
                if (flag == 1) {

                } else {
                    p0.start()
                    //startNextAni()
                }
            }

            override fun onAnimationCancel(p0: Animator) {}

            override fun onAnimationRepeat(p0: Animator) {}
        })
        animatorSet1.start()
    }

    private fun pauseAnimator(movingView: View, time: Int): ValueAnimator {
        val animator = ValueAnimator.ofFloat(movingView.translationY, movingView.translationY)
        animator.addUpdateListener { valueAnimator ->
            // 뷰의 translationY 속성을 변경하지 않고 현재 위치를 유지합니다.
        }
        animator.duration = 1000L * time // 2초로 멈추는 애니메이션 실행 시간 설정
        return animator
    }

    private fun drawMinusLine(value: Int) {
        when (value) {
            1 -> {
                requireActivity().findViewById<TextView>(R.id.Line_m1)
                    .setBackgroundResource(R.drawable.bottom_edge_red)
            }

            2 -> {
                requireActivity().findViewById<TextView>(R.id.Line_m2)
                    .setBackgroundResource(R.drawable.bottom_edge_red)
            }

            3 -> {
                requireActivity().findViewById<TextView>(R.id.Line_m3)
                    .setBackgroundResource(R.drawable.bottom_edge_red)
            }

            4 -> {
                requireActivity().findViewById<TextView>(R.id.Line_m4)
                    .setBackgroundResource(R.drawable.bottom_edge_red)
            }

            5 -> {
                requireActivity().findViewById<TextView>(R.id.Line_m5)
                    .setBackgroundResource(R.drawable.bottom_edge_red)
            }

            6 -> {
                requireActivity().findViewById<TextView>(R.id.Line_m6)
                    .setBackgroundResource(R.drawable.bottom_edge_red)
            }

            7 -> {
                requireActivity().findViewById<TextView>(R.id.Line_m7)
                    .setBackgroundResource(R.drawable.bottom_edge_red)
            }

            8 -> {
                requireActivity().findViewById<TextView>(R.id.Line_m8)
                    .setBackgroundResource(R.drawable.bottom_edge_red)
            }
        }
    }

    private fun drawPlusLine(value: Int) {
        when (value) {
            1 -> {
                requireActivity().findViewById<TextView>(R.id.Line_p1)
                    .setBackgroundResource(R.drawable.bottom_edge_red)
            }

            2 -> {
                requireActivity().findViewById<TextView>(R.id.Line_p2)
                    .setBackgroundResource(R.drawable.bottom_edge_red)
            }

            3 -> {
                requireActivity().findViewById<TextView>(R.id.Line_p3)
                    .setBackgroundResource(R.drawable.bottom_edge_red)
            }

            4 -> {
                requireActivity().findViewById<TextView>(R.id.Line_p4)
                    .setBackgroundResource(R.drawable.bottom_edge_red)
            }

            5 -> {
                requireActivity().findViewById<TextView>(R.id.Line_p5)
                    .setBackgroundResource(R.drawable.bottom_edge_red)
            }

            6 -> {
                requireActivity().findViewById<TextView>(R.id.Line_p6)
                    .setBackgroundResource(R.drawable.bottom_edge_red)
            }

            7 -> {
                requireActivity().findViewById<TextView>(R.id.Line_p7)
                    .setBackgroundResource(R.drawable.bottom_edge_red)
            }

            8 -> {
                requireActivity().findViewById<TextView>(R.id.Line_p8)
                    .setBackgroundResource(R.drawable.bottom_edge_red)
            }

        }
    }

    override fun onDetach() {
        super.onDetach()
        instance = null
    }
}

/*
    private fun startNextAni()
    {
        val unit:Float = height!!.height.toFloat()/17f
        val slideUpAnimator2 =
            ObjectAnimator.ofFloat(movingButton, "translationY", unit*abs(text_arr!!.get(0)-text_arr!!.get(2))/2)
        slideUpAnimator2.duration = 500L * text_arr!!.get(0) // 최초 실행후 아래에서 0으로 움직이는 행위 단 시간은 절반

        // 바로 아래로 내려가는 애니메이션
        val slideDownAnimator =
            ObjectAnimator.ofFloat(movingButton, "translationY", unit * text_arr!!.get(2))
        slideDownAnimator.duration = 1000L * text_arr!!.get(2) // 하강은 일정함

        val slideUpAnimator3 =
            ObjectAnimator.ofFloat(movingButton, "translationY", -unit* text_arr!!.get(0))
        slideUpAnimator3.duration = 1000L * text_arr!!.get(0) // 2번째 실행후 0에서 위로 움직이는 행위 단 시간은 절반

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(
            slideUpAnimator3,
            pauseAnimator(movingButton!!, text_arr!!.get(1)),
            slideDownAnimator,
            pauseAnimator(movingButton!!, text_arr!!.get(3)),
            slideUpAnimator2)
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {}

            override fun onAnimationEnd(p0: Animator) {
                if (flag == 1) {

                } else {
                    p0.start() // 애니메이션이 끝난 후 다시 시작
                }
            }

            override fun onAnimationCancel(p0: Animator) {}

            override fun onAnimationRepeat(p0: Animator) {}
        })
        animatorSet.start()
    }*/




