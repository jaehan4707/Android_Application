package com.example.calculator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_sub.*
class sub : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)

        //val db=MainActivity().db
        closeButton.setOnClickListener {
            finish()
        }
        /*
        historyClearButton.setOnClickListener{
            historyLinearLayout.removeAllViews()
            Thread(Runnable {
                db.historyDao().deleteAll()
            }).start()
        }
        record_Button.setOnClickListener {
            Thread(Runnable {
                db.historyDao().getAll().reversed().forEach {
                    // 뷰 생성하여 넣어주기
                    // 레이아웃 인플레이터 기능 사용 해보기
                    // ui 스레드 열기
                    runOnUiThread {
                        // 핸들러에 포스팅될 내용 작성

                        // R.layout.history_row 에서 인플레이트를 시킴., root랑 attachToRoot. 나중에 addview를 통해 붙일거라 null, false
                        val historyView =
                            LayoutInflater.from(this).inflate(R.layout.history_row, null, false)
                        historyView.findViewById<TextView>(R.id.expressionTextView).text = it.expression
                        historyView.findViewById<TextView>(R.id.resultTextView).text = "= ${it.result}"

                        historyLinearLayout.addView(historyView) // 뷰 추가
                    }
                } // 리스트 뒤집어서 가져오기
            }).start()
        }
    }

         */
    }
}