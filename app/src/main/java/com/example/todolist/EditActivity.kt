//두 번째 화면: 할 일을 추가하거나 수정
package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.util.*

class EditActivity : AppCompatActivity() {

    private val realm = Realm.getDefaultInstance() //Realm 객체의 인스턴스를 얻음
    private val calendar: Calendar = Calendar.getInstance() //오늘 날짜로 초기화된 캘린더 객체 생성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        //업데이트 조건
        val id = intent.getLongExtra("id", -1L) //첫 번째 화면에서 인텐트를 이용해 id값을 전달받음
        if(id == -1L) {
            insertMode()
        } else{
            updateMode(id)
        }

        //캘린더 뷰의 날짜를 선택했을 때 Calendar 객체에 설정
        calendarView.setOnDateChangeListener{ view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
    }

    //추가 모드 초기화
    private fun insertMode(){
        //삭제 버튼을 감추기
        deleteFab.hide()
        //완료 버튼을 클릭하면 추가
        doneFab.setOnClickListener{
            insertTodo() //할 일 추가
        }
    }

    //수정 모드 초기화
    private fun updateMode(id: Long){
        //id에 해당하는 객체를 화면에 표시
        val todo = realm.where<Todo>().equalTo("id", id).findFirst()!!
        todoEditText.setText(todo.title)
        calendarView.date = todo.date

        //완료 버튼을 클릭하면 수정
        doneFab.setOnClickListener {
            updateTodo(id)
        }

        //삭제 버튼을 클릭하면 삭제
        deleteFab.setOnClickListener {
            deleteTodo(id)
        }
    }

    //다음 id를 반환 - Realm은 기본키 자동 증가 기능을 지원하지 않음
    private fun nextId(): Int{
        val maxId = realm.where<Todo>().max("id") //현재 id 중 가장 큰 값
        //where<Todo>: Todo 테이블의 모든 값을 얻는 메서드
        if (maxId != null) {
            return maxId.toInt() + 1
        }
        return 0
    }

    //할 일 추가
    private fun insertTodo(){
        realm.beginTransaction() //트랜잭션(데이터베이스의 작업 단위) 시작
                                 //Realm에서 데이터를 추가,삭제,업데이트
        val todo = realm.createObject<Todo>(nextId()) //새로운 Realm 객체 생성 - nextId(): 기본키 지정
        todo.title = todoEditText.text.toString()
        todo.date = calendar.timeInMillis //timeInMillis: Long형 값으로 변환

        realm.commitTransaction() //트랜잭션 종료 반영

        //다이얼로그 표시
        alert("내용이 추가되었습니다."){
            yesButton{ finish() } //다이얼로그 확인 버튼 -> finish(): 현재 액티비티 종료
        }.show()
    }

    //할 일 수정
    private fun updateTodo(id: Long){ //id를 인자로 받음
        realm.beginTransaction() //트랜잭션 시작

        val updateItem = realm.where<Todo>().equalTo("id", id).findFirst()!! //id컬럼에 id값이 있다면 첫 번째 데이터를 반환
        updateItem.title = todoEditText.text.toString()
        updateItem.date = calendar.timeInMillis //timeInMillis: Long형 값으로 변환

        realm.commitTransaction() //트랜잭션 종료 반영

        //다이얼로그 표시
        alert("내용이 변경되었습니다."){
            yesButton{ finish() } //다이얼로그 확인 버튼 -> finish(): 현재 액티비티 종료
        }.show()
    }

    //할 일 삭제
    private fun deleteTodo(id: Long){
        realm.beginTransaction()

        val deleteItem = realm.where<Todo>().equalTo("id", id).findFirst()!!
        deleteItem.deleteFromRealm() //삭제할 객체를 찾아서 삭제

        realm.commitTransaction()

        //다이얼로그 표시
        alert("내용이 삭제되었습니다."){
            yesButton{ finish() } //다이얼로그 확인 버튼 -> finish(): 현재 액티비티 종료
        }.show()
    }

    override fun onDestroy() { //액티비티가 소멸되는 생명주기
        super.onDestroy()
        realm.close() //사용이 끝난 인스턴스 해제
    }
}