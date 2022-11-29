package com.hao.hweather2.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hao.hweather2.MainActivity
import com.hao.hweather2.R
import com.hao.hweather2.adapter.ManageCityAdapter
import com.hao.hweather2.model.DataWeatherCity
import com.hao.hweather2.utils.IDeleteItemListener
import com.hao.hweather2.utils.MySharedPreferences
import com.hao.hweather2.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CityMangeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CityMangeFragment : Fragment(), IDeleteItemListener {

    @Inject
    lateinit var mainViewModel: MainViewModel

    var lvCityManage: ListView ?= null
    var btnDel: Button ?= null
    var btnAdd: Button ?= null
    var contentSearch: TextView ?= null
    var btnCancelDel : ImageView ? = null

    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private var bottomSheet: View? = null
    private var floatingActionButton: FloatingActionButton? = null
    private var btnCancelSearch: TextView? = null
    private var state = true

    private var mAdapter: ArrayAdapter<*>? = null
    var edtSearchName: EditText? = null

    private var manageCityAdapter : ManageCityAdapter ? = null

    val textCity = arrayOf(
        "Hà Nội",
        "Bắc Ninh",
        "Vĩnh Phúc",
        "Đà Nẵng",
        "Tuyên Quang",
        "Bắc Giang",
        "Ca Mau",
        "Cần Thơ",
        "Seoul"
    )
    var listItemDel: ArrayList<DataWeatherCity>? = null
    var count = 0
    var lang = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = container!!.context
        val view = inflater.inflate(R.layout.fragment_city_mange, container, false)

        mainViewModel = (activity as MainActivity).mainViewModel
        
        listItemDel = ArrayList()
        lang = MySharedPreferences.getLanguage(context)
        lvCityManage = view.findViewById(R.id.LvCityManage)
        btnDel = view.findViewById(R.id.btnDel)
        btnAdd = view.findViewById(R.id.btnSearhAdd)
        contentSearch = view.findViewById(R.id.contentSearch)
        btnCancelDel = view.findViewById(R.id.btnCancelDel)
        initView(view)
        initGridView(view,context)
        initData(mainViewModel,context)
        return view
    }

    private fun initData(mainViewModel: MainViewModel,context: Context) {
        mainViewModel.getAllWeather().observe(viewLifecycleOwner) {
            val listDataWeatherCity: List<DataWeatherCity> = it
            manageCityAdapter = ManageCityAdapter(context, listDataWeatherCity,this)
            lvCityManage!!.adapter = manageCityAdapter
            manageCityAdapter!!.notifyDataSetChanged()
        }
        btnAdd!!.setOnClickListener {
            val nameCityManage = edtSearchName?.text.toString()
            if (Objects.equals(nameCityManage,""))
            {
                Toast.makeText(context, "Vui lòng nhập tên thành phố !!", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val data = mainViewModel.getAlRecord()
                var stateAdd = false;
                for (item in data)
                {
                    if (item.city.name.equals(nameCityManage,ignoreCase = true))
                    {
                        Toast.makeText(context, "Đã tồn tại thành phố trong danh sách", Toast.LENGTH_SHORT).show()
                        stateAdd = true
                        break
                    }
                }
                if (!stateAdd)
                {
                    if (checkNetwork(context))
                    {
                        mainViewModel.insertWeather(nameCityManage,"vi")
                    }
                    else
                    {
                        Toast.makeText(context, "Vui lòng kết nối mạng !!", Toast.LENGTH_SHORT).show()
                    }
                }
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
                state = true
                edtSearchName!!.setText("")
                var imm : InputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(edtSearchName!!.windowToken,0)
            }
            manageCityAdapter!!.notifyDataSetChanged()
        }

        lvCityManage!!.onItemClickListener =
            OnItemClickListener { _, _, i, _ ->
                (activity as MainActivity?)!!.removeCityFragment(i)
            }


        lvCityManage!!.onItemLongClickListener =
            OnItemLongClickListener { _, _, i, l ->
                manageCityAdapter!!.activateButtons(true)
                btnDel!!.visibility = View.VISIBLE
                btnCancelDel!!.visibility = View.VISIBLE
                count = 0
                floatingActionButton!!.visibility = View.INVISIBLE
                btnCancelDel!!.setOnClickListener(View.OnClickListener {
                    manageCityAdapter!!.activateButtons(false)
                    count = 0
                    btnCancelDel!!.visibility = View.GONE
                    btnDel!!.visibility = View.INVISIBLE
                    floatingActionButton!!.visibility = View.VISIBLE
                })
                false
            }

        btnDel!!.setOnClickListener {
            for (item in listItemDel!!)
            {
                mainViewModel.deleteWeather(item)
                manageCityAdapter?.notifyDataSetChanged()
            }
            manageCityAdapter?.activateButtons(false);
            btnDel?.visibility = View.INVISIBLE;
            btnCancelDel?.visibility = View.GONE;
            floatingActionButton?.visibility = View.VISIBLE;
            listItemDel?.clear();
        }
    }


    private fun initView(view: View) {
        edtSearchName = view.findViewById(R.id.edtSearchName)
        btnDel!!.visibility = View.INVISIBLE
        btnCancelDel!!.visibility = View.GONE
        cancelSearch(view)
        initFloatAction(view)
        initLangTemp(view)
    }

    @SuppressLint("SetTextI18n")
    private fun initLangTemp(view: View) {
        if (Objects.equals(lang,"vi"))
        {
            btnDel?.text = "Xóa"
            btnCancelSearch?.text = "Hủy"
            btnAdd?.text = "Thêm"
            edtSearchName?.hint = "Tên thành phố"
            contentSearch?.text = "Thành phố hàng đầu"
        }
        else
        {
            btnDel?.text = "Remove";
            btnCancelSearch?.text = "Cancel";
            btnAdd?.text = "Add";
            edtSearchName?.hint = "City name";
            contentSearch?.text = "List top city";
        }
    }

    private fun initGridView(view: View,context: Context) {
        val gridView : GridView = view.findViewById(R.id.gvListCity)
        mAdapter = ArrayAdapter(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,textCity)
        gridView.adapter = mAdapter
        gridView.onItemClickListener =
            OnItemClickListener { _, view, i, _ ->
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

    private fun initFloatAction(view: View) {
        floatingActionButton = view.findViewById(R.id.flButton)
        bottomSheet = view.findViewById(R.id.design_bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
        floatingActionButton!!.setOnClickListener {
            if (state) {
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
                state = true
            }
        }

    }

    private fun cancelSearch(view: View) {
        btnCancelSearch = view.findViewById(R.id.btnCancelSearch)
        btnCancelSearch!!.setOnClickListener {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
            edtSearchName!!.setText("")
            state = true
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            CityMangeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun iDeteleItem(dataWeatherCity: DataWeatherCity) {
        if (listItemDel?.size == 0)
        {
            listItemDel?.add(dataWeatherCity)
        }
        if (!listItemDel!!.contains(dataWeatherCity))
        {
            listItemDel?.add(dataWeatherCity)
        }
    }

    override fun iReadMoreItem(i: Int) {

    }

    override fun iDelItemName(name: String?) {

    }

    override fun iDelItemUnCheck(dataWeatherCity: DataWeatherCity) {
        listItemDel?.remove(dataWeatherCity)
    }

    private fun checkNetwork(context: Context) : Boolean
    {
        val connectivityManager = context.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val dataMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        return wifi!!.isConnected || dataMobile!!.isConnected
    }
}