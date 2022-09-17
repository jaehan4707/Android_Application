package com.example.calculator

import android.content.Intent
import android.net.IpSecManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.room.Room
import com.example.calculator.model.History
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ArithmeticException
import java.lang.NumberFormatException
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.function.BinaryOperator

class MainActivity : AppCompatActivity() {

    private var isOperator = false // 연산자를 작성하고 있는지 아닌지
    private var hasOperator = false //계산식에 연산자가 있는지 없는지를 뜻함.
    private val txtInput by lazy<TextView> {findViewById(R.id.input_text)}
    private val txtResult by lazy<TextView> { findViewById(R.id.result_text)}
    private val historyLayout : View by lazy{
        findViewById<View>(R.id.historyLayout)
    }
    private val historyLinearLayout: LinearLayout by lazy{
        findViewById<LinearLayout>(R.id.historyLinearLayout)
    }
   lateinit var db:AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main)
        db= Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "historyDB"
        ).build()
   }
    @RequiresApi(Build.VERSION_CODES.M)
    fun buttonClicked(v: View) {
        when (v.id) {
            R.id.num_0_button -> numberButtonClicked("0")
            R.id.num_1_button -> numberButtonClicked("1")
            R.id.num_2_button -> numberButtonClicked("2")
            R.id.num_3_button -> numberButtonClicked("3")
            R.id.num_4_button -> numberButtonClicked("4")
            R.id.num_5_button -> numberButtonClicked("5")
            R.id.num_6_button -> numberButtonClicked("6")
            R.id.num_7_button -> numberButtonClicked("7")
            R.id.num_8_button -> numberButtonClicked("8")
            R.id.num_9_button -> numberButtonClicked("9")

            R.id.plus_button -> operatorButtonClicked("+")
            R.id.minus_button -> operatorButtonClicked("-")
            R.id.times_button -> operatorButtonClicked("X")
            R.id.divide_button -> operatorButtonClicked("/")
            R.id.mod_button -> operatorButtonClicked("%")
        }
    }
    private fun numberButtonClicked(number : String) {
        if (isOperator) { //
            txtInput.append(" ")
        }
        isOperator = false
        val itext = txtInput.text.split(" ")
        if (itext.isNotEmpty() && itext.last().length >= 15) {
            Toast.makeText(this, "15자리까지만 사용할수 있습니다", Toast.LENGTH_SHORT).show()
            return
        } else if (itext.last().isEmpty() && number == "0") {
            Toast.makeText(this, "0은 제일 앞에 올수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        txtInput.append(number)
        txtResult.text = calculateExpression()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun operatorButtonClicked(operator: String) {
        if (txtInput.text.isEmpty()) { //연산자를 눌럿는데 숫자가 없다면 바로 return
            return
        }
        when {
            isOperator -> {
                val text = txtInput.text.toString()
                txtInput.text = text.dropLast(1) + operator
            }
            hasOperator -> {
                Toast.makeText(this, "연산자는 한번만 사용가능합니다", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                txtInput.append(" $operator")
            }
        }

        val ssb= SpannableStringBuilder(txtInput.text)
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.green)),
            txtInput.text.length-1, txtInput.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        txtInput.text=ssb
        isOperator = true
        hasOperator = true
    }
    fun resultButtonClicked(v:View) {
        val itext = txtInput.text.split(" ")
        if (txtInput.text.isEmpty() || itext.size == 1) {
            return
        }
        if (itext.size != 3 && hasOperator) {
            Toast.makeText(this, "수식을 완성해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        if (itext[0].isNumber().not() || itext[2].isNumber().not()) {
            Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        val input_text = txtInput.text.toString()
        val result_text = calculateExpression()

        Thread(Runnable {
            db.historyDao().insertHistory(History(null,input_text,result_text))
        }).start()
        txtResult.text = " "
        txtInput.text = result_text

        isOperator = false
        hasOperator = false
    }
    private fun calculateExpression() : String {
        val input_text = txtInput.text.split(" ")
        if (hasOperator.not() || input_text.size != 3) {
            return ""
        } else if (input_text[0].isNumber().not() || input_text[2].isNumber().not()) {
            return ""
        }
        //  A + B
        val n1 = input_text[0].toBigInteger() //A를 의미
        val n2 = input_text[2].toBigInteger()  //B를 의미
        val op = input_text[1] //+를 의미

        return when (op) { //해당 결과를 return 해줌.
            "+" -> (n1 + n2).toString()
            "-" -> (n1 - n2).toString()
            "X" -> (n1 * n2).toString()
            "%" -> (n1 % n2).toString()
            "/" -> (n1 / n2).toString()
            else -> ""
        }
    }
    fun clearButtonClicked(v: View) {
        txtInput.text = ""
        txtResult.text = ""
        isOperator = false
        hasOperator = false
    }
    fun historyButtonClicked(v:View) {
        historyLayout.isVisible=  true
        historyLinearLayout.removeAllViews()

        Thread(Runnable {
            db.historyDao().getAll().reversed().forEach{
                runOnUiThread{
                    val historyView=LayoutInflater.from(this).inflate(R.layout.history_row,null,false)
                    historyView.findViewById<TextView>(R.id.txt_expression).text=it.expression
                    historyView.findViewById<TextView>(R.id.txt_result).text="=${it.result}"

                    historyLinearLayout.addView(historyView)
                }
            }
        }).start()
    }
    fun historyClearButtonClicked(v:View){
        historyLinearLayout.removeAllViews()

        Thread(Runnable {
            db.historyDao().deleteAll()
        }).start()
    }
    fun closeHistoryButtonCLicked(v: View){
        historyLayout.isVisible=false
    }
}

private fun String.isNumber(): Boolean {
    return try{
        this.toBigInteger()
        true
    }catch ( e:NumberFormatException){
        false
    }
}
