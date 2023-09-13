package com.example.sumjava_project
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.sumjava_project.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Fragment_2_1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var set_min = 9999 // 디폴트는 '무한대 분'
    var set_music = 0 // 디폴트는 0번째 요소인 '좋은 날'

    var first_init = 1

    fun getData2(): Data_2{
        return Data_2(set_min, set_music)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        instance2 = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

            set_min = it.getInt("min_Key")
            set_music = it.getInt("sound_Key")
            first_init = it.getInt("first_Key")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        return inflater.inflate(R.layout.fragment_2_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 설정시간 Spinner 조절
        val time_spinner: Spinner = view.findViewById(R.id.time_spinner)
        val time_adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.cycle_array,
            R.layout.timer_spinner
        )
        time_adapter.setDropDownViewResource(R.layout.timer_spinner)
        time_spinner.adapter = time_adapter


        time_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                set_min = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        // 사운드 spinner
        val sound_spinner: Spinner = view.findViewById(R.id.sound_spinner)
        val sound_adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sound_array,
            R.layout.sound_spinner
        )
        sound_adapter.setDropDownViewResource(R.layout.sound_spinner)
        sound_spinner.adapter = sound_adapter

        if(first_init != 1){
            time_spinner.setSelection(set_min)
            sound_spinner.setSelection(set_music)
        }

        sound_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                set_music = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
//        val main_start = view.findViewById<Button>(R.id.start_img)
//        main_start.setOnClickListener {
//            val mActivity = activity as MainActivity
//            mActivity.second_frag_Data(set_min, set_music)
//        }

    override fun onDetach() {
        super.onDetach()
        instance2 = null
    }

    companion object {
        var instance2: Fragment_2_1? = null
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_2_1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
