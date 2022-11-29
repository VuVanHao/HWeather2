package com.hao.hweather2.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.hao.hweather2.MainActivity
import com.hao.hweather2.R
import com.hao.hweather2.utils.MySharedPreferences
import kotlinx.serialization.descriptors.buildSerialDescriptor
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {

    var unitTemp : TextView ? = null
    var unitLang : TextView ? = null
    var actionBar : TextView ? = null
    var tv : TextView ? = null
    var tvUnit : TextView ? = null
    var tvLang : TextView ? = null
    var tv3 : TextView ? = null
    var tvVersion : TextView ? = null
    var tvPermission : TextView ? = null

    var settingUnit : LinearLayout ? = null
    var settingLang : LinearLayout ?= null
    var custom_view : View? = null
    var custom_view_lang : View ?= null

    var lang = "" //get Language
    var unit = 0 // get Temp Unit



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val context = container!!.context
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        lang = MySharedPreferences.getLanguage(context)
        unit = MySharedPreferences.getTempUnit(context)
        initView(view)
        initData(context)
        initFuncData(view,context)
        return view
    }

    private fun initFuncData(view: View,context: Context) {
        settingUnit = view.findViewById(R.id.settingUnit)
        settingLang = view.findViewById(R.id.settingLang)


        tvPermission!!.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package",context.packageName,null)
            intent.data = uri
            startActivity(intent)
        }

        settingUnit?.setOnClickListener {
            val inflater = activity?.layoutInflater
            custom_view = inflater?.inflate(R.layout.dialog_unit,null)
            val builder : AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setView(custom_view)
            val dialogUnit : TextView = custom_view!!.findViewById(R.id.tvDiaLogUnit)
            var rgDiaLogUnit : RadioGroup = custom_view!!.findViewById(R.id.rgDiaLogUnit)
            val rbDiaLogC : RadioButton = custom_view!!.findViewById(R.id.rbDiaLogC)
            val rbDiaLogF : RadioButton = custom_view!!.findViewById(R.id.rbDiaLogF)
            if(Objects.equals(lang,"vi"))
            {
                dialogUnit.text = "Đơn vị độ"
            }
            else
            {
                dialogUnit.text = "Unit temp"
            }

            if (unit == 0)
            {
                rbDiaLogC.isChecked = true
            }
            else
            {
                rbDiaLogF.isChecked = true
            }

            builder.setPositiveButton("Lưu") { _, _ ->
                if (rbDiaLogC.isChecked)
                {
                    MySharedPreferences.setTempUnit(context,0)
                    unitTemp?.text = "C°"
                    (activity as MainActivity).reloadFragmentSetting()
                }
                else
                {
                    MySharedPreferences.setTempUnit(context,1)
                    unitTemp?.text = "F°"
                    (activity as MainActivity).reloadFragmentSetting()
                }
            }
            builder.setNegativeButton("Hủy") { _, _ -> }

            val alert : AlertDialog = builder.create()
            alert.setCanceledOnTouchOutside(false)
            alert.show()
        }

        settingLang!!.setOnClickListener {
            val inflater = activity?.layoutInflater
            custom_view_lang = inflater?.inflate(R.layout.dialog_lang,null)
            val builder : AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setView(custom_view_lang)
            val dialogLang : TextView = custom_view_lang!!.findViewById(R.id.tvDiaLogLang)
            var rgDiaLogLang : RadioGroup = custom_view_lang!!.findViewById(R.id.rgDiaLogLang)
            val rbDiaLogVi : RadioButton = custom_view_lang!!.findViewById(R.id.rbLangVi)
            val rbDiaLogEn : RadioButton = custom_view_lang!!.findViewById(R.id.rbLangEn)
            if (Objects.equals(lang,"vi"))
            {
                rbDiaLogVi.isChecked = true
                dialogLang.text = "Ngôn ngữ"
            }
            else
            {
                rbDiaLogEn.isChecked = true
                dialogLang.text = "Language"
            }
            builder.setPositiveButton("Lưu") { _, _ ->
                if (rbDiaLogVi.isChecked)
                {
                    MySharedPreferences.setLanguage(context,"vi")
                    unitLang?.text = "Vietnamese"
                    (activity as MainActivity).reloadFragmentSetting()
                }
                else
                {
                    MySharedPreferences.setLanguage(context,"en")
                    unitLang?.text = "English"
                    (activity as MainActivity).reloadFragmentSetting()
                }
            }
            builder.setNegativeButton("Hủy") { _, _ -> }
            val alert : AlertDialog = builder.create()
            alert.setCanceledOnTouchOutside(false)
            alert.show()
        }


    }

    @SuppressLint("SetTextI18n")
    private fun initData(context: Context) {
        if (unit == 0)
        {
            unitTemp?.text = "C°"
        }
        else
        {
            unitTemp?.text = "F°"
        }

        if (Objects.equals(lang,"vi"))
        {
            tvPermission?.text = "Chỉnh sửa quyền";
            unitLang?.text = "Vietnamese";
            tv?.text = "Cài đặt chung";
            tvUnit?.text = "Đơn vị nhiệt độ";
            tvLang?.text = "Ngôn ngữ";
            tv3?.text = "Khác";
            tvVersion?.text = "Phiên bản";
        }
        else
        {
            tvPermission?.text = "Permission setting";
            unitLang?.text = "English";
            tv?.text = "General setting";
            tvUnit?.text = "Unit temp";
            tvLang?.text = "Language";
            tv3?.text = "Other";
            tvVersion?.text = "Version";
        }
    }

    private fun initView(view: View) {
        unitTemp = view.findViewById(R.id.UnitTemp)
        unitLang = view.findViewById(R.id.UnitLang)
        tv = view.findViewById(R.id.tv)
        tvUnit = view.findViewById(R.id.tvUnit)
        tvLang = view.findViewById(R.id.tvLang)
        tv3 = view.findViewById(R.id.tv3)
        tvVersion = view.findViewById(R.id.tvVersion)
        tvPermission = view.findViewById(R.id.tvPermission)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}