package com.orpat.quickmix.activity

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.orpat.quickmix.BuildConfig
import com.orpat.quickmix.R
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.fragment.*
import com.orpat.quickmix.model.SideNavItem
import com.orpat.quickmix.model.TokenDataClass
import com.orpat.quickmix.prefs
import com.orpat.quickmix.utility.Constants
import com.orpat.quickmix.utility.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_bottom_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


const val KEY_URL ="url"
class MainActivity : AppCompatActivity() {

    lateinit var imageDish:ImageView
    lateinit var autoCleanBtn:ImageButton
    lateinit var sauceBtn:ImageButton
    lateinit var soupBtn:ImageButton
    lateinit var icecreamBtn:ImageButton
    lateinit var smoothieBtn:ImageButton
    lateinit var drawer_layout:DrawerLayout

    var rotationAxis:Float = 180.0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageDish = findViewById(R.id.imageDish)
        autoCleanBtn = findViewById(R.id.auto_clean_btn)
        sauceBtn = findViewById(R.id.sauces_btn)
        soupBtn = findViewById(R.id.soups_btn)
        icecreamBtn = findViewById(R.id.ice_cream_btn)
        smoothieBtn = findViewById(R.id.smoothie_btn)
        drawer_layout = findViewById(R.id.drawer_layout)

        imageDish.setImageDrawable(resources.getDrawable(R.drawable.smoothie))
        initHomeButtons()
        println("prefs.isTokenPresent"+prefs.isTokenPresent)
        if (!prefs.isTokenPresent) {

            APIClient.ApiAuthTokenInterface().generateRefreshToken()
                .enqueue(object :Callback<TokenDataClass> {
                    override fun onResponse(
                        call: Call<TokenDataClass>,
                        response: Response<TokenDataClass>
                    ) {

                        if (response.isSuccessful) {

                            val token = response.body()!!.data.token


                            prefs.token = token
                            prefs.isTokenPresent = true
                        }else{
                            Utils.showSnackbar(response.body()!!.message,drawer_layout)
                        }
                    }

                    override fun onFailure(call: Call<TokenDataClass>, t: Throwable) {
                        Utils.showSnackbar("Something went 1 wrong!",drawer_layout)

                    }
                })
        }

    }

    fun callSnackBar(message:String){
        Utils.showSnackbar(message,drawer_layout)
    }

    private fun initHomeButtons(){
        clearButtonIcon()
        imageDish.setImageDrawable(resources.getDrawable(R.drawable.smoothie))
        smoothieBtn.setColorFilter(getResources().getColor(R.color.theme_red), PorterDuff.Mode.SRC_ATOP);
        home_nav_btn.setColorFilter(getResources().getColor(R.color.theme_red), PorterDuff.Mode.SRC_ATOP);
    }

    private fun clearBottomButton(){
        home_nav_btn.setColorFilter(getResources().getColor(R.color.theme_grey), PorterDuff.Mode.SRC_ATOP);
        cart_nav_btn.setColorFilter(getResources().getColor(R.color.theme_grey), PorterDuff.Mode.SRC_ATOP);

    }

    fun bottomNavClick(v:View){
        clearBottomButton()
        when (v.id) {
            R.id.home_nav_btn ->{

                initHomeButtons()
                println("COUNT BACKSTACK"+supportFragmentManager.backStackEntryCount)
                 val backStackCount = supportFragmentManager.backStackEntryCount
                supportFragmentManager.popBackStack("tag",POP_BACK_STACK_INCLUSIVE)
            }
            R.id.ty_confirm_btn ->{
                initHomeButtons()
                println("COUNT BACKSTACK"+supportFragmentManager.backStackEntryCount)
                 val backStackCount = supportFragmentManager.backStackEntryCount
                supportFragmentManager.popBackStack("tag",POP_BACK_STACK_INCLUSIVE)
            }

            R.id.cart_nav_btn ->{
                cart_nav_btn.setColorFilter(getResources().getColor(R.color.theme_red), PorterDuff.Mode.SRC_ATOP);
                val fragment = ShoppingCartFragment()
                callFragment(fragment)
            }

            R.id.center_nav_btn ->{
                val fragment = QuickBuyFragment()
                callFragment(fragment)
            }
        }
    }


    fun openPage(v: View) {
        when (v.id) {
            R.id.drawer_btn -> {
                drawer_layout.openDrawer(GravityCompat.START)
            }

            R.id.menu_icon -> {
                drawer_layout.openDrawer(GravityCompat.START)
            }

            R.id.play_video_view ->{
                val fragment = WebViewFragment.newInstance(BuildConfig.BASE_URL+Constants.QUICKMIX_DEMO_VIDEOS)
                callFragment(fragment)
            }

            R.id.back_btn -> {
                setBottomNavVisibility(true)

                if (supportFragmentManager.backStackEntryCount >0){
                    supportFragmentManager.popBackStack()
                }
                if(supportFragmentManager.backStackEntryCount == 1){
                    clearBottomButton()
                    initHomeButtons()
                }

            }

            R.id.try_recipes ->{
                val fragment = MainRecipeListingFragment()
                callFragment(fragment)
            }
            R.id.live_demo_play_btn ->{
                val fragment = RequestLiveDemoFragment()
                callFragment(fragment)
            }

        }
    }



    fun closeDrawer(item: SideNavItem) {
        clearBottomButton()
        drawer_layout?.closeDrawer(GravityCompat.START)
        when (item.id) {
            1 -> {
               //ABout QuickMix
                val fragment = AboutQuickMixFragment()
                callFragment(fragment)
            }
            2 -> {
                //Product Highlights
                val fragment = ProductHighlightFragment()
                callFragment(fragment)
            }
            3 -> {
                //Quick Buy
                val fragment = QuickBuyFragment()
                callFragment(fragment)
            }
            4 -> {
                //Demo Video
                val fragment = WebViewFragment.newInstance(BuildConfig.BASE_URL+Constants.QUICKMIX_DEMO_VIDEOS)
                callFragment(fragment)
            }
            5 -> {
                //Rewards
                val fragment = WebViewFragment.newInstance(BuildConfig.BASE_URL+Constants.QUICKMIX_REWARDS)
                callFragment(fragment)
            }
            6->{
                //FAQ'S
                val fragment = WebViewFragment.newInstance(BuildConfig.BASE_URL+Constants.QUICKMIX_FAQ)
                callFragment(fragment)
            }
            7 -> {
                //Recipes
                val fragment = MainRecipeListingFragment()
                callFragment(fragment)
            }
            8 -> {
                //Privacy Policy
                val fragment = WebViewFragment.newInstance(BuildConfig.BASE_URL+Constants.QUICKMIX_PRIVACY_POLICY)
                callFragment(fragment)

            }
            9 -> {
                //Terms & Condition
                //setBottomNavVisibility(false)
                val fragment = WebViewFragment.newInstance(BuildConfig.BASE_URL+Constants.QUICKMIX_TNC)
                callFragment(fragment)
            }

            10 -> {
                //My Account
                val fragment = MyAccountFragment()
                callFragment(fragment)
            }

        }
    }
    fun setBottomNavVisibility(visibilityStatus :Boolean){
        if(visibilityStatus){
            bottom_nav_view.visibility = View.VISIBLE
        }else{
            bottom_nav_view.visibility = View.GONE
        }
    }

    private fun callFragment(fragment: Fragment) {
      //  fragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fragment_container_view_tag,fragment).addToBackStack("tag").commit()
    }

    private fun getTheRotationValue(): Float {

        if (rotationAxis == 180.0f)
            rotationAxis = 0.0f
        else
            rotationAxis = 180.0f

        return rotationAxis
    }

    fun rotateDishAnim(v: View) {
        when (v.id) {
            R.id.auto_clean_btn -> {
                clearButtonIcon()
                autoCleanBtn.setColorFilter(getResources().getColor(R.color.theme_red), PorterDuff.Mode.SRC_ATOP);
                val rotate = getTheRotationValue()
                imageDish.animate().rotation(rotate).alpha(0.5f).alpha(1f).start()
                imageDish.setImageDrawable(resources.getDrawable(R.drawable.auto_clean))

            }
            R.id.sauces_btn -> {
                clearButtonIcon()
                sauceBtn.setColorFilter(getResources().getColor(R.color.theme_red), PorterDuff.Mode.SRC_ATOP);
                val rotate = getTheRotationValue()
                imageDish.animate().rotation(rotate).alpha(0.5f).alpha(1f).start()
                imageDish.setImageDrawable(resources.getDrawable(R.drawable.sauces))

            }
            R.id.soups_btn -> {
                clearButtonIcon()
                soupBtn.setColorFilter(getResources().getColor(R.color.theme_red), PorterDuff.Mode.SRC_ATOP);
                imageDish.animate().rotation(getTheRotationValue()).alpha(0.5f).alpha(1f).start()
                imageDish.setImageDrawable(resources.getDrawable(R.drawable.soup))

            }
            R.id.ice_cream_btn -> {
                clearButtonIcon()
                icecreamBtn.setColorFilter(getResources().getColor(R.color.theme_red), PorterDuff.Mode.SRC_ATOP);
                imageDish.animate().rotation(getTheRotationValue()).alpha(0.5f).alpha(1f).start()
                imageDish.setImageDrawable(resources.getDrawable(R.drawable.ice_cream))

            }
            R.id.smoothie_btn -> {
                clearButtonIcon()
                smoothieBtn.setColorFilter(getResources().getColor(R.color.theme_red), PorterDuff.Mode.SRC_ATOP);
                imageDish.animate().rotation(getTheRotationValue()).alpha(0.5f).alpha(1f).start()
                imageDish.setImageDrawable(resources.getDrawable(R.drawable.smoothie))

            }
        }
    }

    private fun clearButtonIcon() {
        autoCleanBtn.setColorFilter(resources.getColor(R.color.theme_grey), PorterDuff.Mode.SRC_ATOP)
        sauceBtn.setColorFilter(resources.getColor(R.color.theme_grey), PorterDuff.Mode.SRC_ATOP)
        soupBtn.setColorFilter(resources.getColor(R.color.theme_grey), PorterDuff.Mode.SRC_ATOP)
        icecreamBtn.setColorFilter(resources.getColor(R.color.theme_grey), PorterDuff.Mode.SRC_ATOP)
        smoothieBtn.setColorFilter(resources.getColor(R.color.theme_grey), PorterDuff.Mode.SRC_ATOP)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = RequestLiveDemoFragment()
        fragment.onActivityResult(requestCode, resultCode, data)

    }

}
