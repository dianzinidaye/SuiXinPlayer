package com.example.suixinplayer.uit

import kotlinx.coroutines.newFixedThreadPoolContext
import java.util.ArrayList

class Download {
    /*  val list  = ArrayList<String>()
      fun te(string: String){
          list.add(string)
          println(list[0])
      }*/

    var arrayList = arrayListOf("哈哈", "呵呵", "呦呦")

    companion object {
        var i = "aaaa"
        fun t() {
            println("hahhah")


        }
    }

    var list = ArrayList<String>()
    fun tst(s: String) {
        arrayList.apply { }
        arrayList.let { }
        arrayList.run { }
        with(arrayList) {
            println(this[0])
        }
        list.add(s)
        println()
    }


}


data class Person(var name: String? = null, var age: Int? = null, var address: Address? = null)
data class Address(var address: String? = null)

fun person(block: Person.() -> Unit): Person {

   /* val person = Person()
    person.block()
    return person*/
    return Person().apply(block) //等价于上面3句
}

fun Person.address(block: Address.() -> Unit) {
   /* val are =  Address()
    this.address = are
    are.block()*/
    this.address = Address().apply(block)//等价于上面3句
}


fun main(args: Array<String>) {

    val person: Person = person {
        this.name = "王三"
        age = 20
        address {
           /*
           this@person.name = "aa" //因为是Person的拓展类,所有也能访问Person的对象
            this.address = "广西"  //因为带Address接收者,所以也能访问Address的对象
            */
            address = "广东"
        }
    }
    println("person = $person")
}