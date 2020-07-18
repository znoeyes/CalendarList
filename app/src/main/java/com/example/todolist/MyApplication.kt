package com.example.todolist

import android.app.Application
import io.realm.Realm

class MyApplication : Application() { //Application 클래스를 상속받음
    override fun onCreate() { //액티비티가 생성되기 전에 호출되는 메서드
        super.onCreate()
        Realm.init(this) //Realm 초기화
    }
}