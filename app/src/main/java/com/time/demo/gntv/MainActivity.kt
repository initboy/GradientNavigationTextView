package com.time.demo.gntv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.time.demo.gntv.adapter.ViewPagerAdapter
import com.time.demo.gntv.fragment.ContentFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        val data = ArrayList<Fragment>()
        val titles = arrayOf("第一标题", "第二标题", "第三标题")
        data.add(ContentFragment().newInstance("第一页面"))
        data.add(ContentFragment().newInstance("第二页面"))
        data.add(ContentFragment().newInstance("第三页面"))
        val adapter = ViewPagerAdapter(supportFragmentManager, data)
        view_pager.adapter = adapter
        title_bar.setViewPager(
            view_pager, titles,
            16F,
            16F,
            120,
            20F,
            true,
            0,
            0F,
            0F,
            0F,
            0
        )
    }
}
