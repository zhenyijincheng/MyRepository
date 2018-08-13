package com.toy.qiaodata.slidingconflict

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewList:ArrayList<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewList = ArrayList()
        viewList.add(getPageIndex1())
        val view2 = getPageIndex2()

        viewList.add(view2)
        viewList.add(getPageIndex3())
        viewPager.adapter = MyPagerAdapter(viewList)
    }

    private fun getPageIndex2():View {
        val view2 = View.inflate(this, R.layout.layout_page_item_2, null)

        val viewPager2 = view2.findViewById<ViewPager>(R.id.viewPager2)
        var viewList2:ArrayList<View> = ArrayList()
        viewList2.add(getPageIndex1())
        viewList2.add(getPageIndex3())
        viewPager2.adapter = MyPagerAdapter(viewList2)

        return view2
    }

    private fun getPageIndex3() = View.inflate(this, R.layout.layout_page_item_3, null)

    private fun getPageIndex1() = View.inflate(this, R.layout.layout_page_item_1, null)

    class MyPagerAdapter : PagerAdapter{
        var viewList:ArrayList<View>

        constructor(viewList:ArrayList<View>) : super() {
            this.viewList = viewList
        }


        override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
            return view == `object`
        }
        override fun getCount(): Int {
            return viewList.size
        }
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(viewList[position])
            return viewList[position]
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any?) {
            container.removeView(viewList[position])
        }
    }
}
