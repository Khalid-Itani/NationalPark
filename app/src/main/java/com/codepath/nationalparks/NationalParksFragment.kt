package com.codepath.nationalparks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONObject

private const val API_KEY = "VGKDLm8jyF4Kc3t6vvBgKUXmlYRdv4cL5oI5VeEI"
private const val PARKS_URL = "https://developer.nps.gov/api/v1/parks"

class NationalParksFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("NationalParksFragment", "onCreateView reached")

        val view = inflater.inflate(R.layout.fragment_national_parks_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        updateAdapter()

        return view
    }

    private fun updateAdapter() {
        Log.d("NationalParksFragment", "updateAdapter called")
        progressBar.visibility = View.VISIBLE

        // Watchdog: if nothing returns in 12s, log it
        progressBar.postDelayed({
            if (progressBar.visibility == View.VISIBLE) {
                Log.e(
                    "NationalParksFragment",
                    "Still loading after 12s. Likely no internet, DNS blocked, or request not returning."
                )
            }
        }, 12_000)

        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = API_KEY
        params["limit"] = "50"
        params["start"] = "0"
        params["fields"] = "fullName,description,states,images"

        client[
            PARKS_URL,
            params,
            object : JsonHttpResponseHandler() {

                override fun onStart() {
                    Log.d("NationalParksFragment", "request started")
                }

                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    json: JSON
                ) {
                    Log.d("NationalParksFragment", "onSuccess status=$statusCode")

                    val dataJSON = json.jsonObject.get("data") as JSONArray
                    val parksRawJSON = dataJSON.toString()

                    val gson = Gson()
                    val listType = object : TypeToken<List<NationalPark>>() {}.type
                    val models: List<NationalPark> = gson.fromJson(parksRawJSON, listType)

                    recyclerView.adapter = NationalParksRecyclerViewAdapter(models)

                    Log.d("NationalParksFragment", "parsed parks=${models.size}")
                }

                // Some failures come through as a String
                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    response: String?,
                    throwable: Throwable?
                ) {
                    Log.e(
                        "NationalParksFragment",
                        "onFailure(String) status=$statusCode response=$response",
                        throwable
                    )
                }

                // Some failures come through as a JSONObject
                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: JSONObject?,
                    throwable: Throwable?
                ) {
                    Log.e(
                        "NationalParksFragment",
                        "onFailure(JSONObject) status=$statusCode response=$errorResponse",
                        throwable
                    )
                }

                // Always called after success or failure
                override fun onFinish() {
                    progressBar.visibility = View.GONE
                    Log.d("NationalParksFragment", "request finished, hiding progress bar")
                }
            }
        ]
    }
}
