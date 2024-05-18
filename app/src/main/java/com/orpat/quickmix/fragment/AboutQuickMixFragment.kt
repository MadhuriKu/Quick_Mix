package com.orpat.quickmix.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kalpesh.krecyclerviewadapter.KRecyclerViewAdapter
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolder
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolderCallBack
import com.kalpesh.krecyclerviewadapter.KRecyclerViewItemClickListener
import com.orpat.quickmix.R
import com.orpat.quickmix.adapter.QuickMixViewHolder
import com.orpat.quickmix.model.AboutScroll
import com.orpat.quickmix.model.ProductImageScroll
import kotlinx.android.synthetic.main.fragment_about_quick_mix.*
import kotlinx.android.synthetic.main.fragment_quick_buy.*



class AboutQuickMixFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_quick_mix, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProductRv()
    }

    private fun setupProductRv() {
        val adapter = KRecyclerViewAdapter(this.requireContext(), prepareAboutScrollItems(), object : KRecyclerViewHolderCallBack {
            override fun onCreateViewHolder(viewGroup: ViewGroup): KRecyclerViewHolder {
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.about_item_view, viewGroup, false)
                return QuickMixViewHolder(view)
            }


            override fun onHolderDisplayed(p0: KRecyclerViewHolder, p1: Int) {
            }
        }, KRecyclerViewItemClickListener { _, _, p1 ->

        })


        about_rv?.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        about_rv?.adapter = adapter
    }

    private fun prepareAboutScrollItems(): ArrayList<AboutScroll> {
        val menuItemsList = ArrayList<AboutScroll>()
        menuItemsList.add(AboutScroll(1,
            resources.getString(R.string.about_content1_title_string),
            resources.getString(R.string.about_content1_string)))
        menuItemsList.add(AboutScroll(2,
            resources.getString(R.string.about_content2_title_string),
            resources.getString(R.string.about_content2_string)))
        return menuItemsList
    }
}