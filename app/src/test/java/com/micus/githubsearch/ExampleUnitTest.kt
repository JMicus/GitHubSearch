package com.micus.githubsearch

import android.app.Instrumentation
import android.content.res.AssetManager
import android.util.Log
import com.beust.klaxon.Klaxon
import com.beust.klaxon.PathMatcher
import me.lazmaid.kraph.Kraph
import org.json.JSONObject
import org.junit.Test

import org.junit.Assert.*
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun graphQL() {

        // Query 1 with Kraph -> works
        val query1 = Kraph {
            query {
                field("viewer") {
                    field("name")
                }
            }
        }

        /* Query 2 with Kraph -> doesn't work
            maybe the arguments of "search" or the "on Repository" part should be different
         */
        val query2 = Kraph {
            query {
                fieldObject("search", args = mapOf("query" to "test", "type" to "REPOSITORY", "first" to 3)) {
                    fieldObject("edge") {
                        fieldObject("node") {
                            fieldObject("... on Repository") {
                                field("name")
                            }
                        }
                    }
                }
            }
        }


        /* Query 3 without Kraph
            Output of query 2 changed by hand, but still doesn't work
        */
        val queryString3 = "{\"query\": \"query { search (query: \"test\", type: REPOSITORY, first: 3) { edge { node { ... on Repository { name } } } }\"}"


        /* HINT
           { search { edge { cursor }}} does work but gives an error (missing arguments)
           { search (query:"x") { edge { cursor }}} returns a bad request (400)
         */


        println(query2.toGraphQueryString())

        println(query2.toRequestString())

        println(queryString3)
    }

    @Test
    fun loadJSON() {

        try {

            val json: String = File("../app/src/main/assets/exampleResponse.json").readText(Charsets.UTF_8)

            println(json)

            val pathMatcherCursor = object : PathMatcher {
                var values: MutableList<String> = ArrayList()
                override fun pathMatches(path: String) = Pattern.matches(".*data.*search.*edges.*cursor.*", path)
                override fun onMatch(path: String, value: Any) {
                    values.add(value as String)
                }
            }

            val pathMatcherNames = object : PathMatcher {
                var values: MutableList<String> = ArrayList()
                override fun pathMatches(path: String) = Pattern.matches(".*data.*search.*edges.*node.name.*", path)
                override fun onMatch(path: String, value: Any) {
                    values.add(value as String)
                }
            }

            val pathMatcherDescription = object : PathMatcher {
                var values: MutableList<String> = ArrayList()
                override fun pathMatches(path: String) = Pattern.matches(".*data.*search.*edges.*node.description.*", path)
                override fun onMatch(path: String, value: Any) {
                    values.add(value as String)
                }
            }

            val pathMatcherUrl = object : PathMatcher {
                var values: MutableList<String> = ArrayList()
                override fun pathMatches(path: String) = Pattern.matches(".*data.*search.*edges.*node.url.*", path)
                override fun onMatch(path: String, value: Any) {
                    values.add(value as String)
                }
            }

            val pathMatcherLanguage = object : PathMatcher {
                var values: MutableList<String> = ArrayList()
                override fun pathMatches(path: String) = Pattern.matches(".*data.*search.*edges.*node.primaryLanguage.name.*", path)
                override fun onMatch(path: String, value: Any) {
                    values.add(value as String)
                }
            }

            val pathMatcherColor = object : PathMatcher {
                var values: MutableList<String> = ArrayList()
                override fun pathMatches(path: String) = Pattern.matches(".*data.*search.*edges.*node.primaryLanguage.color.*", path)
                override fun onMatch(path: String, value: Any) {
                    values.add(value as String)
                }
            }

            Klaxon().pathMatcher(pathMatcherCursor).parseJsonObject(StringReader(json))

            println(pathMatcherCursor.values.get(0))


        } catch (e:Exception){
            e.printStackTrace()
        }


    }

    @Test
    fun serverRequest() {

        val mURL = URL("https://api.github.com/graphql")

        try {
            with(mURL.openConnection() as HttpURLConnection) {
                // optional default is GET
                requestMethod = "GET"

                //readTimeout = 2000
                //connectTimeout = 2000

                setRequestProperty("Authorization", "bearer  c76d37a0f98e738456d9"+"9ab5f424e9a331d2b910")

                setDoOutput(true)
                val wr = OutputStreamWriter(getOutputStream());

                // TODO example request
                var request = Query.create("example")

                println(request.toString())

                wr.write(request)

                wr.flush();

                println("URL : $url")
                println("Response Code : $responseCode")

                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuffer()

                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                    println("Response : $response")

                    println(response.toString())
                }
            }
        }catch (e: Exception) {
            //e.printStackTrace()
        }
    }
}
