package com.toy.qiaodata.slidingconflict

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_coordinator.*
import java.util.*

class CoordinatorActivity : AppCompatActivity() {

    var expand = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator)


        rv_content_1.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        val adapter = Adapter(R.layout.layout_list_item_1)
        val view = View(this)
        view.layoutParams = ViewGroup.LayoutParams(-1,50)
        adapter.addHeaderView(view)
        adapter.bindToRecyclerView(rv_content_1)
        var n = 0
        while ( n < 100) {
            n++
            adapter.addData("第" + n + "条item")
        }
        adapter.notifyDataSetChanged()

        fab.setOnClickListener {
            if (expand) {
                abl1.setExpanded(false,true)
            } else {
                abl1.setExpanded(true,true)

            }
            expand = !expand
        }

        abl1.addOnOffsetChangedListener(object :AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State) {
                Log.d("ABL", "state:$state")
            }

        })

    }

    class Adapter(layoutResId: Int) : BaseQuickAdapter<String, BaseViewHolder>(layoutResId) {
        override fun convert(helper: BaseViewHolder?, item: String?) {
            helper?.setText(R.id.tv_content,item)
        }

    }

    abstract class AppBarStateChangeListener :AppBarLayout.OnOffsetChangedListener {
        public enum class State {
            EXPANDED,
            COLLAPSED,
            IDLE
        }

        private var currentState = State.IDLE

        override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
            if (appBarLayout != null) {
                when {
                    verticalOffset == 0 -> {
                        if (currentState != State.EXPANDED) {
                            onStateChanged(appBarLayout, State.EXPANDED);
                        }
                        currentState = State.EXPANDED
                    }
                    Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange() -> {
                        if (currentState != State.COLLAPSED) {
                            onStateChanged(appBarLayout, State.COLLAPSED)
                        }
                        currentState = State.COLLAPSED
                    }
                    else -> {
                        if (currentState != State.IDLE) {
                            onStateChanged(appBarLayout, State.IDLE)
                        }
                        currentState = State.IDLE
                    }
                }
            }
        }

        abstract fun onStateChanged(appBarLayout: AppBarLayout?,state:State)

    }
}
