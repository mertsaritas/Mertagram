package com.mertsaritas.mertagram.utils

import android.content.Context
import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.mertsaritas.mertagram.Home.HomeActivity
import com.mertsaritas.mertagram.News.NewsActivity
import com.mertsaritas.mertagram.Profile.ProfileActivity
import com.mertsaritas.mertagram.R
import com.mertsaritas.mertagram.Search.SearchActivity
import com.mertsaritas.mertagram.Share.ShareActivity

class BottomnavigationViewHelper {
    companion object {
        fun setupBottomNavigationView(bottomnavigationViewEx: BottomNavigationViewEx) {

            bottomnavigationViewEx.enableAnimation(false)
            bottomnavigationViewEx.enableItemShiftingMode(false)
            bottomnavigationViewEx.enableShiftingMode(false)
            bottomnavigationViewEx.setTextVisibility(false)
        }

            fun setupNavigation (context: Context, bottomnavigationViewEx: BottomNavigationViewEx){

                bottomnavigationViewEx.onNavigationItemSelectedListener=object :BottomNavigationView.OnNavigationItemSelectedListener{
                    override fun onNavigationItemSelected(item: MenuItem): Boolean {
                        when(item.itemId){
                            R.id.ic_home->{
                                val intent=Intent(context,HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                                return true

                            }
                            R.id.ic_search->{
                                val intent=Intent(context,SearchActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                                return true
                            }
                            R.id.ic_share->{
                                val intent=Intent(context,ShareActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                                return true
                            }
                            R.id.ic_news->{
                                val intent=Intent(context,NewsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                                return true
                            }
                            R.id.ic_profile->{
                                val intent=Intent(context,ProfileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                                return true
                            }

                        }
                        return false
                    }

                }
            }
        }


    }
