package com.example.todolist

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter

//데이터를 리스트 뷰에 어떻게 표시할지 정의하는 객체
class TodoListAdapter (realmResult: OrderedRealmCollection<Todo>)
    : RealmBaseAdapter<Todo>(realmResult){

    //뷰 홀더 패턴 클래스 - 전달받은 view 에서 텍스트 뷰들의 참조를 저장
    class TodoViewHolder(view: View) {
        val dateTextView: TextView = view.findViewById(R.id.text1)
        val textTextView: TextView = view.findViewById(R.id.text2)
    }

    //리스트 뷰의 각 아이템에 표시할 뷰를 구성
    //매 아이템이 화면에 보일 때마다 호출
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        //position: 리스트 뷰의 아이템 위치, convertView: 재활용되는 아이템의 뷰, parent: 리스트 뷰의 참조
        val vh: TodoViewHolder
        val view: View

        //convertView는 아이템이 작성되기 전에는 null이고 한 번 작성되면 이전에 작성했던 뷰를 전달
        if(convertView == null) { //convertView가 null이면 레이아웃 작성
            //LayoutInflater 클래스: XML 레이아웃 파일 코드 호출
            view = LayoutInflater.from(parent?.context) //객체를 얻는다
                .inflate(R.layout.item_todo, parent, false) //XML 파일을 읽어서 뷰로 반환하여 view 변수에 할당

            vh = TodoViewHolder(view) //뷰 홀더 객체를 초기화
            view.tag = vh //뷰 홀더 객체는 tag 프로퍼티(Any형으로 어떠한 객체도 저장 가능)로 view에 저장
        }
        else { //convertView가 null이 아니라면
            view = convertView // 이전에 작성했던 convertView를 재사용
            vh = view.tag as TodoViewHolder // 뷰 홀더 객체를 tag 프로퍼티에서 꺼낸다.
                                            // 반환되는 데이터형이 Any이므로 TodoViewHolder형으로 형변환
        }

        //let 함수 - 블록에 자기 자신을 인수로 전달하고 수행된 결과를 반환, 인수로 전달된 객체는 it
        //let 은 안전한 호출 연산자 ? 와 함께 사용하면 null 값이 아닐 때만 실행하는 코드를 나타냄
        adapterData?.let {
            //adapterData?.let == if(adapterData!=null)
            val item = it[position] //해당하는 위치의 데이터를 item 변수에 담는다
            vh.textTextView.text = item.title
            vh.dateTextView.text = DateFormat.format("yyyy/MM/dd", item.date)
        }

        return view //완성된 view 변수 반환. 이 뷰는 다음 번에 호출되면 convertView 로 재사용
    }

    //데이터베이스 레코드의 고유한 아이디 반환
    override fun getItemId(position: Int): Long {
        adapterData?.let {
            return it[position].id
        }
        return super.getItemId(position)
    }

}