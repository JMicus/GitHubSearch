package com.micus.githubsearch

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.opengl.Visibility
import android.util.Log
import android.view.Gravity
import android.widget.*

// Keep a reference to the NetworkFragment, which owns the AsyncTask object
// that is used to execute network ops.
private var mNetworkFragment: NetworkFragment? = null

// Boolean telling us whether a download is in progress, so we don't trigger overlapping
// downloads with consecutive button clicks.
private var mDownloading = false


class MainActivity : AppCompatActivity(), DownloadCallback<String> {

    var TAG = "Activity Main"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNetworkFragment = NetworkFragment.getInstance(supportFragmentManager, "https://api.github.com/graphql")

        // hide action bar
        val actionBar = supportActionBar
        actionBar?.hide()

    }

    override fun onBackPressed() {

        // change view to show list
        var ivIcon = findViewById<ImageView>(R.id.ivIcon)

        if (ivIcon.visibility == View.VISIBLE) super.onBackPressed()

        else {
            var llMain = findViewById<LinearLayout>(R.id.llMain)
            var lvList = findViewById<ListView>(R.id.lvList)

            var tvTitle = findViewById<TextView>(R.id.tvTitle)


            llMain.setVerticalGravity(Gravity.CENTER)
            lvList.visibility = View.GONE
            ivIcon.visibility = View.VISIBLE
            tvTitle.visibility = View.VISIBLE
        }

    }


    fun clickSearch(view: View) {
        Log.v(TAG, "search clicked")

        // change view to show list
        var llMain = findViewById<LinearLayout>(R.id.llMain)
        var lvList = findViewById<ListView>(R.id.lvList)
        var ivIcon = findViewById<ImageView>(R.id.ivIcon)
        var tvTitle = findViewById<TextView>(R.id.tvTitle)

        llMain.setVerticalGravity(Gravity.TOP)
        lvList.visibility = View.VISIBLE
        ivIcon.visibility = View.GONE
        tvTitle.visibility = View.GONE

        startDownload()
    }

    private fun startDownload() {
        if (!mDownloading) {
            // Execute the async download.
            mNetworkFragment?.apply {
                startDownload()
                mDownloading = true
            }
        }
    }

    override fun updateFromDownload(result: String?) {
        // Update your UI here based on result of download.
        Log.v(TAG, "RESULT: "+result)


        // TODO list is created with example data

        val repositoryList: MutableList<Repository> = ArrayList()
        repositoryList.add(Repository("example", "https://github.com/golang/example", "Go", "#375eab", "Go example projects"))
        repositoryList.add(Repository("TensorFlow-Examples", "https://github.com/aymericdamien/TensorFlow-Examples", "Jupyter Notebook", "#DA5B0B", "TensorFlow Tutorial and Examples for Beginners with Latest APIs"))
        repositoryList.add(Repository("examples", "https://github.com/pytorch/examples", "Python", "#3572A5", "A set of examples around pytorch in Vision, Text, Reinforcement Learning, etc."))


        var lvList = findViewById<ListView>(R.id.lvList)
        val adapter = CustomAdapter(this, repositoryList)
        lvList.adapter = adapter
        lvList.setOnItemClickListener(adapter)
    }

    override fun getActiveNetworkInfo(): NetworkInfo {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo
    }

    override fun onProgressUpdate(progressCode: Int, percentComplete: Int) {
        when (progressCode) {
            // You can add UI behavior for progress updates here.
            ERROR -> {
            }
            CONNECT_SUCCESS -> {
            }
            GET_INPUT_STREAM_SUCCESS -> {
            }
            PROCESS_INPUT_STREAM_IN_PROGRESS -> {
            }
            PROCESS_INPUT_STREAM_SUCCESS -> {
            }
        }
    }

    override fun finishDownloading() {
        mDownloading = false
        mNetworkFragment?.cancelDownload()
    }



}