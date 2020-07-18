package com.example.todolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    private val realm = Realm.getDefaultInstance() //Realm 객체를 초기화

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //전체 할 일 정보를 가져와서 날짜순으로 내림차순 정렬
        val realmResult = realm.where<Todo>()
            .findAll()
            .sort("date", Sort.DESCENDING) //날짜순으로 내림차순 정렬

        //새 할 일 추가
        fab.setOnClickListener {
            startActivity<EditActivity>()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() { //액티비티가 소멸되는 생명주기
        super.onDestroy()
        realm.close() //사용이 끝난 인스턴스 해제
    }
}