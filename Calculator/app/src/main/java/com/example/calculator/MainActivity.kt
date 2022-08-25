package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.lang.ArithmeticException
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    enum class Opkind {
        Plus, Minus, Times, Divide
    }

    companion object {
        fun Opkind.compute(a: BigDecimal, b: BigDecimal) = when (this) {
            Opkind.Plus -> a + b
            Opkind.Minus -> a - b
            Opkind.Times -> a * b
            Opkind.Divide -> a.divide(b, 10, RoundingMode.HALF_EVEN)
        }
    }

    private val txtInput by lazy<TextView> {findViewById(R.id.input_text)}
    private val txtResult by lazy<TextView> { findViewById(R.id.result_text)}
    private var lastResult: BigDecimal = BigDecimal.ZERO;
    private var lastOp: Opkind? = null
    private var waitingNextOperand: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.num_0_button).setOnClickListener { appendText("0") }
        findViewById<Button>(R.id.num_1_button).setOnClickListener { appendText("1") }
        findViewById<Button>(R.id.num_2_button).setOnClickListener { appendText("2") }
        findViewById<Button>(R.id.num_3_button).setOnClickListener { appendText("3") }
        findViewById<Button>(R.id.num_4_button).setOnClickListener { appendText("4") }
        findViewById<Button>(R.id.num_5_button).setOnClickListener { appendText("5") }
        findViewById<Button>(R.id.num_6_button).setOnClickListener { appendText("6") }
        findViewById<Button>(R.id.num_7_button).setOnClickListener { appendText("7") }
        findViewById<Button>(R.id.num_8_button).setOnClickListener { appendText("8") }
        findViewById<Button>(R.id.num_9_button).setOnClickListener { appendText("9") }
        findViewById<Button>(R.id.num_0_button).setOnClickListener { appendText("0") }
        findViewById<Button>(R.id.dot_button).setOnClickListener { appendText(".") }
        findViewById<Button>(R.id.result_clear).setOnClickListener { resultClear() }
        findViewById<Button>(R.id.left_right_button).setOnClickListener { appendText(")") }
        findViewById<Button>(R.id.del_button).setOnClickListener {
            val currentText = txtInput.text.toString()
            val newText = currentText.substring(0, currentText.length - 1)
            txtInput.text = if (newText.isEmpty() || newText == "-") "0" else newText
        }
        findViewById<Button>(R.id.clear_button).setOnClickListener { clearText() }
        findViewById<Button>(R.id.plus_button).setOnClickListener { calc(Opkind.Plus) }
        findViewById<Button>(R.id.minus_button).setOnClickListener { calc(Opkind.Minus) }
        findViewById<Button>(R.id.times_button).setOnClickListener { calc(Opkind.Times) }
        findViewById<Button>(R.id.divide_button).setOnClickListener { calc(Opkind.Divide) }
        findViewById<Button>(R.id.equals_button).setOnClickListener { calc(null) }

        clearText()
    }

    private fun clearText() {
        txtInput.text = "0"
    }
    private fun resultClear(){
        txtResult.text="0"
    }
    private fun appendText(text: String) {
        if (waitingNextOperand) {
            clearText()
            waitingNextOperand = false
        }
        val currentText = txtInput.text.toString()
        txtInput.text = if (currentText == "0") text else currentText + text
    }

    private fun calc(nextOp: Opkind?) {
        if (waitingNextOperand) {
            lastOp = nextOp
            return
        }
        val currentValue = BigDecimal(txtInput.text.toString())
        val newValue = try {
            lastOp?.compute(lastResult, currentValue) ?: currentValue
        } catch (e: ArithmeticException) {
            lastOp = null
            waitingNextOperand = true
            Toast.makeText(
                applicationContext,
                "invalid operation!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (nextOp != null) lastResult = newValue
        if (lastOp != null) txtResult.text = newValue.toPlainString()
        lastOp = nextOp
        waitingNextOperand = nextOp != null
    }
}