package com.orpat.quickmix

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.orpat.quickmix.activity.MainActivity
import com.orpat.quickmix.adapter.SideNavAdapter
import com.orpat.quickmix.model.SideNavItem
import kotlinx.android.synthetic.main.fragment_side_nav.*


class SideNavFragment :Fragment(){

    private val sideNavAdapter: SideNavAdapter = SideNavAdapter {position, item ->
        onItemClick(position,item)
    }

    private fun onItemClick(position: Int, item: SideNavItem) {
       // Toast.makeText(requireContext(),""+item.itemName,Toast.LENGTH_LONG).show()
        (activity as MainActivity).closeDrawer(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  = inflater.inflate(R.layout.fragment_side_nav, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {

        rv_side_nav_options?.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        val decorator = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        decorator.setDrawable(ContextCompat.getDrawable( requireActivity(),R.drawable.divider_ios)!!)
        rv_side_nav_options?.addItemDecoration(decorator)
        rv_side_nav_options?.adapter = sideNavAdapter

        sideNavAdapter.setNavItemsData(prepareNavItems())
    }

    //Create List of items to be displayed on the sidenav
    private fun prepareNavItems(): List<SideNavItem> {
        val menuItemsList = ArrayList<SideNavItem>()
        menuItemsList.add(SideNavItem(1,"About Quick Mix"))
        menuItemsList.add(SideNavItem(2,"Product Highlights"))
        menuItemsList.add(SideNavItem(3,"Quick Buy"))
        menuItemsList.add(SideNavItem(5,"Rewards"))
        menuItemsList.add(SideNavItem(6,"FAQ'S"))
        menuItemsList.add(SideNavItem(7,"Recipes"))
        menuItemsList.add(SideNavItem(4,"Demo Videos"))
        menuItemsList.add(SideNavItem(8,"Privacy Policy"))
        menuItemsList.add(SideNavItem(9,"Terms & Condition"))
        menuItemsList.add(SideNavItem(10,"My Account"))
        return menuItemsList
    }
}