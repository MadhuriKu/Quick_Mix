package com.orpat.quickmix.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.orpat.quickmix.R
import com.orpat.quickmix.activity.MainActivity
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.model.RecipeData
import com.orpat.quickmix.model.RecipeDataClass
import com.orpat.quickmix.utility.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_recipe_detail.*
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val ARG_category_id = "category_id"
private const val ARG_recipe_id = "recipe_id"


class RecipeDetailFragment : Fragment() {
    private var category_id: String? = null
    private var recipe_id: String? = null
    private var searchString: String? = null
    private var page: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category_id = it.getString(ARG_category_id)
            recipe_id = it.getString(ARG_recipe_id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getApiData()
    }
    
    companion object {
        @JvmStatic
        fun newInstance(category_id: String, recipe_id: String) =
            RecipeDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_category_id, category_id)
                    putString(ARG_recipe_id, recipe_id)
                }
            }
    }

    private fun getApiData() {
        APIClient.ApiAuthInterface().getRecipes(category_id,recipe_id,searchString,page)
            .enqueue(object : Callback<RecipeDataClass> {
                override fun onResponse(
                    call: Call<RecipeDataClass>,
                    response: Response<RecipeDataClass>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val data = response.body()!!.data[0]
                        initView(data)
                    }else{
                       // Utils.showSnackbar(response.body()!!.message,drawer_layout)
                        (activity as MainActivity).callSnackBar(response.body()!!.message)
                    }
                }

                override fun onFailure(call: Call<RecipeDataClass>, t: Throwable) {
                  //  Utils.showSnackbar("Something went wrong!",drawer_layout)
                    (activity as MainActivity).callSnackBar("Something went wrong!")

                    progressBar.visibility = View.GONE

                }
            })
    }

    private fun initView(itemObject: RecipeData) {
        Glide.with(this@RecipeDetailFragment).load(itemObject.image).fitCenter()
            .placeholder(R.drawable.attachment1).into(receipe_image)
        recipe_main_title.text = itemObject.name
        calories_tv.text = itemObject.calories
        duration_tv.text = itemObject.duration
        difficulty_tv.text = itemObject.difficulty
        yield_tv.text = itemObject.yield
        ingredients_title.text = "Ingredients"
        ingredients_desc.text = itemObject.ingredients
        direction_title.text = "Direction"
        direction_desc.text = itemObject.direction

    }
}