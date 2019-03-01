package com.time.demo.gntv.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.time.demo.gntv.R
import kotlinx.android.synthetic.main.fragment_content.*

/**
 *Project:GradientNavigationTextView
 *Author:君
 *Time:2019-02-28 11:58
 */
class ContentFragment : Fragment() {

    fun newInstance(name: String): ContentFragment {
        val fragment = ContentFragment()
        val data = Bundle()
        data.putString("name", name)
        fragment.arguments = data
        return fragment
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arguments
        tv.text = data?.get("name").toString()
    }

}