package com.developer.valyutaapp.ui.widget

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import com.developer.valyutaapp.domain.entities.Valute
import com.developer.valyutaapp.ui.adapter.DialogAdapter
import com.developer.valyutaapp.R
import android.os.Bundle
import android.util.Log
import com.developer.valyutaapp.utils.ImageResource
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import by.kirich1409.viewbindingdelegate.viewBinding
import com.developer.valyutaapp.databinding.ActivityWidgetBinding
import com.developer.valyutaapp.utils.Utils
import io.paperdb.Paper
import java.text.DecimalFormat
import java.util.ArrayList

class WidgetActivity : AppCompatActivity(R.layout.activity_widget), WidgetViewInterface {

    private val viewBinding by viewBinding(ActivityWidgetBinding::bind, R.id.container)

    var alertdialog: AlertDialog.Builder? = null
    var dialog: AlertDialog? = null
    var valutes: ArrayList<Valute>? = null
    private var valuteId = 840
    var adapter: DialogAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        valutes = ArrayList()
        setupMVP()
        valuteById
        valuteList
        viewBinding.saveWidget.setOnClickListener {
            val val_dec: String
            val decimalFormat = DecimalFormat("#.####")
            val decimal = decimalFormat.format(viewBinding.tvValue.text.toString().toDouble())
            val_dec = if (viewBinding.tvNominal.text.toString().length < 3) {
                decimalFormat.format(viewBinding.tvNominal.text.toString().toDouble())
            } else {
                viewBinding.tvNominal.text.toString()
            }
            Paper.init(this@WidgetActivity)
            Paper.book().write("charcode", viewBinding.name1.text)
            Log.d("widget", " = " + viewBinding.name1.text)
            Paper.book().write("charcode2", viewBinding.name2.text)
            Log.d("widget", " = " + viewBinding.name2.text)
            Paper.book().write("nominal", val_dec)
            Log.d("widget", " = " + viewBinding.tvNominal.text)
            Paper.book().write("value", decimal.toString())
            Log.d("widget", " = $decimal")
            Paper.book().write("dat", Utils.getDate())
            Toast.makeText(
                this@WidgetActivity,
                "?????????? ???????????? ???? ?????????? ????????????????????",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupMVP() {
        //widgetPresenter = WidgetPresenter(this.applicationContext, this)
    }

    private val valuteById: Unit
        get() {
            //widgetPresenter!!.getValuteById(valuteId)
        }
    private val valuteList: Unit
        get() {
           // widgetPresenter!!.valutes()
        }

    override fun showToast(s: String) {
        Toast.makeText(this@WidgetActivity, s, Toast.LENGTH_LONG).show()
    }

    override fun showProgressBar() {
        viewBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        viewBinding.progressBar.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    override fun displayValuteWithId(valute: Valute) {
        val bt = ImageResource.getImageRes(this, valute.charCode)
        viewBinding.iconValute1.setImageBitmap(bt)
        viewBinding.tvNominal.text = java.lang.String.valueOf(valute.nominal)
        viewBinding.tvValue.text = valute.value
        viewBinding.name1.text = valute.charCode
        viewBinding.name2.text = "TJS"
        viewBinding.iconValute2.setImageResource(R.drawable.tajikistan)
    }

    override fun displayValutes(valute: List<Valute>) {
        valutes!!.addAll(valute)
    }

    override fun displayError(s: String) {
        showToast(s)
    }

    fun dialogValutes() {
        alertdialog = AlertDialog.Builder(this)
        alertdialog!!.setTitle("?????? ????????????")
        val inflater = this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.row_item, null)
        val listView = row.findViewById<View>(R.id.list_dialog) as ListView
        //adapter = DialogAdapter(this, valutes!!)
        listView.adapter = adapter
        alertdialog!!.setView(row)
        dialog = alertdialog!!.create()
        dialog!!.show()
    }


}