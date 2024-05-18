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
import com.orpat.quickmix.activity.MainActivity
import com.orpat.quickmix.adapter.QuickMixViewHolder
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.model.RecipeCategoryData
import com.orpat.quickmix.model.RecipeCategoryDataClass
import com.orpat.quickmix.utility.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main_recipe_listing.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRecipeListingFragment : Fragment() {

    private var recipeAdapter: KRecyclerViewAdapter? = null
    private var recipesList: ArrayList<RecipeCategoryData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_recipe_listing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRv()
        getApiData()

    }

    private fun setupRv() {
        recipeAdapter = KRecyclerViewAdapter(requireContext(), recipesList, object :
            KRecyclerViewHolderCallBack {
            override fun onCreateViewHolder(viewGroup: ViewGroup): KRecyclerViewHolder {
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.main_recipe_item_view, viewGroup, false)
                return QuickMixViewHolder(view)
            }

            override fun onHolderDisplayed(p0: KRecyclerViewHolder, p1: Int) {
            }

        }, KRecyclerViewItemClickListener { _, _, p1 ->

            val fragment = RecipeListFragment.newInstance(recipesList[p1].categoryId.toString())
            val fragmentManager = activity?.supportFragmentManager
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container_view_tag,fragment)?.addToBackStack("tag")?.commit()

        })


        MainRecipeListRV?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        MainRecipeListRV?.adapter = recipeAdapter
    }

    private fun getApiData() {
        APIClient.ApiAuthInterface().getRecipeCategory()
            .enqueue(object : Callback<RecipeCategoryDataClass> {
                override fun onResponse(
                    call: Call<RecipeCategoryDataClass>,
                    response: Response<RecipeCategoryDataClass>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val data = response.body()!!.data
                        recipesList.clear()
                        recipesList.addAll(data)
                        recipeAdapter!!.notifyDataSetChanged()

                    }else{
                        //Utils.showSnackbar(response.body()!!.message,drawer_layout)
                        (activity as MainActivity).callSnackBar( response.body()!!.message)
                    }
                }

                override fun onFailure(call: Call<RecipeCategoryDataClass>, t: Throwable) {
                    //Utils.showSnackbar("Something went wrong!",drawer_layout)
                    (activity as MainActivity).callSnackBar("Something went wrong!")
                    progressBar.visibility = View.GONE

                }
            })
    }

}