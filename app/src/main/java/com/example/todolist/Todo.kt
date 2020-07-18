//Realm 데이터베이스
package com.example.todolist

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Todo(
    @PrimaryKey var id: Long = 0, //고유아이디(유일값)
    var title: String = "", //할 일 내용
    var date: Long = 0 //시간
) : RealmObject(){
}
