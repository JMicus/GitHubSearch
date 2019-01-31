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
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

// Keep a reference to the NetworkFragment, which owns the AsyncTask object
// that is used to execute network ops.
private var mNetworkFragment: NetworkFragment? = null


// loaded repositories
private var repositories: MutableList<Repository> = ArrayList()

// pagination values
private var currentPage = 0
private var goingToNextPage = true // going to previous page otherwise

// are all respositories loaded?
private var allLoaded = false


class MainActivity : AppCompatActivity(), DownloadCallback<String> {

    var TAG = "Activity Main"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNetworkFragment = NetworkFragment.getInstance(supportFragmentManager, getString(R.string.url))

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

        // hide keyboard
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

        // reset search
        allLoaded = false

        startDownload("after", "")


    }

    fun clickLeft(view: View) {
        currentPage--

        goingToNextPage = false

        startDownload("before", repositories.get(0).cursor)

    }

    fun clickRight(view: View) {
        currentPage++

        goingToNextPage = true

        startDownload("after", repositories.get(repositories.lastIndex).cursor)
    }


    private fun startDownload(direction: String, cursor: String) {

        mNetworkFragment?.cancelDownload()

        // Execute the async download.
        mNetworkFragment?.apply {
            var etSearch = findViewById<EditText>(R.id.etSearch)
            startDownload(etSearch.text.toString(), resources.getInteger(R.integer.results_per_page), direction, cursor)
        }
    }

    override fun updateFromDownload(result: String?) {
        // Update your UI here based on result of download.
        Log.v(TAG, "RESULT: "+result)


        // list created with example data

        //var repositoryList: MutableList<Repository> = ArrayList()
        //repositoryList.add(Repository("example", "https://github.com/golang/example", "Go", "#375eab", "Go example projects"))
        //repositoryList.add(Repository("TensorFlow-Examples", "https://github.com/aymericdamien/TensorFlow-Examples", "Jupyter Notebook", "#DA5B0B", "TensorFlow Tutorial and Examples for Beginners with Latest APIs"))
        //repositoryList.add(Repository("examples", "https://github.com/pytorch/examples", "Python", "#3572A5", "A set of examples around pytorch in Vision, Text, Reinforcement Learning, etc."))


        // list created from example json file

        /*val exampleJson: String = File("exampleResponse.json").readText(Charsets.UTF_8)
        var exampleJson: String
        application.assets.open("exampleResponse.json").apply {
            exampleJson = this.readBytes().toString(Charsets.UTF_8)
        }.close()*/

        repositories = ResponseParser.createRepositories(result!!)

        var lvList = findViewById<ListView>(R.id.lvList)
        val adapter = CustomAdapter(this, repositories)
        lvList.adapter = adapter
        lvList.setOnItemClickListener(adapter)

        // update arrow visibility

        var ivLeft = findViewById<ImageView>(R.id.ivLeft)
        var ivRight = findViewById<ImageView>(R.id.ivRight)

        if (goingToNextPage) {
            ivLeft.visibility = View.VISIBLE
            if (repositories.size < resources.getInteger(R.integer.results_per_page)) ivRight.visibility = View.INVISIBLE
            else ivRight.visibility = View.VISIBLE
        }
        else {
            ivRight.visibility = View.VISIBLE
            if (currentPage > 0) ivLeft.visibility = View.VISIBLE
            else ivLeft.visibility = View.INVISIBLE
        }


    }

    override fun getActiveNetworkInfo(): NetworkInfo {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo
    }

    override fun onProgressUpdate(progressCode: Int, percentComplete: Int) {
        when (progressCode) {
            // You can add UI behavior for progress updates here.
            ERROR -> {
                Log.d(TAG, "Download error")
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




}