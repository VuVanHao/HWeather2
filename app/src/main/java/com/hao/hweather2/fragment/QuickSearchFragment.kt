package com.hao.hweather2.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.hao.hweather2.MainActivity
import com.hao.hweather2.R
import com.hao.hweather2.adapter.SearchListCityAdapter
import com.hao.hweather2.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QuickSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class QuickSearchFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()

    var edtSearchName : EditText ? = null
    var btnSearchSpeed : ImageView ? = null
    var gvListCity : GridView ? = null
    var contentSearch : TextView ? = null

    var searchListCityAdapter : SearchListCityAdapter ? = null

    private val textCity : List<String> = listOf("Hà Nội",
        "Bắc Ninh",
        "Vĩnh Phúc",
        "Đà Nẵng",
        "Tuyên Quang",
        "Bắc Giang",
        "Ca Mau",
        "Cần Thơ",
        "Seoul")
    private var mAdapter: ArrayAdapter<*>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = container!!.context
        val view = inflater.inflate(R.layout.fragment_quick_search, container, false)
        initView(view,context)
        initGridView(context)
        goToSearch(context)
        return view
    }

    private fun goToSearch(context: Context) {
        btnSearchSpeed!!.setOnClickListener {
            if (Objects.equals(edtSearchName?.text.toString(),""))
            {
                Toast.makeText(context, "Vui lòng nhập tên thành phố", Toast.LENGTH_SHORT).show()
            }
            else
            {
                if (checkNetwork(context))
                {
                    (activity as MainActivity).goToSeachFragment(edtSearchName!!.text.toString())
                }
                else
                {
                    Toast.makeText(context, "Vui lòng kết nối mạng !!", Toast.LENGTH_SHORT).show()
                }
                
            }
        }
    }

    private fun initGridView(context: Context) {
        searchListCityAdapter = SearchListCityAdapter(context,textCity)
        gvListCity?.adapter = searchListCityAdapter
        gvListCity?.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, i, _ ->
                when (i) {
                    0 -> edtSearchName!!.setText(textCity[0])
                    1 -> edtSearchName!!.setText(textCity[1])
                    2 -> edtSearchName!!.setText(textCity[2])
                    3 -> edtSearchName!!.setText(textCity[3])
                    4 -> edtSearchName!!.setText(textCity[4])
                    5 -> edtSearchName!!.setText(textCity[5])
                    6 -> edtSearchName!!.setText(textCity[6])
                    7 -> edtSearchName!!.setText(textCity[7])
                    8 -> edtSearchName!!.setText(textCity[8])
                }
            }
    }

    private fun initView(view: View, context: Context) {
        edtSearchName = view.findViewById(R.id.edtSearchName)
        btnSearchSpeed = view.findViewById(R.id.btnSearchSpeed)
        gvListCity = view.findViewById(R.id.gvSearchCity)
        contentSearch = view.findViewById(R.id.contentHistory)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            QuickSearchFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun checkNetwork(context: Context) : Boolean
    {
        val connectivityManager = context.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val dataMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        return wifi!!.isConnected || dataMobile!!.isConnected
    }
}