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
import com.orpat.quickmix.model.RecipeData
import com.orpat.quickmix.model.RecipeDataClass
import com.orpat.quickmix.utility.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main_recipe_listing.MainRecipeListRV
import kotlinx.android.synthetic.main.fragment_main_recipe_listing.progressBar
import kotlinx.android.synthetic.main.fragment_recipe_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val ARG_CATEGORY = "category"

class RecipeListFragment : Fragment() {
    private var category: String? = null
    private var recipeAdapter: KRecyclerViewAdapter? = null
    private var recipesList: ArrayList<RecipeData> = ArrayList()
    private var recipe_id: String? = null
    private var searchString: String? = null
    private var page: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(ARG_CATEGORY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_list, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            RecipeListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY, param1)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRv()
        getApiData()
        searchBtn.setOnClickListener {
            println("SEARCH"+searchRecipesET.text?.trim())
            searchString = searchRecipesET.text?.trim().toString()
            getApiData()
        }

        clearSearch.setEndIconOnClickListener {
            searchRecipesET.setText("")
            searchString = ""
            getApiData()
        }
    }

    private fun setupRv() {
        recipeAdapter = KRecyclerViewAdapter(requireContext(), recipesList, object :
            KRecyclerViewHolderCallBack {
            override fun onCreateViewHolder(viewGroup: ViewGroup): KRecyclerViewHolder {
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.receipe_list_item_view, viewGroup, false)
                return QuickMixViewHolder(view)
            }

            override fun onHolderDisplayed(p0: KRecyclerViewHolder, p1: Int) {
            }

        }, KRecyclerViewItemClickListener { _, _, p1 ->


            val fragment = RecipeDetailFragment.newInstance(recipesList[p1].category_id.toString(),
                recipesList[p1].id.toString())
            val fragmentManager = activity?.supportFragmentManager
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container_view_tag,fragment)?.addToBackStack("tag")?.commit()


        })


        MainRecipeListRV?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        MainRecipeListRV?.adapter = recipeAdapter
    }

    private fun getApiData() {
        APIClient.ApiAuthInterface().getRecipes(category,recipe_id,searchString,page)
            .enqueue(object : Callback<RecipeDataClass> {
                override fun onResponse(
                    call: Call<RecipeDataClass>,
                    response: Response<RecipeDataClass>
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

                override fun onFailure(call: Call<RecipeDataClass>, t: Throwable) {
                   // Utils.showSnackbar("Something went wrong!",drawer_layout)
                    (activity as MainActivity).callSnackBar("Something went wrong!")

                    progressBar.visibility = View.GONE

                }
            })
    }
}