package com.example.todolist

import android.view.View
import android.view.ViewGroup
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter

//데이터를 리스트 뷰에 어떻게 표시할지 정의하는 객체
class TodoListAdapter (realmResult: OrderedRealmCollection<Todo>)
    : RealmBaseAdapter<Todo>(realmResult){

    //리스트 뷰의 각 아이템에 표시할 뷰를 구성
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        TODO("Not yet implemented")
    }
}