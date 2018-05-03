package com.zhaopin.qiaodata.kotlin

import java.util.*

/**
 * 出租店应用程序，记录
 * Created by YANG on 2018/4/4.
 */

class RentalStore {
    companion object {
        fun main() {


        }
    }

}

class Rental {
    private var movie: Movie? = null
    private var dayRented: Int = 0

    constructor(movie: Movie?, dayRented: Int) {
        this.movie = movie
        this.dayRented = dayRented
    }


}

class Movie {
    companion object  {
        val CHILDRENS = 1
        val REGULAR = 0
        val NEW_RELEAST = 2
    }

    private var _title: String? = null
    private var _priceCode: Int = 0

    constructor(_title: String, _priceCode: Int) {
        this._title = _title
        this._priceCode = _priceCode
    }

    fun getTitle(): String? {
        return _title
    }

    fun getPreiceCode(): Int {
        return _priceCode
    }

    fun setTitle(x: String) {
        _title = x
    }

    fun setPrice(x: Int) {
        _priceCode = x
    }
}

class Customer {
    private var name: String = "nameless"
    private val rentals: Vector<Rental> = Vector()

    constructor(name: String) {
        this.name = name
    }

    fun addRental(arg: Rental): Unit {
        rentals.addElement(arg)
    }
}