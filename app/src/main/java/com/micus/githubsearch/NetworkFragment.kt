package com.micus.githubsearch

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment

private const val TAG = "NetworkFragment"
private const val URL_KEY = "UrlKey"

class NetworkFragment : Fragment() {
    private var mCallback: DownloadCallback<String>? = null
    private var mDownloadTask: DownloadTask? = null
    private var mUrlString: String? = null

    companion object {
        /**
         * Static initializer for NetworkFragment that sets the URL of the host it will be
         * downloading from.
         */
        fun getInstance(fragmentManager: android.support.v4.app.FragmentManager, url: String): NetworkFragment {
            val networkFragment = NetworkFragment()
            val args = Bundle()
            args.putString(URL_KEY, url)
            networkFragment.arguments = args
            fragmentManager.beginTransaction().add(networkFragment, TAG).commit()
            return networkFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUrlString = arguments?.getString(URL_KEY)

        // TODO ??
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // Host Activity will handle callbacks from task.
        mCallback = context as? DownloadCallback<String>
    }

    override fun onDetach() {
        super.onDetach()
        // Clear reference to host Activity to avoid memory leak.
        mCallback = null
    }

    override fun onDestroy() {
        // Cancel task when Fragment is destroyed.
        cancelDownload()
        super.onDestroy()
    }

    /**
     * Start non-blocking execution of DownloadTask.
     */
    fun startDownload() {
        cancelDownload()
        mCallback?.also { callback ->
            mDownloadTask = DownloadTask(callback).apply {
                execute(mUrlString)
            }
        }
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing DownloadTask execution.
     */
    fun cancelDownload() {
        mDownloadTask?.cancel(true)
    }

}
